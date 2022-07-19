package com.seeds.admin.web.merchant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.merchant.request.SysMerchantAddReq;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.dto.merchant.request.SysMerchantPageReq;
import com.seeds.admin.dto.merchant.response.SysMerchantResp;

import java.util.List;

/**
 * 系统商家
 *
 * @author hang.yu
 * @date 2022/7/19
 */
public interface SysMerchantService {

    /**
     * 获取系统商家信息
     * @return 系统商家信息
     */
    List<SysMerchantResp> queryList();

    /**
     * 分页获取系统商家信息
     * @param query 分页查询条件
     * @return 系统商家信息
     */
    IPage<SysMerchantResp> queryPage(SysMerchantPageReq query);

    /**
     * 添加系统商家信息
     * @param req 商家信息
     */
    void add(SysMerchantAddReq req);

    /**
     * 获取系统商家信息
     * @param id 商家id
     * @return 系统商家信息
     */
    SysMerchantResp detail(Long id);

    /**
     * 修改系统商家信息
     * @param req 系统商家信息
     */
    void modify(SysMerchantModifyReq req);

    /**
     * 删除系统商家信息
     * @param id 商家id
     */
    void delete(Long id);

}
