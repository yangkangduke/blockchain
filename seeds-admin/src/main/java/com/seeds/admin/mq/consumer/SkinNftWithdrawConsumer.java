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
import com.seeds.game.dto.request.internal.SkinNftWithdrawDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: hewei
 * @date 2023/5/13
 */

@Component
@Slf4j
public class SkinNftWithdrawConsumer {

    private static final String TMP_FILE_PATH = "fileStorage/skin/json/";

    @Autowired
    private FileTemplate template;

    @Autowired
    private FileProperties properties;

    @Autowired
    private SeedsAdminApiConfig seedsAdminApiConfig;
    /**
     * 消费 skin nft withdraw 消息
     * 重新生成nft的metadata文件，上传到shadow
     *
     * @param msg
     */

    @Autowired
    private SysNftPicService nftPicService;

    @KafkaListener(groupId = "skin-withdraw", topics = {KafkaTopic.SKIN_NFT_WITHDRAW})
    public void gameMintNft(String msg) {
        log.info("收到skin-withdraw消息：{}", msg);
        SkinNftWithdrawDto withdrawDto = JSONUtil.toBean(msg, SkinNftWithdrawDto.class);
        SysNftPicEntity picEntity = nftPicService.getById(withdrawDto.getNftPicId());

        SkinNFTAttrDto attr = nftPicService.handleAttr(picEntity, withdrawDto);

        String fileName = picEntity.getTokenId() + ".json";
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
                // 文件上传shadow
                uploadFileToShadow(file, fileName);
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

    private void uploadFileToShadow(File file, String fileName) {
        String uploadUrl = seedsAdminApiConfig.getEditShadow();
        try {
            HttpRequest.post(uploadUrl)
                    .timeout(30 * 1000)
                    .form("file", file)
                    .execute();
        } catch (Exception e) {
            // 文件上传失败
            log.info("withdraw 更新 Metadata 文件失败：{},error:{}", fileName, e.getMessage());
        }
    }
}
