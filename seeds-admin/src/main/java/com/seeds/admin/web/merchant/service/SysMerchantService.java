package com.seeds.admin.web.merchant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.common.ListReq;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.merchant.request.SysMerchantAddReq;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.dto.merchant.request.SysMerchantPageReq;
import com.seeds.admin.dto.merchant.request.SysMerchantUserAddReq;
import com.seeds.admin.dto.merchant.response.SysMerchantResp;
import com.seeds.admin.entity.merchant.SysMerchantEntity;

import java.util.Collection;
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
     * 通过id列表获取系统商家信息
     * @param ids id列表
     * @return 系统商家信息
     */
    List<SysMerchantEntity> queryByIds(Collection<Long> ids);

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

    /**
     * 批量启用/停用商家
     * @param req 状态和id列表
     */
    void enableOrDisable(List<SwitchReq> req);

    /**
     * 批量删除商家用户
     * @param req 商家用户id列表
     * @param merchantId 商家id
     */
    void deleteUser(ListReq req, Long merchantId);

    /**
     * 批量删除商家游戏
     * @param req 商家游戏id列表
     * @param merchantId 商家id
     */
    void deleteGame(ListReq req, Long merchantId);

    /**
     * 批量添加商家游戏
     * @param req 商家游戏id列表
     * @param merchantId 商家id
     */
    void addGame(ListReq req, Long merchantId);

    /**
     * 添加商家用户
     * @param req 商家用户信息
     * @param merchantId 商家id
     */
    void addUser(SysMerchantUserAddReq req, Long merchantId);

}
