package com.seeds.admin.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.config.SeedsAdminApiConfig;
import com.seeds.admin.dto.SkinNFTAttrDto;
import com.seeds.admin.dto.SysNFTAttrDto;
import com.seeds.admin.dto.game.GameApplyAutoIdsDto;
import com.seeds.admin.dto.game.SkinNftPushAutoIdDto;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.request.chain.SkinNftCancelAssetDto;
import com.seeds.admin.dto.request.chain.SkinNftCancelAuctionDto;
import com.seeds.admin.dto.request.chain.SkinNftEnglishDto;
import com.seeds.admin.dto.request.chain.SkinNftListAssetDto;
import com.seeds.admin.dto.response.SysNftPicMIntedResp;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SkinNftEnums;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysNftPicMapper;
import com.seeds.admin.mq.producer.KafkaProducer;
import com.seeds.admin.service.SysGameApiService;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.service.SysNftSkinAsyncService;
import com.seeds.admin.utils.CsvUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.game.dto.request.internal.SkinNftWithdrawDto;
import com.seeds.game.entity.NftEquipment;
import com.seeds.game.entity.NftMarketOrderEntity;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.enums.NftHeroTypeEnum;
import com.seeds.game.feign.RemoteNftEquipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class SysNftPicImpl extends ServiceImpl<SysNftPicMapper, SysNftPicEntity> implements SysNftPicService {

    private String zipFilePath = "fileStorage/package";
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FileTemplate template;

    @Autowired
    private SysGameApiService gameApiService;
    @Autowired
    private SeedsAdminApiConfig seedsAdminApiConfig;

    @Autowired
    private SysNftSkinAsyncService skinAsyncService;

    @Autowired
    private RemoteNftEquipService remoteNftEquipService;

    @Override
    public IPage<SysNftPicResp> queryPage(SysNftPicPageReq req) {
        LambdaQueryWrapper<SysNftPicEntity> queryWrapper = new LambdaQueryWrapper<>();
        Long start = 0L;
        Long end = 0L;
        if (!Objects.isNull(req.getQueryTime())) {
            DateTime dateTime = DateUtil.parseDate(req.getQueryTime());
            start = DateUtil.beginOfDay(dateTime).getTime();
            end = DateUtil.endOfDay(dateTime).getTime();
        }
        queryWrapper.between(!Objects.isNull(req.getQueryTime()), SysNftPicEntity::getCreatedAt, start, end)
                .eq(!Objects.isNull(req.getRarity()), SysNftPicEntity::getRarity, req.getRarity())
                .eq(!Objects.isNull(req.getFeature()), SysNftPicEntity::getFeature, req.getFeature())
                .eq(!Objects.isNull(req.getColor()), SysNftPicEntity::getColor, req.getColor())
                .eq(!Objects.isNull(req.getAccessories()), SysNftPicEntity::getAccessories, req.getAccessories())
                .eq(!Objects.isNull(req.getDecorate()), SysNftPicEntity::getDecorate, req.getDecorate())
                .eq(!Objects.isNull(req.getOther()), SysNftPicEntity::getOther, req.getOther())
                .eq(!Objects.isNull(req.getHero()), SysNftPicEntity::getHero, req.getHero())
                .eq(!Objects.isNull(req.getSkin()), SysNftPicEntity::getSkin, req.getSkin())
                .eq(!Objects.isNull(req.getAutoId()), SysNftPicEntity::getAutoId, req.getAutoId())
                .eq(!Objects.isNull(req.getConfId()), SysNftPicEntity::getConfId, req.getConfId())
                .eq(!Objects.isNull(req.getTokenAddress()), SysNftPicEntity::getTokenAddress, req.getTokenAddress())
                .orderByDesc(SysNftPicEntity::getCreatedAt);

        Page<SysNftPicEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysNftPicEntity> records = this.page(page, queryWrapper).getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysNftPicResp resp = new SysNftPicResp();
            BeanUtils.copyProperties(p, resp);
            resp.setDesc(p.getDescription());
            return resp;
        });
    }

    @Override
    public IPage<SysNftPicMIntedResp> queryMintedPage(SysNftPicMintedPageReq req) {

        LambdaQueryWrapper<SysNftPicEntity> queryWrapper = new LambdaQueryWrapper<>();
        long start = 0L;
        long end = 0L;
        if (StringUtils.isNotBlank(req.getQueryTime())) {
            DateTime dateTime = DateUtil.parseDate(req.getQueryTime());
            start = DateUtil.beginOfDay(dateTime).getTime();
            end = DateUtil.endOfDay(dateTime).getTime();
        }
        queryWrapper.between(StringUtils.isNotBlank(req.getQueryTime()), SysNftPicEntity::getMintTime, start, end)
                .eq(!Objects.isNull(req.getAutoId()), SysNftPicEntity::getAutoId, req.getAutoId())
                .eq(!Objects.isNull(req.getConfId()), SysNftPicEntity::getConfId, req.getConfId())
                .eq( SysNftPicEntity::getMintState, SkinNftEnums.SkinMintStateEnum.MINTED.getCode())
                .eq(!Objects.isNull(req.getListState()), SysNftPicEntity::getListState, req.getListState())
                .eq(StringUtils.isNotBlank(req.getTokenAddress()), SysNftPicEntity::getTokenAddress, req.getTokenAddress())
                .orderByDesc(SysNftPicEntity::getMintTime);
        Page<SysNftPicEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysNftPicEntity> records = this.page(page, queryWrapper).getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        List<String> tokenAddresses = records.stream().map(SysNftPicEntity::getTokenAddress).collect(Collectors.toList());
        Map<String, NftEquipment> data = null;
        try {
            GenericDto<Map<String, NftEquipment>> result = remoteNftEquipService.getOwnerByMintAddress(tokenAddresses);
            data = result.getData();
        } catch (Exception e) {
            log.error("内部请求game获取tokenAddresses的owner");
        }
        Map<String, NftEquipment> finalData = data;
        return page.convert(p -> {
            SysNftPicMIntedResp resp = new SysNftPicMIntedResp();
            BeanUtils.copyProperties(p, resp);
            resp.setOwner(null != finalData ? finalData.get(p.getTokenAddress()).getOwner() : "");
            resp.setListState(null != finalData ? finalData.get(p.getTokenAddress()).getOnSale() : 0);
            return resp;
        });
    }


    @Override
    public Integer upload(MultipartFile file) {

        List<SysNftPicEntity> batchUpdate = new ArrayList<>();
        // 解析CSV文件
        List<SysNFTAttrDto> sysNFTAttrDtos = CsvUtils.getCsvData(file, SysNFTAttrDto.class);
        List<String> heros = sysNFTAttrDtos.stream().map(p -> p.getHero().toLowerCase()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(sysNFTAttrDtos)) {
            sysNFTAttrDtos.forEach(p -> {
                SysNftPicEntity one = this.getOne(new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getPicName, p.getPictureName()));
                if (!Objects.isNull(one) && one.getPicName().equals(p.getPictureName())) {
                    BeanUtils.copyProperties(p, one);
                    one.setUpdatedAt(System.currentTimeMillis());
                    one.setProfession(NftHeroTypeEnum.getProfession(p.getHero()));
                    batchUpdate.add(one);
                }
            });
        }
        if (!CollectionUtils.isEmpty(batchUpdate)) {
            // 批量更新属性
            this.updateBatchById(batchUpdate);
           }
        return batchUpdate.size();
    }

    @Override
    public String getAttr(Long id) {

        SysNftPicEntity entity = this.getById(id);
        SkinNFTAttrDto attr = this.handleAttr(entity, null);
        return JSONUtil.toJsonStr(attr);
    }

    @Override
    public SkinNFTAttrDto handleAttr(SysNftPicEntity entity, SkinNftWithdrawDto withdrawDto) {
        SkinNFTAttrDto jsonDto = new SkinNFTAttrDto();
        jsonDto.setName(NFTEnumConstant.TokenNamePreEnum.SEEDS_SKIN.getName() + entity.getTokenId());
        jsonDto.setImage(seedsAdminApiConfig.getShadowUrl() + entity.getTokenId() + ".png");
        ArrayList<SkinNFTAttrDto.Attributes> attributesList = new ArrayList<>();

        SkinNFTAttrDto.Attributes configId = new SkinNFTAttrDto.Attributes();
        configId.setTrait_type(SkinNftEnums.NftAttrEnum.CONFIGID.getName());
        configId.setValue(entity.getConfId().toString());
        attributesList.add(configId);

        SkinNFTAttrDto.Attributes autoId = new SkinNFTAttrDto.Attributes();
        autoId.setTrait_type(SkinNftEnums.NftAttrEnum.AUTOID.getName());
        autoId.setValue(entity.getAutoId().toString());
        attributesList.add(autoId);

        SkinNFTAttrDto.Attributes rarity = new SkinNFTAttrDto.Attributes();
        rarity.setTrait_type(SkinNftEnums.NftAttrEnum.RARITY.getName());
        rarity.setValue(entity.getRarity());
        attributesList.add(rarity);

        SkinNFTAttrDto.Attributes feature = new SkinNFTAttrDto.Attributes();
        feature.setTrait_type(SkinNftEnums.NftAttrEnum.FEATURE.getName());
        feature.setValue(entity.getFeature());
        attributesList.add(feature);

        SkinNFTAttrDto.Attributes color = new SkinNFTAttrDto.Attributes();
        color.setTrait_type(SkinNftEnums.NftAttrEnum.COLOR.getName());
        color.setValue(entity.getColor());
        attributesList.add(color);

        SkinNFTAttrDto.Attributes accessories = new SkinNFTAttrDto.Attributes();
        accessories.setTrait_type(SkinNftEnums.NftAttrEnum.ACCESSORIES.getName());
        accessories.setValue(entity.getAccessories());
        attributesList.add(accessories);

        SkinNFTAttrDto.Attributes decorate = new SkinNFTAttrDto.Attributes();
        decorate.setTrait_type(SkinNftEnums.NftAttrEnum.DECORATE.getName());
        decorate.setValue(entity.getDecorate());
        attributesList.add(decorate);

        SkinNFTAttrDto.Attributes other = new SkinNFTAttrDto.Attributes();
        other.setTrait_type(SkinNftEnums.NftAttrEnum.OTHER.getName());
        other.setValue(entity.getOther());
        attributesList.add(other);

        SkinNFTAttrDto.Attributes hero = new SkinNFTAttrDto.Attributes();
        hero.setTrait_type(SkinNftEnums.NftAttrEnum.HERO.getName());
        hero.setValue(entity.getHero());
        attributesList.add(hero);

        SkinNFTAttrDto.Attributes skin = new SkinNFTAttrDto.Attributes();
        skin.setTrait_type(SkinNftEnums.NftAttrEnum.SKIN.getName());
        skin.setValue(entity.getSkin());
        attributesList.add(skin);

        // 处理游戏中的击杀等数据
        handleGameAttr(attributesList, withdrawDto);
        jsonDto.setAttributes(attributesList);
        SkinNFTAttrDto.Properties.Files files = new SkinNFTAttrDto.Properties.Files();
        files.setType("image/png");
        files.setUri(seedsAdminApiConfig.getShadowUrl() + entity.getTokenId() + ".png");
        ArrayList<SkinNFTAttrDto.Properties.Files> fileList = new ArrayList<>();
        fileList.add(files);
        SkinNFTAttrDto.Properties properties = new SkinNFTAttrDto.Properties();
        properties.setFiles(fileList);
        jsonDto.setProperties(properties);
        return jsonDto;
    }

    private void handleGameAttr(ArrayList<SkinNFTAttrDto.Attributes> attributesList, SkinNftWithdrawDto withdrawDto) {
        SkinNFTAttrDto.Attributes win = new SkinNFTAttrDto.Attributes();
        win.setTrait_type(SkinNftEnums.NftAttrEnum.WIN.getName());

        SkinNFTAttrDto.Attributes defeat = new SkinNFTAttrDto.Attributes();
        defeat.setTrait_type(SkinNftEnums.NftAttrEnum.DEFEAT.getName());

        SkinNFTAttrDto.Attributes seqWin = new SkinNFTAttrDto.Attributes();
        seqWin.setTrait_type(SkinNftEnums.NftAttrEnum.SEQWIN.getName());

        SkinNFTAttrDto.Attributes seqDefeat = new SkinNFTAttrDto.Attributes();
        seqDefeat.setTrait_type(SkinNftEnums.NftAttrEnum.SEQDEFEAT.getName());

        SkinNFTAttrDto.Attributes seqKill = new SkinNFTAttrDto.Attributes();
        seqKill.setTrait_type(SkinNftEnums.NftAttrEnum.SEQKILL.getName());

        SkinNFTAttrDto.Attributes killPlayer = new SkinNFTAttrDto.Attributes();
        killPlayer.setTrait_type(SkinNftEnums.NftAttrEnum.KILLPLAYER.getName());

        SkinNFTAttrDto.Attributes killNpc = new SkinNFTAttrDto.Attributes();
        killNpc.setTrait_type(SkinNftEnums.NftAttrEnum.KILLNPC.getName());

        SkinNFTAttrDto.Attributes killedByPlayer = new SkinNFTAttrDto.Attributes();
        killedByPlayer.setTrait_type(SkinNftEnums.NftAttrEnum.KILLEDBYPLAYER.getName());

        SkinNFTAttrDto.Attributes killedByNPC = new SkinNFTAttrDto.Attributes();
        killedByNPC.setTrait_type(SkinNftEnums.NftAttrEnum.KILLEDBYNPC.getName());

        if (null != withdrawDto) {
            win.setValue(String.valueOf(withdrawDto.getVictory()));
            defeat.setValue(String.valueOf(withdrawDto.getLose()));
            seqWin.setValue(String.valueOf(withdrawDto.getMaxStreak()));
            seqDefeat.setValue(String.valueOf(withdrawDto.getMaxLose()));
            seqKill.setValue(String.valueOf(withdrawDto.getKillingSpree()));
            killPlayer.setValue(String.valueOf(withdrawDto.getCapture()));
            killNpc.setValue(String.valueOf(withdrawDto.getGoblinKill()));
            killedByPlayer.setValue(String.valueOf(withdrawDto.getSlaying()));
            killedByNPC.setValue(String.valueOf(withdrawDto.getGoblin()));
        } else {
            win.setValue("0");
            defeat.setValue("0");
            seqWin.setValue("0");
            seqDefeat.setValue("0");
            seqKill.setValue("0");
            killPlayer.setValue("0");
            killNpc.setValue("0");
            killedByPlayer.setValue("0");
            killedByNPC.setValue("0");
        }
        attributesList.add(win);
        attributesList.add(defeat);
        attributesList.add(seqDefeat);
        attributesList.add(seqKill);
        attributesList.add(killPlayer);
        attributesList.add(killNpc);
        attributesList.add(killedByPlayer);
        attributesList.add(killedByNPC);
    }

    @Override
    public void updateAttribute(SysNftPicAttributeModifyReq req) {
        log.info("NftAttributeModifyReq == {}", req);
        SysNftPicEntity entity = this.getById(req.getId());
        if (Objects.nonNull(entity) && entity.getMintState().equals(SkinNftEnums.SkinMintStateEnum.MINTED)) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40025_CAN_NOT_MODIFY);
        }
        SysNftPicEntity sysNftPicEntity = new SysNftPicEntity();
        BeanUtils.copyProperties(req, sysNftPicEntity);
        this.updateById(sysNftPicEntity);
    }

    @Override
    public void getPackageDownload(HttpServletResponse response, ListReq req) {
        List<String> fileUrlList = new ArrayList<>();

        List<SysNftPicEntity> list = this.list(new LambdaQueryWrapper<SysNftPicEntity>().in(SysNftPicEntity::getId, req.getIds()));
        fileUrlList = list.stream().map(p -> p.getJsonUrl()).collect(Collectors.toList());
        String packageName = "asset.zip";
        //判断集合是否有路径
        if (fileUrlList.size() != 0) {
            // 压缩输出流,包装流,将临时文件输出流包装成压缩流,将所有文件输出到这里,打成zip包
            ZipOutputStream zipOut = null;
            try {

                zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath + packageName));
            } catch (FileNotFoundException e) {
                log.error("创建压缩输出流错误，{}", e.getMessage());
            }
            // 循环调用压缩文件方法,将一个一个需要下载的文件打入压缩文件包
            for (String path : fileUrlList) {
                try {
                    //调用工具类方法,传递路径和压缩流，压缩包文件的名字
                    fileToZip(path, zipOut);
                } catch (Exception e) {
                    log.error("打包文件错误，{}", e.getMessage());
                }
            }

            try {
                // 压缩完成后,关闭压缩流
                zipOut.close();
            } catch (IOException e) {
                log.error("关闭压缩流错误，{}", e.getMessage());
            }

            //设置内容内容型应用下载，设置字符集
            response.setContentType("application/x-download;charset=UTF-8");
            //告诉客户端该文件不是直接解析而是以附件的形式打开（下载）
            response.setHeader("Content-Disposition", "attachment;filename=" + packageName);
            //提高作用域
            ServletOutputStream outputStream = null;
            FileInputStream inputStream = null;
            try {
                //该流不可以手动关闭,手动关闭下载会出问题,下载完成后会自动关闭
                outputStream = response.getOutputStream();
                inputStream = new FileInputStream(zipFilePath + packageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //文件下载，复制
            IoUtil.copy(inputStream, outputStream);
            // 关闭输入流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("关闭输入流错误，{}", e.getMessage());
            }

            //下载完成，删掉zip包
            File fileTempZip = new File(zipFilePath + packageName);
            fileTempZip.delete();
        }
    }

    @Override
    public void applyAutoIds(ListReq ids) {

        Set<Long> noApplyIds = this.listByIds(ids.getIds())
                .stream()
                .filter(i -> i.getApplyState().equals(SkinNftEnums.AutoIdApplyStateEnum.NO_APPLY.getCode()))
                .map(p -> p.getConfId())
                .collect(Collectors.toSet());

        GameApplyAutoIdsDto dto = new GameApplyAutoIdsDto();
        dto.setConfigIds(noApplyIds);
        List<String> url = gameApiService.queryUrlByGameAndType(1L, ApiType.APPLY_AUTOID.getCode());
        String applyUrl = url.get(0);
        String param = JSONUtil.toJsonStr(dto);
        log.info("调用游戏方接口申请autoId, url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(applyUrl)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("调用游戏方接口申请autoId,接口返回result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            int ret = (int) jsonObject.get("ret");
            if (ret == 0) {
                List<SysNftPicEntity> updateList = noApplyIds.stream().map(p -> {
                    SysNftPicEntity entity = new SysNftPicEntity();
                    entity.setConfId(p);
                    entity.setApplyState(SkinNftEnums.AutoIdApplyStateEnum.APPLYING.getCode());
                    return entity;
                }).collect(Collectors.toList());
                this.updateBatchByQueryWrapper(updateList, item ->
                        new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getConfId, item.getConfId()));
            }

        } catch (Exception e) {
            log.error("调用游戏方接口申请autoId失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void pushAutoId(SkinNftPushAutoIdDto dto) {
        Set<Long> configIds = dto.getAutoIds().keySet();
        List<Long> validConfigIds = this.list(new LambdaQueryWrapper<SysNftPicEntity>()
                .in(SysNftPicEntity::getConfId, configIds)
                .eq(SysNftPicEntity::getAutoId, WhetherEnum.NO.value()))
                .stream()
                .map(p -> p.getConfId())
                .collect(Collectors.toList());

        List<SysNftPicEntity> updateList = dto.getAutoIds().keySet().stream().filter(i -> validConfigIds.contains(i)).map(p -> {
            SysNftPicEntity entity = new SysNftPicEntity();
            entity.setConfId(p);
            entity.setAutoId(dto.getAutoIds().get(p));
            entity.setApplyState(SkinNftEnums.AutoIdApplyStateEnum.APPLIED.getCode());
            return entity;
        }).collect(Collectors.toList());
        this.updateBatchByQueryWrapper(updateList, item ->
                new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getConfId, item.getConfId()));
    }

    @Override
    public void skinMint(SysSkinNftMintReq req) {
        List<SysNftPicEntity> records = this.listByIds(req.getIds());
        records.forEach(p -> {
            if (!p.getApplyState().equals(SkinNftEnums.AutoIdApplyStateEnum.APPLIED.getCode())
                    || p.getAutoId().equals(Long.valueOf(WhetherEnum.NO.value()))) {
                throw new GenericException(AdminErrorCodeEnum.ERR_40023_INVALID_RECORD, p.getPicName());
            }
            validate(p);
            p.setMintState(SkinNftEnums.SkinMintStateEnum.MINTING.getCode());
        });
        this.updateBatchById(records);

        skinAsyncService.skinMint(req);
    }

    @Override
    public void listAsset(SysSkinNftListAssetReq req) {

        List<SysNftPicEntity> list = this.listByIds(req.getIds());
        List<SkinNftListAssetDto> listAssetDto = list.stream().map(p -> {
            SkinNftListAssetDto dto = new SkinNftListAssetDto();
            dto.setNftAddress(p.getTokenAddress());
            dto.setPrice(req.getPrice());
            dto.setAuctionHouse(req.getAuctionHouseAddress());
            return dto;
        }).collect(Collectors.toList());
        String url = seedsAdminApiConfig.getBaseDomain() + seedsAdminApiConfig.getListAsset();
        String param = JSONUtil.toJsonStr(listAssetDto);
        log.info("请求skin-list-asset接口， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(60 * 1000 * 10)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            // 更新上架状态
            log.info(" list-asset 成功--result:{}", jsonObject);
            if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
                list.forEach(p -> p.setListState(SkinNftEnums.SkinNftListStateEnum.LISTED.getCode()));
                this.updateBatchById(list);
            }
        } catch (Exception e) {
            log.info(" 请求skin-list-asset接口--出错:{}", e.getMessage());
            throw new GenericException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    @Override
    public void englishV2(SysSkinNftEnglishReq req) {
        List<SysNftPicEntity> list = this.listByIds(req.getIds());
        List<SkinNftEnglishDto> englishDtos = list.stream().map(p -> {
            SkinNftEnglishDto dto = new SkinNftEnglishDto();
            BeanUtils.copyProperties(req, dto);
            dto.setMintAddress(p.getTokenAddress());
            return dto;
        }).collect(Collectors.toList());
        String url = seedsAdminApiConfig.getBaseDomain() + seedsAdminApiConfig.getEnglish();
        String param = JSONUtil.toJsonStr(englishDtos);
        log.info("请求skin-englishV2-接口， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(60 * 1000 * 10)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            // 更新上架状态
            log.info(" englishV2-->result:{}", jsonObject);
            if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
                list.forEach(p -> p.setListState(SkinNftEnums.SkinNftListStateEnum.ON_AUCTION.getCode()));
                this.updateBatchById(list);
            }
        } catch (Exception e) {
            log.info(" 请求skin-englishV2-出错:{}", e.getMessage());
            throw new GenericException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    @Override
    public void shadowUploadSuccess(ListStringReq req) {
        List<Long> tokenIds = req.getFileNames().stream().map(p -> Long.parseLong(p.substring(0, p.indexOf(".")))).
                collect(Collectors.toList());
        List<SysNftPicEntity> list = this.list(new LambdaQueryWrapper<SysNftPicEntity>().in(SysNftPicEntity::getTokenId, tokenIds));
        list.forEach(p -> p.setMintState(SkinNftEnums.SkinMintStateEnum.MINTED.getCode()));
        this.updateBatchById(list);
    }

    @Override
    public void cancelAsset(SysSkinNftListAssetReq req) {
        List<SysNftPicEntity> list = this.listByIds(req.getIds());

        List<NftMarketOrderEntity> receipts = baseMapper.getListReceiptByMintAddress(list.stream().map(SysNftPicEntity::getTokenAddress).collect(Collectors.toList()));
        Map<String, String> receiptsMap = receipts.stream().collect(Collectors.toMap(NftMarketOrderEntity::getMintAddress, NftMarketOrderEntity::getListReceipt));
        List<SkinNftCancelAssetDto> dtos = list.stream().map(p -> {
            SkinNftCancelAssetDto skinNftCancelAssetDto = new SkinNftCancelAssetDto();
            skinNftCancelAssetDto.setListReceipt(receiptsMap.get(p.getTokenAddress()));
            skinNftCancelAssetDto.setAuctionHouse(req.getAuctionHouseAddress());
            return skinNftCancelAssetDto;
        }).collect(Collectors.toList());

        String url = seedsAdminApiConfig.getBaseDomain() + seedsAdminApiConfig.getCancelAsset();
        String param = JSONUtil.toJsonStr(dtos);
        log.info("请求skin-cancelAsset-接口， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(60 * 1000 * 10)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            // 更新下架状态
            log.info(" englishV2 成功--result:{}", jsonObject);
            if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
                list.forEach(p -> p.setListState(SkinNftEnums.SkinNftListStateEnum.NO_LIST.getCode()));
                this.updateBatchById(list);
            }
        } catch (Exception e) {
            log.info(" 请求skin-cancelAsset-出错:{}", e.getMessage());
            throw new GenericException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }


    @Override
    public void cancelAuction(ListReq req) {
        List<SysNftPicEntity> list = this.listByIds(req.getIds());
        List<NftMarketOrderEntity> receipts = baseMapper.getAuctionIdByMintAddress(list.stream().map(SysNftPicEntity::getTokenAddress).collect(Collectors.toList()));
        Map<String, Long> receiptsMap = receipts.stream().collect(Collectors.toMap(NftMarketOrderEntity::getMintAddress, NftMarketOrderEntity::getAuctionId));
        List<SkinNftCancelAuctionDto> dtos = list.stream().map(p -> {
            SkinNftCancelAuctionDto skinNftCancelAssetDto = new SkinNftCancelAuctionDto();
            skinNftCancelAssetDto.setAuctionId(receiptsMap.get(p.getTokenAddress()));
            return skinNftCancelAssetDto;
        }).collect(Collectors.toList());

        String url = seedsAdminApiConfig.getBaseDomain() + seedsAdminApiConfig.getCancelAuction();
        String param = JSONUtil.toJsonStr(dtos);
        log.info("请求skin-cancelAuction-接口， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(60 * 1000 * 10)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            // 更新取消拍卖状态
            log.info(" cancelAuction 成功--result:{}", jsonObject);
            if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
                list.forEach(p -> p.setListState(SkinNftEnums.SkinNftListStateEnum.CANCEL_AUCTION.getCode()));
                this.updateBatchById(list);
            }
        } catch (Exception e) {
            log.info(" 请求skin-cancelAsset-出错:{}", e.getMessage());
        }

    }


    private void validate(SysNftPicEntity p) {
        if (StringUtils.isBlank(p.getRarity()) ||
                StringUtils.isBlank(p.getRarity()) ||
                StringUtils.isBlank(p.getFeature()) ||
                StringUtils.isBlank(p.getColor()) ||
                StringUtils.isBlank(p.getSkin()) ||
                p.getConfId().equals(Long.valueOf(WhetherEnum.NO.value()))
        ) {
            throw new GenericException(AdminErrorCodeEnum.ERR_40024_MISS_ATTR_RECORD, p.getPicName());
        }
    }

    private void fileToZip(String filePath, ZipOutputStream zipOut) throws IOException {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        //创建文件输入流
        String str1 = filePath.split("\\?")[0];
        String bucketName = str1.substring(str1.lastIndexOf("/") + 1);
        String objectName = filePath.substring(filePath.lastIndexOf("=") + 1);
        // 下载文件
        InputStream inputStream = template.getObject(bucketName, objectName).getObjectContent();
        byte[] bufferArea = new byte[1024 * 10];
        BufferedInputStream bufferStream = new BufferedInputStream(inputStream, 1024 * 10);
        // 将当前文件作为一个zip实体写入压缩流,realFileName代表压缩文件中的文件名称
        zipOut.putNextEntry(new ZipEntry(fileName));
        int length = 0;
        // 写操作
        while ((length = bufferStream.read(bufferArea, 0, 1024 * 10)) != -1) {
            zipOut.write(bufferArea, 0, length);
        }
        //关闭流
        // 需要注意的是缓冲流必须要关闭流,否则输出无效
        bufferStream.close();
        // 压缩流不必关闭,使用完后再关
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchByQueryWrapper(Collection<SysNftPicEntity> entityList, Function<SysNftPicEntity, LambdaQueryWrapper> wrapperFunction) {
        String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE);
        return this.executeBatch(entityList, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            MapperMethod.ParamMap param = new MapperMethod.ParamMap();
            param.put(Constants.ENTITY, entity);
            param.put(Constants.WRAPPER, wrapperFunction.apply(entity));
            sqlSession.update(sqlStatement, param);
        });
    }
}
