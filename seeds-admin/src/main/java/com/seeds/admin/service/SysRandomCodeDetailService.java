package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.SysRandomCodeDetailPageReq;
import com.seeds.admin.dto.response.SysRandomCodeDetailResp;
import com.seeds.admin.entity.SysRandomCodeDetailEntity;

import java.util.List;


/**
 * 随机数明细
 *
 * @author hang.yu
 * @date 2022/11/07
 */
public interface SysRandomCodeDetailService extends IService<SysRandomCodeDetailEntity> {

    /**
     * 分页查询随机码明细
     * @param query 入参
     * @return 随机码明细
     */
    IPage<SysRandomCodeDetailResp> queryPage(SysRandomCodeDetailPageReq query);

    /**
     * 通过批次号查询
     * @param batchNo 批次号
     * @return 随机码明细
     */
    List<SysRandomCodeDetailEntity> queryByBatchNo(String batchNo);

    /**
     * 通过批次号和随机码查询
     * @param batchNo 批次号
     * @param code 随机码
     * @return 随机码明细
     */
    SysRandomCodeDetailEntity queryByBatchNoAndCode(String batchNo, String code);

    /**
     * 删除随机码
     * @param batchNo 批次号
     */
    void removeByBatchNo(String batchNo);

}
