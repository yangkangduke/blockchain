package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.entity.SysGameEntity;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统游戏
 *
 * @author hang.yu
 * @date 2022/7/21
 */
public interface SysGameService extends IService<SysGameEntity> {

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
    List<SysGameResp> select(Long merchantId);

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
    void modify(SysGameModifyReq req);

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

    /**
     * 通过游戏id列表获取系统游戏名称列表
     * @param ids 游戏id列表
     * @return 系统游戏名称列表
     */
    Map<Long, String> queryMapByIds(Collection<Long> ids);

    /**
     * 通过游戏官方网址列表获取系统游戏名称列表
     * @param officialUrl 官方网址
     * @param name 游戏名称
     * @return 系统游戏
     */
    SysGameEntity queryByOfficialUrlOrName(String officialUrl, String name);

    /**
     * 获取系统游戏下拉列表
     * @return 系统游戏信息
     */
    List<SysGameBriefResp> dropdownList();

    /**
     * uc收藏
     * @param id 游戏的id
     */
    void ucCollection(Long id);

}
