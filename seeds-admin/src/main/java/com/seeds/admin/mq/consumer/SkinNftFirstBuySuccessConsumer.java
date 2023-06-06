package com.seeds.admin.mq.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.seeds.admin.config.SeedsAdminApiConfig;
import com.seeds.admin.dto.SkinNFTAttrDto;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.utils.CreateJsonFileUtil;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.game.dto.request.internal.SkinNftFirstBuySuccessDto;
import com.seeds.game.dto.request.internal.SkinNftWithdrawDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: hewei
 * @date 2023/5/13
 */

@Component
@Slf4j
public class SkinNftFirstBuySuccessConsumer {

    private static final String TMP_FILE_PATH = "fileStorage/skin/json/";
    private static final String TMP_IMG_FILE_PATH = "fileStorage/skin/img/";

    @Autowired
    private FileTemplate template;

    @Autowired
    private FileProperties properties;

    @Autowired
    private SeedsAdminApiConfig seedsAdminApiConfig;
    /**
     * 消费 skin nft 首次购买成功消息
     * 重新生成nft的metadata文件，上传到shadow
     * 上传NFT的picture到shadow。
     *
     * @param msg
     */

    @Autowired
    private SysNftPicService nftPicService;

    @KafkaListener(groupId = "skin-nft-first-buy-success", topics = {KafkaTopic.SKIN_NFT_BUY_SUCCESS_FIRST})
    public void gameMintNft(String msg) {
        log.info("收到skin-nft-first-buy-success消息：{}", msg);
        SkinNftFirstBuySuccessDto dto = JSONUtil.toBean(msg, SkinNftFirstBuySuccessDto.class);
        SysNftPicEntity picEntity = nftPicService.getById(dto.getNftPicId());
        SkinNFTAttrDto attr = nftPicService.handleAttr(picEntity, null);

        String imgName = picEntity.getTokenId() + ".png";
        this.download(picEntity.getUrl(), imgName, TMP_IMG_FILE_PATH);

        String jsonName = picEntity.getTokenId() + ".json";
        boolean flag = CreateJsonFileUtil.createJsonFile(JSONUtil.toJsonStr(attr), TMP_FILE_PATH, jsonName);
        // 上传文件
        if (flag) {
            InputStream inputStream = null;
            try {
                String bucketName = properties.getMetadata().getBucketName();
                String objectName = "skin/metadata/" + jsonName;
                File jsonFile = new File(TMP_FILE_PATH + jsonName);
                inputStream = new FileInputStream(jsonFile);
                template.putObject(bucketName, objectName, inputStream);

                File img = new File(TMP_IMG_FILE_PATH + imgName);
                // json文件上传shadow
                editShadow(jsonFile, jsonName);
                // 图片文件上传shadow
                uploadFileToShadow(img, imgName);
                // 先关闭流，否则 删除文件不成功
                inputStream.close();
                // 上传成功后删除临时的JSON文件
                jsonFile.delete();
                img.delete();
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

    private void editShadow(File file, String fileName) {
        String uploadUrl = seedsAdminApiConfig.getEditShadow();
        try {
            HttpRequest.post(uploadUrl)
                    .timeout(30 * 1000)
                    .form("file", file)
                    .execute();
        } catch (Exception e) {
            // 文件上传失败
            log.info("皮肤首次购买 更新 Metadata 文件失败：{},error:{}", fileName, e.getMessage());
        }
    }
    private void uploadFileToShadow(File file, String fileName) {
        String uploadUrl = seedsAdminApiConfig.getUploadShadow();
        try {
            HttpRequest.post(uploadUrl)
                    .timeout(30 * 1000)
                    .form("file", file)
                    .execute();
        } catch (Exception e) {
            // 文件上传失败
            log.info("皮肤首次购买，上传图片失败：{},error:{}", fileName, e.getMessage());
        }
    }


    /**
     * 下载文件
     *
     * @param urlStr   文件的URL地址
     * @param fileName 文件名
     * @param savePath 文件保存路径
     */
    public void download(String urlStr, String fileName, String savePath) {
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(savePath + "\\" + fileName);
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
            out.close();
            in.close();
            log.info("文件{}下载成功。", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
