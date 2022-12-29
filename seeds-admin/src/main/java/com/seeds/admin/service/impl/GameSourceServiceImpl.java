package com.seeds.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.CountryContinentService;
import com.seeds.admin.dto.request.SysGameSrcAddReq;
import com.seeds.admin.dto.request.SysGameSrcPageReq;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.dto.response.GameSrcResp;
import com.seeds.admin.entity.SysGameSourceEntity;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.*;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysGameSourceMapper;
import com.seeds.admin.service.GameSourceService;
import com.seeds.admin.service.SysUserService;
import com.seeds.admin.utils.IPUtil;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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
                template.uploadMultipartFileByPart(file.getInputStream(), file.getContentType(), file.getOriginalFilename(), gameBucketName, objectName,file.getSize());

                LambdaQueryWrapper<SysGameSourceEntity> wrapper = new LambdaQueryWrapper<SysGameSourceEntity>().eq(SysGameSourceEntity::getFileName, originalFilename);
                SysGameSourceEntity one = this.getOne(wrapper);

                if (Objects.isNull(one)) {
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
                    gameSrc.setCreatedBy(UserContext.getCurrentAdminUserId());
                    gameSrc.setUpdatedBy(UserContext.getCurrentAdminUserId());
                    if (req.getSrcType().equals(GameSrcTypeEnum.PATCH_PK.getCode())) {
                        gameSrc.setStatus(WhetherEnum.YES.value());
                    } else {
                        gameSrc.setStatus(WhetherEnum.NO.value());
                    }
                    save(gameSrc);
                } else {
                    //更新
                    one.setUpdatedAt(System.currentTimeMillis());
                    one.setUpdatedBy(UserContext.getCurrentAdminUserId());
                    one.setSrcSize(getPrintSize(file.getSize()));
                    this.updateById(one);
                }
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
    public Boolean delete(String fileName) throws Exception {
            LambdaQueryWrapper<SysGameSourceEntity> wrapper = new QueryWrapper<SysGameSourceEntity>().lambda()
                    .eq(SysGameSourceEntity::getS3Path, fileName)
                    .eq(SysGameSourceEntity::getStatus, WhetherEnum.YES.value());
            List<SysGameSourceEntity> list = list(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                throw new GenericException(AdminErrorCodeEnum.ERR_120001_CANNOT_DELETE_RESOURCE_IN_ENABLE_STATE);
            }
            template.removeObject(fileName);
        return true;
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
    public List<GameSrcResp> queryPage(SysGameSrcPageReq req) {

        LambdaQueryWrapper<SysGameSourceEntity> queryWrap = new LambdaQueryWrapper<>();

        queryWrap.eq(!ObjectUtil.isEmpty(req.getSrcType()), SysGameSourceEntity::getSrcType, req.getSrcType())
                .like(!ObjectUtil.isEmpty(req.getFileName()), SysGameSourceEntity::getFileName, req.getFileName())
                .eq(!ObjectUtil.isEmpty(req.getStatus()), SysGameSourceEntity::getStatus, req.getStatus())
                .orderByDesc(SysGameSourceEntity::getStatus)
                .orderByDesc(SysGameSourceEntity::getCreatedAt);

        List<SysGameSourceEntity> entityList = list(queryWrap);

        List<String> paths = entityList.stream().map(SysGameSourceEntity::getFileName).collect(Collectors.toList());
        List<GameSrcResp> filePathTree = getFilePathTree(paths, entityList);

        filePathTree = filePathTree.stream().map(p -> {
            GameSrcResp gameSrcResp = new GameSrcResp();
            BeanUtils.copyProperties(p, gameSrcResp);
            for (SysGameSourceEntity entity : entityList) {
                if (entity.getFileName().equals(p.getFilePath())) {
                    BeanUtils.copyProperties(entity, gameSrcResp);
                    gameSrcResp.setOsName(OsTypeEnum.getNameByCode(entity.getOs()));
                    gameSrcResp.setSrcTypeName(GameSrcTypeEnum.getNameByCode(entity.getSrcType()));
                    SysUserEntity creator = sysUserService.getById(entity.getCreatedBy());
                    gameSrcResp.setUploader(Objects.isNull(creator) ? "" : creator.getRealName());
                    SysUserEntity updater = sysUserService.getById(entity.getUpdatedBy());
                    gameSrcResp.setUpdatedBy(Objects.isNull(updater) ? "" : updater.getRealName());
                }
            }
            return gameSrcResp;
        }).collect(Collectors.toList());
        return filePathTree;
    }

    @Override
    public Boolean switchStatus(SwitchReq req) {

        SysGameSourceEntity src = this.getById(req.getId());
        if (src.getStatus().equals(req.getStatus())) {
            return true;
        }
        if (req.getStatus().equals(WhetherEnum.NO.value())) {

            long count = this.count(new QueryWrapper<SysGameSourceEntity>().lambda().eq(SysGameSourceEntity::getStatus, WhetherEnum.YES.value())
                    .eq(SysGameSourceEntity::getSrcType, src.getSrcType())
                    .eq(SysGameSourceEntity::getOs, src.getOs()));

            if ((count == 1 && src.getStatus().equals(WhetherEnum.YES.value())) && (src.getSrcType().equals(GameSrcTypeEnum.MAIN_VIDEO.getCode()) || src.getSrcType().equals(GameSrcTypeEnum.INSTALL_PK.getCode()))) {
                throw new GenericException(AdminErrorCodeEnum.ERR_120002_THERE_MUST_BE_AT_LEAST_ONE_ENABLED_DATA);
            }
        }


        SysGameSourceEntity entity = new SysGameSourceEntity();
        BeanUtils.copyProperties(src, entity);
        entity.setId(req.getId());
        entity.setStatus(req.getStatus());
        this.updateById(entity);

        if (src.getSrcType().equals(GameSrcTypeEnum.MAIN_VIDEO.getCode()) || src.getSrcType().equals(GameSrcTypeEnum.INSTALL_PK.getCode())) {
            LambdaQueryWrapper<SysGameSourceEntity> wrapper = new QueryWrapper<SysGameSourceEntity>().lambda()
                    .ne(SysGameSourceEntity::getId, req.getId())
                    .eq(SysGameSourceEntity::getSrcType, src.getSrcType())
                    .eq(SysGameSourceEntity::getOs, src.getOs());
            List<SysGameSourceEntity> list = list(wrapper);
            list.stream().forEach(p -> p.setStatus(WhetherEnum.NO.value()));
            this.updateBatchById(list);
        }
        return true;
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


    private List<GameSrcResp> getFilePathTree(List<String> paths, List<SysGameSourceEntity> entityList) {
        Map<String, Integer> map = new LinkedHashMap<>();
        Integer id = 1;
        for (int i = 0; i < paths.size(); i++) {
            String[] path = paths.get(i).split("/");
            String p = "";
            for (int j = 0; j < path.length; j++) {
                p += path[j] + "/";
                if (!map.containsKey(p.substring(0, p.length() - 1))) {
                    map.put(p.substring(0, p.length() - 1), id++);
                }
            }
        }

        List<GameSrcResp> srcResps = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            GameSrcResp srcResp = new GameSrcResp();
            Integer values = entry.getValue();
            String[] keys = entry.getKey().split("/");
            srcResp.setVId(values);
            if (keys.length == 1) {
                srcResp.setVParentId(0);
                srcResp.setFileName(keys[0]);
                srcResp.setFilePath(keys[0]);
            } else {
                String path = "";
                for (int i = 0; i < keys.length - 1; i++) {
                    path += keys[i] + "/";
                }
                srcResp.setFileName(keys[keys.length - 1]);
                srcResp.setFilePath(String.join("/", keys));
                path = path.substring(0, path.length() - 1);
                srcResp.setVParentId(map.get(path));
            }
            for (SysGameSourceEntity entity : entityList) {
                if (entity.getFileName().equals(srcResp.getFilePath())) {
                    BeanUtils.copyProperties(entity, srcResp);
                    srcResp.setOsName(OsTypeEnum.getNameByCode(entity.getOs()));
                    srcResp.setSrcTypeName(GameSrcTypeEnum.getNameByCode(entity.getSrcType()));
                    srcResp.setUploader(sysUserService.detail(entity.getCreatedBy()).getRealName());
                    srcResp.setUpdatedBy(sysUserService.detail(entity.getUpdatedBy()).getRealName());
                }
            }


            srcResps.add(srcResp);
        }
        //获取父节点
        List<GameSrcResp> collect = srcResps.stream().filter(m -> m.getVParentId() == 0).map(
                (m) -> {
                    m.setChildList(getChildrens(m, srcResps));
                    return m;
                }
        ).collect(Collectors.toList());

        return collect;
    }

    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     * @return 根节点信息
     */
    private List<GameSrcResp> getChildrens(GameSrcResp root, List<GameSrcResp> all) {
        List<GameSrcResp> children = all.stream().filter(m -> {
            return Objects.equals(m.getVParentId(), root.getVId());
        }).map((m) -> {
                    m.setChildList(getChildrens(m, all));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }


}
