package com.seeds.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysFileResp;
import com.seeds.admin.dto.response.SysRandomCodeDetailResp;
import com.seeds.admin.dto.response.SysRandomCodeExportResp;
import com.seeds.admin.dto.response.SysRandomCodeResp;
import com.seeds.admin.entity.SysRandomCodeDetailEntity;
import com.seeds.admin.entity.SysRandomCodeEntity;
import com.seeds.admin.enums.RandomCodeStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysRandomCodeMapper;
import com.seeds.admin.mq.producer.KafkaProducer;
import com.seeds.admin.service.SysFileService;
import com.seeds.admin.service.SysRandomCodeDetailService;
import com.seeds.admin.service.SysRandomCodeService;
import com.seeds.admin.service.SysSequenceNoService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.RandomCodeType;
import com.seeds.common.exception.SeedsException;
import lombok.Cleanup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 随机码
 *
 * @author hang.yu
 * @date 2022/10/10
 */
@Service
public class SysRandomCodeServiceImpl extends ServiceImpl<SysRandomCodeMapper, SysRandomCodeEntity> implements SysRandomCodeService {

    private final static String SOURCE_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Value("${admin.random.code.generate.number:1000}")
    private Long generateNumber;

    @Value("${admin.random.code.export.number:10000}")
    private Long exportNumber;

    @Resource
    private KafkaProducer kafkaProducer;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysSequenceNoService sysSequenceNoService;

    @Autowired
    private SysRandomCodeDetailService sysRandomCodeDetailService;

    @Override
    public IPage<SysRandomCodeResp> queryPage(SysRandomCodePageReq query) {
        LambdaQueryWrapper<SysRandomCodeEntity> queryWrap = new QueryWrapper<SysRandomCodeEntity>().lambda()
                .eq(query.getStatus() != null, SysRandomCodeEntity::getStatus, query.getStatus())
                .eq(query.getType() != null, SysRandomCodeEntity::getType, query.getType())
                .eq(query.getLength() != null, SysRandomCodeEntity::getLength, query.getLength())
                .eq(query.getNumber() != null, SysRandomCodeEntity::getNumber, query.getNumber())
                .orderByDesc(SysRandomCodeEntity::getId);
        Page<SysRandomCodeEntity> page = page(new Page<>(query.getCurrent(), query.getSize()), queryWrap);
        List<SysRandomCodeEntity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysRandomCodeResp resp = new SysRandomCodeResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public IPage<SysRandomCodeDetailResp> detail(SysRandomCodeDetailPageReq query) {
        return sysRandomCodeDetailService.queryPage(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generate(SysRandomCodeGenerateReq req) {
        String batchNo = sysSequenceNoService.generateRandomCodeNo();
        SysRandomCodeEntity randomCode = new SysRandomCodeEntity();
        randomCode.setBatchNo(batchNo);
        BeanUtils.copyProperties(req, randomCode);
        randomCode.setStatus(RandomCodeStatusEnum.GENERATING.getCode());
        // 处理数量小于阈值直接同步处理
        if (req.getNumber() < generateNumber) {
            Set<String> randoms = getRandomStringArray(req.getLength(), req.getNumber());
            randoms.forEach(p -> {
                SysRandomCodeDetailEntity randomCodeDetail = new SysRandomCodeDetailEntity();
                randomCodeDetail.setBatchNo(batchNo);
                randomCodeDetail.setCode(p);
                sysRandomCodeDetailService.save(randomCodeDetail);
            });
            randomCode.setStatus(RandomCodeStatusEnum.NORMAL.getCode());
            save(randomCode);
        } else {
            save(randomCode);
            // 发随机码生成消息
            kafkaProducer.send(KafkaTopic.RANDOM_CODE_GENERATE, randomCode.getBatchNo());
        }
    }

    @Override
    public SysRandomCodeEntity queryByBatchNo(String batchNo) {
        LambdaQueryWrapper<SysRandomCodeEntity> query = new QueryWrapper<SysRandomCodeEntity>().lambda()
                .eq(SysRandomCodeEntity::getBatchNo, batchNo);
        return getOne(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCode(String batchNo) {
        SysRandomCodeEntity randomCode = queryByBatchNo(batchNo);
        Set<String> randoms = getRandomStringArray(randomCode.getLength(), randomCode.getNumber());
        randoms.forEach(p -> {
            SysRandomCodeDetailEntity randomCodeDetail = new SysRandomCodeDetailEntity();
            randomCodeDetail.setBatchNo(randomCode.getBatchNo());
            randomCodeDetail.setCode(p);
            sysRandomCodeDetailService.save(randomCodeDetail);
        });
        randomCode.setStatus(RandomCodeStatusEnum.NORMAL.getCode());
        updateById(randomCode);
    }

    @Override
    public void modify(SysRandomCodeModifyReq req) {
        SysRandomCodeEntity randomCode = queryByBatchNo(req.getBatchNo());
        if (randomCode == null) {
            return;
        }
        randomCode.setDesc(req.getDesc());
        randomCode.setExpireTime(req.getExpireTime());
        updateById(randomCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String batchNo) {
        SysRandomCodeEntity randomCode = queryByBatchNo(batchNo);
        if (randomCode == null) {
            return;
        }
        removeById(randomCode);
        sysRandomCodeDetailService.removeByBatchNo(batchNo);
    }

    @Override
    public void detailDelete(ListReq req) {
        sysRandomCodeDetailService.removeBatchByIds(req.getIds());
    }

    @Override
    public String export(String batchNo) {
        SysRandomCodeEntity randomCode = queryByBatchNo(batchNo);
        if (randomCode == null) {
            throw new SeedsException("Record does not exist");
        }
        if (StringUtils.isNotBlank(randomCode.getExcelUrl())) {
            return randomCode.getExcelUrl();
        }
        // 处理数量小于阈值直接同步处理
        if (randomCode.getNumber() < exportNumber) {
            try {
                return exportCode(batchNo);
            } catch (IOException e) {
                throw new SeedsException("Export excel failure");
            }
        } else {
            randomCode.setStatus(RandomCodeStatusEnum.EXPORTING.getCode());
            updateById(randomCode);
            // 发随机码生成消息
            kafkaProducer.send(KafkaTopic.RANDOM_CODE_GENERATE, randomCode.getBatchNo());
        }
        return null;
    }

    @Override
    public String exportCode(String batchNo) throws IOException {
        List<SysRandomCodeExportResp> respList = new ArrayList<>();
        SysRandomCodeEntity randomCode = queryByBatchNo(batchNo);
        List<SysRandomCodeDetailEntity> randomCodeDetails = sysRandomCodeDetailService.queryByBatchNo(batchNo);
        if (CollectionUtils.isEmpty(randomCodeDetails)) {
            return null;
        }
        randomCodeDetails.forEach(p -> {
            SysRandomCodeExportResp resp = new SysRandomCodeExportResp();
            resp.setCode(p.getCode());
            resp.setUseFlag(WhetherEnum.YES.value() == p.getUseFlag() ? "已使用" : "未使用");
            resp.setDesc(randomCode.getDesc());
            resp.setCreatedAt(DateUtil.formatDateTime(new Date(p.getCreatedAt())));
            resp.setExpireTime(DateUtil.formatDateTime(new Date(randomCode.getExpireTime())));
            respList.add(resp);
        });
        @Cleanup ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(os, SysRandomCodeExportResp.class).build();
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName("sheet1");
        excelWriter.write(respList, writeSheet);
        excelWriter.finish();
        byte[] content = os.toByteArray();
        @Cleanup InputStream is = new ByteArrayInputStream(content);
        SysFileResp resp = sysFileService.upload(is, batchNo + ".xlsx", RandomCodeType.from(randomCode.getType()).getDescEn(), null);
        randomCode.setExcelUrl(resp.getUrl());
        updateById(randomCode);
        return resp.getUrl();
    }

    @Override
    public void useRandomCode(RandomCodeUseReq req) {
        LambdaQueryWrapper<SysRandomCodeEntity> query = new QueryWrapper<SysRandomCodeEntity>().lambda()
                .eq(SysRandomCodeEntity::getType, req.getType())
                .gt(SysRandomCodeEntity::getExpireTime, System.currentTimeMillis())
                .orderByDesc(SysRandomCodeEntity::getId);
        List<SysRandomCodeEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            throw new SeedsException("invitation not exist");
        }
        SysRandomCodeEntity randomCode = list.get(0);
        SysRandomCodeDetailEntity randomCodeDetail = sysRandomCodeDetailService.queryByBatchNoAndCode(randomCode.getBatchNo(), req.getCode());
        if (randomCodeDetail == null) {
            throw new SeedsException("invitation code not exist");
        }
        randomCodeDetail.setUseFlag(WhetherEnum.YES.value());
        sysRandomCodeDetailService.updateById(randomCodeDetail);
    }

    public static Set<String> getRandomStringArray(int length, int size) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        HashSet<String> set = new HashSet<>();
        // 生成随机字符串到set里面
        while (set.size() < size){
            sb.setLength(0);
            for (int i = 0; i < length; i++) {
                sb.append(SOURCE_STR.charAt(random.nextInt(62)));
            }
            set.add(sb.toString());
        }
        return set;
    }
}

