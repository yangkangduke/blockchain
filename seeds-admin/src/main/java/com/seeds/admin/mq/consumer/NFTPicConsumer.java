package com.seeds.admin.mq.consumer;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.service.SysFileService;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * 消费者
 *
 * @author hewei
 */
@Component
@Slf4j
public class NFTPicConsumer {

    @Autowired
    private FileTemplate template;

    @Autowired
    private FileProperties properties;
    @Resource
    private SysNftPicService nftPicService;

    @Autowired
    private SysFileService sysFileService;

    /**
     * 消费NFT图片属性更新消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.NFT_PIC_ATTR_UPDATE_SUCCESS})
    public void gameMintNft(String msg) throws IOException {
        log.info("收到消息：{}", msg);
        JSONArray objects = JSONUtil.parseArray(msg);
        List<Long> ids = objects.toList(Long.class);
        for (Long id : ids) {
            SysNftPicEntity entity = nftPicService.getById(id);
            String attr = nftPicService.getAttr(entity.getId());
            String fileName = entity.getPicName().substring(0, entity.getPicName().indexOf(".")) + ".json";
            boolean flag = createJsonFile(attr, "fileStorage/json/", fileName);
            // 上传文件
            if (flag) {
                InputStream inputStream = null;
                try {
                    String bucketName = properties.getBucketName();
                    String objectName = "NFT_JSON/" + fileName;
                    File file = new File("fileStorage/json/" + fileName);
                    inputStream = new FileInputStream(file);
                    template.putObject(bucketName, objectName, inputStream);

                    String jsonUrl = sysFileService.getFileUrl(objectName);
                    entity.setJsonUrl(jsonUrl);
                    nftPicService.updateById(entity);

                    // 先关闭流，否则 删除文件不成功
                    inputStream.close();
                    // 上传成功后删除临时的JSON文件
                    file.delete();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();

                }
            }
        }

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

            if (jsonString.indexOf("'") != -1) {
                //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("'", "\\'");
            }
            if (jsonString.indexOf("\"") != -1) {
                //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("\"", "\\\"");
            }

            if (jsonString.indexOf("\r\n") != -1) {
                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
            }
            if (jsonString.indexOf("\n") != -1) {
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
