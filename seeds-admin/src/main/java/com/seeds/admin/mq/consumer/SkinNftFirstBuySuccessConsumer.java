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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
    public void gameMintNft(String msg) throws IOException {
        log.info("收到skin-nft-first-buy-success消息：{}", msg);
        SkinNftFirstBuySuccessDto dto = JSONUtil.toBean(msg, SkinNftFirstBuySuccessDto.class);
        SysNftPicEntity picEntity = nftPicService.getById(dto.getNftPicId());
        SkinNFTAttrDto attr = nftPicService.handleAttr(picEntity, null);

        String imgName = picEntity.getTokenId() + ".png";
        downloadImage(picEntity.getUrl(), TMP_IMG_FILE_PATH, imgName);

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
     */
    public void downloadImage(String imageUrl, String saveDir, String fileName) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // 检查HTTP响应代码是否为HTTP_OK (200)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpConn.getInputStream();
            // 创建保存文件的目录
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 保存文件的完整路径
            String saveFilePath = saveDir + File.separator + fileName;
            // 创建文件输出流并将输入流中的数据写入输出流中
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            System.out.println("图片已下载到：" + saveFilePath);
        }
        httpConn.disconnect();
    }
}
