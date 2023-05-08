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
import com.seeds.admin.enums.SkinNftEnums;
import com.seeds.admin.service.SysFileService;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.service.SysNftSkinAsyncService;
import com.seeds.admin.utils.CreateJsonFileUtil;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
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

    @Override
    @Async
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
                    .timeout(60 * 1000 * 2)
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
        }

    }

    private void createMetadata(List<SysSkinNftMintSuccessDto.SkinNftMintSuccess> dto, List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {
            SysNftPicEntity entity = nftPicService.getById(ids.get(i));
            entity.setName(dto.get(i).getName());
            entity.setTokenAddress(dto.get(i).getMintAddress());
            SkinNFTAttrDto attr = nftPicService.handleAttr(entity);
            String tokenId = dto.get(i).getName().substring(dto.get(i).getName().lastIndexOf("#") + 1);
            String fileName = tokenId + ".json";
            boolean flag = CreateJsonFileUtil.createJsonFile(JSONUtil.toJsonStr(attr), TMP_FILE_PATH, fileName);
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

        // 通知游戏方，skin mint 成功
        List<SysNftPicEntity> sysNftPicEntities = nftPicService.listByIds(ids);
        List<SkinNftMintSuccessDto> notifyDtos = sysNftPicEntities.stream().map(p -> {
            SkinNftMintSuccessDto notityDto = new SkinNftMintSuccessDto();
            notityDto.setRarity(p.getRarity());
            notityDto.setConfigId(p.getConfId());
            notityDto.setAutoId(p.getAutoId());
            notityDto.setTokenAddress(p.getTokenAddress());
            return notityDto;
        }).collect(Collectors.toList());
        notifyGameService.skinMintSuccess(notifyDtos);
    }
}
