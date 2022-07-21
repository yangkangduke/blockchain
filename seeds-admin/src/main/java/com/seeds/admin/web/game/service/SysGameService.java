package com.seeds.admin.web.game.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.common.ListReq;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.game.request.SysGameAddReq;
import com.seeds.admin.dto.game.request.SysGamePageReq;
import com.seeds.admin.dto.game.response.SysGameResp;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.entity.game.SysGameEntity;

import java.util.Collection;
import java.util.List;

/**
 * 系统游戏
 *
 * @author hang.yu
 * @date 2022/7/21
 */
public interface SysGameService {

    /**
     * 分页获取系统游戏信息
     * @param query 分页查询条件
     * @return 系统游戏信息
     */
    IPage<SysGameResp> queryPage(SysGamePageReq query);

    /**
     * 获取系统游戏列表
     * @param merchantId 商家id
     * @return 系统游戏列表
     */
    List<SysGameResp> queryList(Long merchantId);

    /**
     * 通过游戏id列表获取系统游戏列表
     * @param ids 游戏id列表
     * @return 系统游戏列表
     */
    List<SysGameEntity> queryByIds(Collection<Long> ids);

    /**
     * 通过游戏id获取系统游戏
     * @param id 游戏id
     * @return 系统游戏
     */
    SysGameEntity queryById(Long id);

    /**
     * 添加游戏
     * @param req 游戏信息
     */
    void add(SysGameAddReq req);

    /**
     * 通过游戏id获取系统游戏信息
     * @param id 游戏id
     * @return 系统游戏信息
     */
    SysGameResp detail(Long id);

    /**
     * 修改游戏
     * @param req 游戏信息
     */
    void modify(SysMerchantModifyReq req);

    /**
     * 批量删除游戏
     * @param req 游戏id列表
     */
    void batchDelete(ListReq req);

    /**
     * 批量上架/下架游戏
     * @param req 状态和id列表
     */
    void enableOrDisable(List<SwitchReq> req);

}
