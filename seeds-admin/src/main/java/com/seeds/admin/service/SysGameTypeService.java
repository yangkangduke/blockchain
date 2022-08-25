package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysGameTypeResp;
import com.seeds.admin.entity.SysGameTypeEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 系统游戏类别
 *
 * @author hang.yu
 * @date 2022/8/25
 */
public interface SysGameTypeService extends IService<SysGameTypeEntity> {

    /**
     * 通过游戏类别id列表获取游戏名称
     * @param ids 游戏类别id列表
     * @return 游戏类别名称
     */
    Map<Long, SysGameTypeEntity> queryMapByIds(Collection<Long> ids);

    /**
     * 根据id获取系统游戏类别
     * @param id 游戏类别id
     * @return 系统游戏类别
     */
    SysGameTypeEntity queryById(Long id);

    /**
     * 获取系统游戏类别列表
     * @param name 游戏类别名称
     * @return 系统游戏类别列表
     */
    List<SysGameTypeResp> queryRespList(String name);

    /**
     * 添加系统游戏类别
     * @param req 系统游戏类别
     */
    void add(SysGameTypeAddReq req);

    /**
     * 根据id获取系统游戏类别
     * @param id 游戏类别id
     * @return 系统游戏类别
     */
    SysGameTypeResp detail(Long id);

    /**
     * 修改系统游戏类别
     * @param req 系统游戏类别
     */
    void modify(SysGameTypeModifyReq req);

    /**
     * 根据NFT类别编码获取系统游戏类别
     * @param code 游戏类别编码
     * @return 系统游戏类别
     */
    SysGameTypeEntity queryByTypeCode(String code);

    /**
     * 根据id列表批量删除
     * @param req id列表
     */
    void batchDelete(ListReq req);

    /**
     * 批量停用/启用
     * @param req id列表和状态
     */
    void enableOrDisable(List<SwitchReq> req);

    /**
     * 统计子类别
     * @param codes 游戏类别code列表
     * @return 子类别数目
     */
    Long countKidsByCodes(Set<String> codes);

    /**
     * 游戏类别code列表
     * @param ids 游戏类别id列表
     * @return 游戏类别code列表
     */
    Set<String> queryCodesByIds(Set<Long> ids);

    /**
     * 根据父类code获取系统游戏类别列表
     * @param parentCode 父类code
     * @return 系统游戏类别列表
     */
    List<SysGameTypeResp> queryRespByParentCode(String parentCode);


}
