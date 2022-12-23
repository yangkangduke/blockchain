package com.seeds.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.CountryContinentService;
import com.seeds.admin.dto.request.SysGameSrcAddReq;
import com.seeds.admin.dto.request.SysGameSrcPageReq;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.dto.response.GameSrcResp;
import com.seeds.admin.entity.SysGameSourceEntity;
import com.seeds.admin.enums.ContinentEnum;
import com.seeds.admin.enums.GameSrcTypeEnum;
import com.seeds.admin.enums.OsTypeEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysGameSourceMapper;
import com.seeds.admin.service.GameSourceService;
import com.seeds.admin.service.SysUserService;
import com.seeds.admin.utils.IPUtil;
import com.seeds.common.exception.SeedsException;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: hewei
 * @date 2022/12/17
 */

@Service
@Slf4j
public class GameSourceServiceImpl extends ServiceImpl<SysGameSourceMapper, SysGameSourceEntity> implements GameSourceService {

    @Autowired
    private FileProperties properties;

    @Autowired
    private FileTemplate template;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CountryContinentService countryContinentService;

    @Override
    public void upload(MultipartFile[] files, SysGameSrcAddReq req) {


        String gameBucketName = properties.getGame().getOss1().getBucketName();

        for (MultipartFile file : files) {
            String objectName = "game/";
            if (req.getSrcType().equals(GameSrcTypeEnum.INSTALL_PK.getCode())) {
                objectName += "packages/";
            }
            if (req.getSrcType().equals(GameSrcTypeEnum.MAIN_VIDEO.getCode())) {
                objectName += "video/";
            }
            if (req.getSrcType().equals(GameSrcTypeEnum.PATCH_PK.getCode())) {
                objectName += "patches/";
            }
            String originalFilename = file.getOriginalFilename();
            objectName += originalFilename;

            try {

                // 上传S3
                template.uploadMultipartFileByPart(file, gameBucketName, objectName);

                // 记录资源信息
                SysGameSourceEntity gameSrc = new SysGameSourceEntity();
                gameSrc.setSrcType(req.getSrcType());
                gameSrc.setFileName(originalFilename);
                gameSrc.setOs(req.getOs());
                gameSrc.setSrcSize(getPrintSize(file.getSize()));
                gameSrc.setS3Path(objectName);
                gameSrc.setJapanUrl(getFileUrl(properties.getGame().getOss1().getCdn(), objectName));
                gameSrc.setEuUrl(getFileUrl(properties.getGame().getOss2().getCdn(), objectName));
                gameSrc.setUsUrl(getFileUrl(properties.getGame().getOss3().getCdn(), objectName));
                gameSrc.setRemark(req.getRemark());
                gameSrc.setCreatedAt(System.currentTimeMillis());
                gameSrc.setUpdatedAt(System.currentTimeMillis());
//                gameSrc.setCreatedBy(UserContext.getCurrentAdminUserId());
                gameSrc.setCreatedBy(10L);
                gameSrc.setStatus(WhetherEnum.NO.value());
                save(gameSrc);
                log.info("文件url，{}", "https://" + properties.getGame().getOss1().getCdn() + "/" + objectName);
            } catch (Exception e) {
                log.error("文件上传失败，fileName={},e={}", originalFilename, e.getMessage());
                throw new GenericException("File upload failed");
            }
        }


    }

    @Override
    public List<GameFileResp> getAll() {
        List<GameFileResp> list = new ArrayList<>();
        List<S3ObjectSummary> allObjects = template.getAllObjects();
        if (!CollectionUtils.isEmpty(allObjects)) {
            list = allObjects.stream().map(p -> {
                GameFileResp gameFileResp = new GameFileResp();
                gameFileResp.setBucketName(p.getBucketName());
                gameFileResp.setKey(p.getKey());
                gameFileResp.setFileName(p.getKey().substring(p.getKey().lastIndexOf("/") + 1));
                gameFileResp.setJapanURL(getFileUrl(properties.getGame().getOss1().getCdn(), p.getKey()));
                gameFileResp.setEuURL(getFileUrl(properties.getGame().getOss2().getCdn(), p.getKey()));
                gameFileResp.setUsURL(getFileUrl(properties.getGame().getOss3().getCdn(), p.getKey()));
                gameFileResp.setSize(getPrintSize(p.getSize()));
                gameFileResp.setUploadTime(DateUtil.format(p.getLastModified(), "yyyy-MM-dd HH:mm:ss"));
                return gameFileResp;
            }).sorted(Comparator.comparing(GameFileResp::getUploadTime).reversed()).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public void delete(String fileName) {
        try {
            // todo 文件名被使用时，不让删除

            template.removeObject(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GameSrcLinkResp> getLinks(String ip, Integer type) {

        List<GameSrcLinkResp> respList = new ArrayList<>();
        try {

            LambdaQueryWrapper<SysGameSourceEntity> wrapper = new QueryWrapper<SysGameSourceEntity>().lambda()
                    .eq(SysGameSourceEntity::getSrcType, type)
                    .eq(SysGameSourceEntity::getStatus, WhetherEnum.YES.value());
            List<SysGameSourceEntity> list = list(wrapper);


            String ipPossession = IPUtil.getIpPossession(ip);
            String continent = countryContinentService.getCountryContinentMap().get(ipPossession);

            if (continent == null) {
                return list.stream().map(p -> {
                    GameSrcLinkResp rsp = new GameSrcLinkResp();
                    rsp.setOs(p.getOs());
                    rsp.setUrl(p.getJapanUrl());
                    rsp.setSrcName(p.getFileName());
                    return rsp;
                }).collect(Collectors.toList());
            }

            // 非洲、欧洲 访问位于欧洲的cdnURL
            if (continent.equals(ContinentEnum.AFRICA.getName()) || continent.equals(ContinentEnum.EUROPE.getName())) {
                respList = list.stream().map(p -> {
                    GameSrcLinkResp rsp = new GameSrcLinkResp();
                    rsp.setOs(p.getOs());
                    rsp.setUrl(p.getEuUrl());
                    rsp.setSrcName(p.getFileName());
                    return rsp;
                }).collect(Collectors.toList());
                // 南北美州 访问位于美国的cdnURL
            } else if (continent.equals(ContinentEnum.NORTH_AMERICA.getName()) || continent.equals(ContinentEnum.SOUTH_AMERICA.getName())) {
                respList = list.stream().map(p -> {
                    GameSrcLinkResp rsp = new GameSrcLinkResp();
                    rsp.setOs(p.getOs());
                    rsp.setUrl(p.getUsUrl());
                    rsp.setSrcName(p.getFileName());
                    return rsp;
                }).collect(Collectors.toList());
            } else {
                //其余地区 访问位于日本的cdnURL
                respList = list.stream().map(p -> {
                    GameSrcLinkResp rsp = new GameSrcLinkResp();
                    rsp.setOs(p.getOs());
                    rsp.setUrl(p.getJapanUrl());
                    rsp.setSrcName(p.getFileName());
                    return rsp;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respList;
    }

    @Override
    public IPage<GameSrcResp> queryPage(SysGameSrcPageReq req) {

        LambdaQueryWrapper<SysGameSourceEntity> queryWrap = new LambdaQueryWrapper<>();

        queryWrap.eq(!ObjectUtil.isEmpty(req.getSrcType()), SysGameSourceEntity::getSrcType, req.getSrcType())
                .like(!ObjectUtil.isEmpty(req.getFileName()), SysGameSourceEntity::getSrcType, req.getFileName())
                .eq(!ObjectUtil.isEmpty(req.getStatus()), SysGameSourceEntity::getStatus, req.getStatus())
                .orderByDesc(SysGameSourceEntity::getCreatedAt);

        Page<SysGameSourceEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysGameSourceEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            GameSrcResp resp = new GameSrcResp();
            BeanUtils.copyProperties(p, resp);
            resp.setOsName(OsTypeEnum.getNameByCode(p.getOs()));
            resp.setSrcTypeName(GameSrcTypeEnum.getNameByCode(p.getSrcType()));
            resp.setUploader(sysUserService.detail(p.getCreatedBy()).getRealName());
            return resp;
        });
    }

    @Override
    public Boolean switchStatus(SwitchReq req) {

        SysGameSourceEntity src = this.getById(req.getId());
        if (req.getStatus().equals(WhetherEnum.NO.value())) {

            long count = this.count(new QueryWrapper<SysGameSourceEntity>().lambda().eq(SysGameSourceEntity::getStatus, WhetherEnum.YES.value())
                    .eq(SysGameSourceEntity::getOs, src.getOs()));
            if (count == 1 && (src.getSrcType().equals(GameSrcTypeEnum.MAIN_VIDEO.getCode()) || src.getSrcType().equals(GameSrcTypeEnum.INSTALL_PK.getCode()))) {
                throw new SeedsException("There must be at least one enabled data");
            }
        }

        SysGameSourceEntity entity = new SysGameSourceEntity();
        entity.setId(req.getId());
        entity.setStatus(req.getStatus());
        return this.updateById(entity);
    }

    @Override
    public List<GameFileResp> getAllPatch() {
        List<GameFileResp> list = new ArrayList<>();
        List<S3ObjectSummary> allPatch = template.getAllObjectsByPrefix(properties.getGame().getOss1().getBucketName(), "game/patches/patch");
        if (!CollectionUtils.isEmpty(allPatch)) {
            list = allPatch.stream().map(p -> {
                GameFileResp gameFileResp = new GameFileResp();
                gameFileResp.setBucketName(p.getBucketName());
                gameFileResp.setKey(p.getKey());
                gameFileResp.setFileName(p.getKey().substring(p.getKey().lastIndexOf("/") + 1));
                gameFileResp.setJapanURL(getFileUrl(properties.getGame().getOss1().getCdn(), p.getKey()));
                gameFileResp.setEuURL(getFileUrl(properties.getGame().getOss2().getCdn(), p.getKey()));
                gameFileResp.setUsURL(getFileUrl(properties.getGame().getOss3().getCdn(), p.getKey()));
                gameFileResp.setSize(getPrintSize(p.getSize()));
                gameFileResp.setUploadTime(DateUtil.format(p.getLastModified(), "yyyy-MM-dd HH:mm:ss"));
                return gameFileResp;
            }).sorted(Comparator.comparing(GameFileResp::getUploadTime).reversed()).collect(Collectors.toList());
        }
        return list;
    }

    private String getFileUrl(String cdn, String objectName) {
        return String.format("https://%s/%s", cdn, objectName);
    }

    /**
     * 字节转kb/mb/gb
     *
     * @param size
     * @return
     */
    public String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }
}
