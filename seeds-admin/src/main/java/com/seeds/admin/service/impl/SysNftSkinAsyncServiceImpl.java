package com.seeds.admin.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.seeds.admin.config.SeedsAdminApiConfig;
import com.seeds.admin.dto.SkinNFTAttrDto;
import com.seeds.admin.dto.SysSkinNftMintSuccessDto;
import com.seeds.admin.dto.game.SkinNftMintSuccessDto;
import com.seeds.admin.dto.request.SysSkinNftMintReq;
import com.seeds.admin.dto.request.chain.SkinNftMintDto;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SkinNftEnums;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.service.SysFileService;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.service.SysNftSkinAsyncService;
import com.seeds.admin.utils.CreateJsonFileUtil;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.game.dto.request.internal.NftPublicBackpackDto;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.enums.PatformEnum;
import com.seeds.game.feign.RemoteNftBackpackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: hewei
 * @date 2023/4/27
 */

@Slf4j
@Service
public class SysNftSkinAsyncServiceImpl implements SysNftSkinAsyncService {

    private static final String TMP_FILE_PATH = "fileStorage/skin/json/";
    @Autowired
    private FileTemplate template;

    @Autowired
    private FileProperties properties;

    @Autowired
    private SeedsAdminApiConfig seedsAdminApiConfig;

    @Autowired
    private AsyncNotifyGameServiceImpl notifyGameService;

    @Autowired
    @Lazy
    private SysNftPicService nftPicService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private RemoteNftBackpackService backpackService;


    @Override
    //   @Async   改成同步执行
    public void skinMint(SysSkinNftMintReq dto) {
        List<SysNftPicEntity> nfts = nftPicService.listByIds(dto.getIds());
        String url = seedsAdminApiConfig.getBaseDomain() + seedsAdminApiConfig.getMintNft();
        SkinNftMintDto mintDto = new SkinNftMintDto();
        mintDto.setAmount(dto.getIds().size());
        String param = JSONUtil.toJsonStr(mintDto);
        log.info("请求skin-mint-nft接口， url:{}， params:{}", url, param);
        HttpResponse response = null;
        try {
            response = HttpRequest.post(url)
                    .timeout(60 * 1000 * 10)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            nfts.forEach(p -> p.setMintState(SkinNftEnums.SkinMintStateEnum.MINT_FAILED.getCode()));
            nftPicService.updateBatchById(nfts);
            log.info(" 请求skin-mint-nft接口--出错:{}", e.getMessage());
        }
        assert response != null;
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        log.info(" 请求skin-mint-nft接口--result:{}", jsonObject);
        if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
            String result = JSON.toJSONString(jsonObject.get("data"));
            List<SysSkinNftMintSuccessDto.SkinNftMintSuccess> mintSuccesses = JSON.parseArray(result, SysSkinNftMintSuccessDto.SkinNftMintSuccess.class);
            //生成metadata
            createMetadata(mintSuccesses, dto.getIds());
        } else {
            nfts.forEach(p -> p.setMintState(SkinNftEnums.SkinMintStateEnum.MINT_FAILED.getCode()));
            nftPicService.updateBatchById(nfts);
            log.info(" 请求skin-mint-nft接口--出错:{}", jsonObject.get("message"));
            throw new GenericException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }

    }

    private void createMetadata(List<SysSkinNftMintSuccessDto.SkinNftMintSuccess> dto, List<Long> ids) {
        ArrayList<NftPublicBackpackDto> backpackEntities = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            SysNftPicEntity entity = nftPicService.getById(ids.get(i));
            entity.setName(dto.get(i).getName());
            entity.setTokenAddress(dto.get(i).getMintAddress());
            entity.setMintTime(System.currentTimeMillis());
            entity.setTokenId(Long.parseLong(dto.get(i).getTokenId()));
            SkinNFTAttrDto attr = nftPicService.handleAttr(entity, null);
            String fileName = dto.get(i).getTokenId() + ".json";
            //boolean flag = CreateJsonFileUtil.createJsonFile(JSONUtil.toJsonStr(attr), TMP_FILE_PATH, fileName);
            boolean flag = CreateJsonFileUtil.createJsonFile("none", TMP_FILE_PATH, fileName);
            // 上传文件
            if (flag) {
                InputStream inputStream = null;
                try {
                    String bucketName = properties.getMetadata().getBucketName();
                    String objectName = "skin/metadata/" + fileName;
                    File file = new File(TMP_FILE_PATH + fileName);
                    inputStream = new FileInputStream(file);
                    template.putObject(bucketName, objectName, inputStream);

                    String jsonUrl = sysFileService.getNftFileUrl(bucketName, objectName);
                    entity.setJsonUrl(jsonUrl);
                    entity.setUpdatedAt(System.currentTimeMillis());
                    nftPicService.updateById(entity);

                    // 组装公共背包需要的数据
                    backpackEntities.add(handleBackpack(dto.get(i), entity, attr, jsonUrl));

                    // 先关闭流，否则 删除文件不成功
                    inputStream.close();
                    // 上传成功后删除临时的JSON文件
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert inputStream != null;
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        // 插入公共背包
        backpackService.insertBackpack(backpackEntities);

        // 通知游戏方，skin mint 成功
        notifyGameSkinMintSuccess(ids);
    }

    public void notifyGameSkinMintSuccess(List<Long> ids) {
        List<SysNftPicEntity> sysNftPicEntities = nftPicService.listByIds(ids);
        List<SkinNftMintSuccessDto> notifyDtos = sysNftPicEntities.stream().map(p -> {
            SkinNftMintSuccessDto notityDto = new SkinNftMintSuccessDto();
            notityDto.setRarity(p.getRarity());
            notityDto.setConfigId(p.getConfId());
            notityDto.setAutoId(p.getAutoId());
            notityDto.setTokenAddress(p.getTokenAddress());
            if (p.getPlatform().equals(PatformEnum.GAME.getCode())) {
                notityDto.setShop(true);
            }
            return notityDto;
        }).collect(Collectors.toList());
        notifyGameService.skinMintSuccess(notifyDtos);
    }

    private NftPublicBackpackDto handleBackpack(SysSkinNftMintSuccessDto.SkinNftMintSuccess skinNft, SysNftPicEntity nftPicEntity, SkinNFTAttrDto attr, String jsonUrl) {
        NftPublicBackpackDto backpackDto = new NftPublicBackpackDto();
        backpackDto.setNftPicId(nftPicEntity.getId());
        backpackDto.setEqNftId(skinNft.getEquipmentId());
        backpackDto.setName(nftPicEntity.getSkin());
        backpackDto.setTokenName(skinNft.getName());
        backpackDto.setOwner(skinNft.getOwner());
        backpackDto.setTokenId(nftPicEntity.getTokenId());
        backpackDto.setMetadata(JSONUtil.toJsonStr(attr.getAttributes()));
        backpackDto.setMetadataUrl(jsonUrl);
        backpackDto.setMetadataShaUrl(seedsAdminApiConfig.getShadowUrl() + nftPicEntity.getTokenId() + ".json");
        backpackDto.setImage(nftPicEntity.getUrl());
        backpackDto.setImageSha(seedsAdminApiConfig.getShadowUrl() + nftPicEntity.getTokenId() + ".png");
        backpackDto.setTokenAddress(skinNft.getMintAddress());
        backpackDto.setCreatedAt(System.currentTimeMillis());
        backpackDto.setUpdatedAt(System.currentTimeMillis());
        backpackDto.setServerRoleId(0L);
        backpackDto.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        backpackDto.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        backpackDto.setDesc(NFTEnumConstant.NFTDescEnum.SEEDS_SKIN.getDesc());
        backpackDto.setType(NFTEnumConstant.NftTypeEnum.HERO.getCode());
        backpackDto.setItemId(nftPicEntity.getConfId());
        backpackDto.setAutoId(nftPicEntity.getAutoId());
        HashMap<String, String> attribute = new HashMap<>();
        attribute.put("rarity", nftPicEntity.getRarity());
        backpackDto.setAttributes(JSONUtil.toJsonStr(attribute));
        backpackDto.setProfession(nftPicEntity.getProfession());
        backpackDto.setRarity(nftPicEntity.getRarity());
        return backpackDto;
    }
}
