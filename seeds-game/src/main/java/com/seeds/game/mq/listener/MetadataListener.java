package com.seeds.game.mq.listener;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.game.dto.MetadataAttrDto;
import com.seeds.game.dto.request.EquMetadataDto;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.INftPublicBackpackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

/**
 * @author: hewei
 * @date 2023/4/15
 */
@Slf4j
@Component
public class MetadataListener {
    // json文件临时存储位置，上传成功后删除
    private static final String FILEPATH = "fileStorage/json/";
    // 指定文件的content-type，浏览器直接打开JSON文件，而不是下载
    private static final String CONTENTTYPE = "application/json";
    @Autowired
    private FileProperties properties;

    @Autowired
    private FileTemplate template;

    @Autowired
    private INftPublicBackpackService backpackService;

    @KafkaListener(groupId = "nft-mint-success", topics = {KafkaTopic.NFT_MINT_SUCCESS})
    public void uploadJsonFile(String msg) {
        log.info("收到【NFT-MINT-SUCCESS】消息：{}", msg);
        MetadataAttrDto dto = JSONUtil.toBean(msg, MetadataAttrDto.class);
        try {
            EquMetadataDto metadata = this.handleAttr(dto);
            String fileName = dto.getTokenId() + ".json";
            boolean flag = createJsonFile(JSONUtil.toJsonStr(metadata), FILEPATH, fileName);
            // 上传文件
            if (flag) {
                InputStream inputStream = null;
                try {
                    File file = new File(FILEPATH + fileName);
                    inputStream = new FileInputStream(file);
                    String bucketName = properties.getMetadata().getBucketName();
                    String objectName = "metadata/" + fileName;
                    template.putMetadataObject(bucketName, objectName, inputStream, CONTENTTYPE);
                    log.info("上传JSON文件成功：{}",fileName);
                    // 更新背包表
                    updateBackpack(dto, metadata, bucketName, objectName);
                    // 先关闭流，否则 删除文件不成功
                    inputStream.close();
                    // 上传成功后删除临时的JSON文件
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            log.info("消费【NFT-MINT-SUCCESS】消息，错误：{}", e.getMessage());
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    private void updateBackpack(MetadataAttrDto dto, EquMetadataDto metadata, String bucketName, String objectName) {
        String viewUrl = properties.getMetadata().getEndpoint() + "/" + bucketName + "/" + objectName;
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        entity.setMetadata(JSONUtil.toJsonStr(metadata.getAttributes()));
        entity.setMetadataUrl(viewUrl);
        log.info("更新metadata：{}", entity);
        backpackService.update(entity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, dto.getAutoId()));
    }

    // 组装metadata
    public EquMetadataDto handleAttr(MetadataAttrDto attrDto) {
        EquMetadataDto metadata = new EquMetadataDto();
        metadata.setName(attrDto.getName());
        metadata.setImage(attrDto.getImage());

        ArrayList<EquMetadataDto.Attributes> attributesList = new ArrayList<>();

        EquMetadataDto.Attributes configId = new EquMetadataDto.Attributes();
        configId.setTrait_type(NFTEnumConstant.EquMetadataAttrEnum.CONFIGID.getName());
        configId.setValue(attrDto.getConfigId().toString());
        attributesList.add(configId);

        EquMetadataDto.Attributes autoId = new EquMetadataDto.Attributes();
        autoId.setTrait_type(NFTEnumConstant.EquMetadataAttrEnum.AUTOID.getName());
        autoId.setValue(attrDto.getAutoId().toString());
        attributesList.add(autoId);

        EquMetadataDto.Attributes durability = new EquMetadataDto.Attributes();
        durability.setTrait_type(NFTEnumConstant.EquMetadataAttrEnum.DURABILITY.getName());
        durability.setValue(attrDto.getDurability().toString());
        attributesList.add(durability);

        EquMetadataDto.Attributes rareAttribute = new EquMetadataDto.Attributes();
        rareAttribute.setTrait_type(NFTEnumConstant.EquMetadataAttrEnum.RAREATTRIBUTE.getName());
        rareAttribute.setValue(attrDto.getRareAttribute().toString());
        attributesList.add(rareAttribute);

        EquMetadataDto.Attributes quality = new EquMetadataDto.Attributes();
        quality.setTrait_type(NFTEnumConstant.EquMetadataAttrEnum.QUALITY.getName());
        quality.setValue(attrDto.getQuality().toString());
        attributesList.add(quality);

        metadata.setAttributes(attributesList);
        EquMetadataDto.Properties.Files files = new EquMetadataDto.Properties.Files();
        files.setType("image/png");
        files.setUri(attrDto.getImage());
        ArrayList<EquMetadataDto.Properties.Files> fileList = new ArrayList<>();
        fileList.add(files);
        EquMetadataDto.Properties properties = new EquMetadataDto.Properties();
        properties.setFiles(fileList);
        metadata.setProperties(properties);
        return metadata;
    }

    private boolean createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;
        // 拼接文件完整路径
        String fullPath = filePath + fileName;
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            if (jsonString.contains("'")) {
                //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("'", "\\'");
            }
            if (jsonString.contains("\"")) {
                //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("\"", "\\\"");
            }
            if (jsonString.contains("\r\n")) {
                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
            }
            if (jsonString.contains("\n")) {
                //将换行转换一下，因为JSON串中字符串不能出现显式的换行
                jsonString = jsonString.replaceAll("\n", "\\u000a");
            }
            // 格式化json字符串
            jsonString = JSONUtil.formatJsonStr(jsonString);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

}
