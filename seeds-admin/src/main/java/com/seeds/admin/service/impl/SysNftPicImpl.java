package com.seeds.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
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
import com.seeds.admin.dto.SkinNftPushAutoIdReq;
import com.seeds.admin.dto.SysNFTAttrDto;
import com.seeds.admin.dto.SysNFTAttrJSONDto;
import com.seeds.admin.dto.game.GameApplyAutoIdsDto;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysApplyAutoIdsReq;
import com.seeds.admin.dto.request.SysNftPicAttributeModifyReq;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.AutoIdApplyStateEnum;
import com.seeds.admin.enums.NftAttrEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysNftPicMapper;
import com.seeds.admin.mq.producer.KafkaProducer;
import com.seeds.admin.service.SysGameApiService;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.utils.CsvUtils;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.ApiType;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
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
    public Integer upload(MultipartFile file) {
        List<SysNftPicEntity> batchUpdate = new ArrayList<>();

        // 解析CSV文件
        List<SysNFTAttrDto> sysNFTAttrDtos = CsvUtils.getCsvData(file, SysNFTAttrDto.class);
        if (!CollectionUtils.isEmpty(sysNFTAttrDtos)) {
            sysNFTAttrDtos.forEach(p -> {
                if (Objects.nonNull(p.getSymbol()) && p.getSymbol().length() > 10) {
                    throw new GenericException(" 图片 [" + p.getPictureName() + "]，属性更新失败：" + AdminErrorCodeEnum.ERR_40022_SYMBOL_TOO_LONG.getDescEn());
                }
                SysNftPicEntity one = this.getOne(new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getPicName, p.getPictureName()));
                if (!Objects.isNull(one) && one.getPicName().equals(p.getPictureName())) {
                    BeanUtils.copyProperties(p, one);
                    one.setDescription(p.getDesc());
                    one.setUpdatedAt(System.currentTimeMillis());
                    batchUpdate.add(one);
                }
            });
        }

        if (!CollectionUtils.isEmpty(batchUpdate)) {
            // 批量更新属性
            this.updateBatchById(batchUpdate);
            // 屬性更新成功消息
            kafkaProducer.send(KafkaTopic.NFT_PIC_ATTR_UPDATE_SUCCESS, JSONUtil.toJsonStr(batchUpdate.stream().map(p -> p.getId()).collect(Collectors.toList())));
        }
        return batchUpdate.size();
    }

    @Override
    public String getAttr(Long id) {

        SysNftPicEntity entity = this.getById(id);
        SysNFTAttrJSONDto sysNFTAttrJSONDto = this.handelAttr(entity);
        return JSONUtil.toJsonStr(sysNFTAttrJSONDto);
    }

    public SysNFTAttrJSONDto handelAttr(SysNftPicEntity entity) {
        SysNFTAttrJSONDto jsonDto = new SysNFTAttrJSONDto();
        jsonDto.setName(entity.getName());
        jsonDto.setSymbol(entity.getSymbol());
        jsonDto.setDescription(entity.getDescription());
        jsonDto.setImage(entity.getPicName());

        ArrayList<SysNFTAttrJSONDto.Attributes> attributesList = new ArrayList<>();
        if (!StrUtil.isEmpty(entity.getRarity())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.RARITY.getName());
            attributes.setValue(entity.getRarity());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getFeature())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.FEATURE.getName());
            attributes.setValue(entity.getFeature());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getColor())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.COLOR.getName());
            attributes.setValue(entity.getColor());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getAccessories())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.ACCESSORIES.getName());
            attributes.setValue(entity.getAccessories());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getDecorate())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.DECORATE.getName());
            attributes.setValue(entity.getDecorate());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getOther())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.OTHER.getName());
            attributes.setValue(entity.getOther());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getHero())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.HERO.getName());
            attributes.setValue(entity.getHero());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getSkin())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTrait_type(NftAttrEnum.SKIN.getName());
            attributes.setValue(entity.getSkin());
            attributesList.add(attributes);
        }
        jsonDto.setAttributes(attributesList);
        SysNFTAttrJSONDto.Properties.Files files = new SysNFTAttrJSONDto.Properties.Files();
        files.setType("image/" + entity.getPicName().substring(entity.getPicName().lastIndexOf(".") + 1));
        files.setUri(entity.getPicName());
        ArrayList<SysNFTAttrJSONDto.Properties.Files> fileList = new ArrayList<>();
        fileList.add(files);
        SysNFTAttrJSONDto.Properties properties = new SysNFTAttrJSONDto.Properties();
        properties.setFiles(fileList);
        jsonDto.setProperties(properties);
        return jsonDto;
    }

    @Override
    public void updateAttribute(SysNftPicAttributeModifyReq req) {
        log.info("NftAttributeModifyReq == {}",req);
        SysNftPicEntity sysNftPicEntity = new SysNftPicEntity();
        BeanUtils.copyProperties(req, sysNftPicEntity);
        sysNftPicEntity.setDescription(req.getDesc());
        this.updateById(sysNftPicEntity);

        // 屬性更新成功消息
        kafkaProducer.send(KafkaTopic.NFT_PIC_ATTR_UPDATE_SUCCESS, JSONUtil.toJsonStr(CollectionUtil.newArrayList(req.getId())));
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
    public void applyAutoIds(SysApplyAutoIdsReq ids) {
        Set<Long> noApplyIds = this.list(new LambdaQueryWrapper<SysNftPicEntity>()
                .in(SysNftPicEntity::getConfId, ids.getConfigIds())
                .eq(SysNftPicEntity::getApplyState, AutoIdApplyStateEnum.NO_APPLY.getCode()))
                .stream()
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
                    entity.setApplyState(AutoIdApplyStateEnum.APPLYING.getCode());
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
    public void pushAutoId(SkinNftPushAutoIdReq req) {
        Set<Long> configIds = req.getAutoIds().keySet();
        List<Long> validConfigIds = this.list(new LambdaQueryWrapper<SysNftPicEntity>()
                .in(SysNftPicEntity::getConfId, configIds)
                .eq(SysNftPicEntity::getAutoId, WhetherEnum.NO.value()))
                .stream()
                .map(p -> p.getConfId())
                .collect(Collectors.toList());

        List<SysNftPicEntity> updateList = req.getAutoIds().keySet().stream().filter(i -> validConfigIds.contains(i)).map(p -> {
            SysNftPicEntity entity = new SysNftPicEntity();
            entity.setConfId(p);
            entity.setAutoId(req.getAutoIds().get(p));
            entity.setApplyState(AutoIdApplyStateEnum.APPLIED.getCode());
            return entity;
        }).collect(Collectors.toList());
        this.updateBatchByQueryWrapper(updateList, item ->
                new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getConfId, item.getConfId()));
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
