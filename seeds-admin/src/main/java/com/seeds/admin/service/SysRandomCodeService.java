package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysRandomCodeDetailResp;
import com.seeds.admin.dto.response.SysRandomCodeResp;
import com.seeds.admin.entity.SysRandomCodeEntity;


/**
 * 随机数
 *
 * @author hang.yu
 * @date 2022/11/07
 */
public interface SysRandomCodeService extends IService<SysRandomCodeEntity> {

    /**
     * 分页查询随机码
     * @param query 入参
     * @return 随机码
     */
    IPage<SysRandomCodeResp> queryPage(SysRandomCodePageReq query);

    /**
     * 分页查询随机码明细
     * @param query 入参
     * @return 随机码明细
     */
    IPage<SysRandomCodeDetailResp> detail(SysRandomCodeDetailPageReq query);

    /**
     * 生成随机码
     * @param req 入参
     */
    void generate(SysRandomCodeGenerateReq req);

    /**
     * 通过批次号查询
     * @param batchNo 批次号
     * @return 随机码
     */
    SysRandomCodeEntity queryByBatchNo(String batchNo);

    /**
     * 生成随机码
     * @param batchNo 批次号
     */
    void generateCode(String batchNo);

    /**
     * 编辑随机码
     * @param req 入参
     */
    void modify(SysRandomCodeModifyReq req);

    /**
     * 删除随机码
     * @param batchNo 批次号
     */
    void delete(String batchNo);

    /**
     * 删除随机码明细
     * @param req id列表
     */
    void detailDelete(ListReq req);

    /**
     * 导出随机码明细
     * @param batchNo 批次号
     * @return excel地址
     */
    String export(String batchNo);

    /**
     * 导出随机码明细
     * @param batchNo 批次号
     * @return excel地址
     */
    String exportCode(String batchNo);

    /**
     * 使用随机码
     * @param req 入参
     */
    void useRandomCode(RandomCodeUseReq req);

}
