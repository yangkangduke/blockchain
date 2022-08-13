package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysNftTypeAddReq;
import com.seeds.admin.dto.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.response.SysNftTypeResp;
import com.seeds.admin.entity.SysNftTypeEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/7/21
 */
public interface SysNftTypeService extends IService<SysNftTypeEntity> {

    /**
     * 通过NFT类别id列表获取NFT名称
     * @param ids NFT类别id列表
     * @return NFT类别名称
     */
    Map<Long, String> queryNameMapByIds(Collection<Long> ids);

    /**
     * 根据id获取系统NFT类别
     * @param id NFT类别id
     * @return 系统NFT类别
     */
    SysNftTypeEntity queryById(Long id);

    /**
     * 获取系统NFT类别列表
     * @param name NFT类别名称
     * @return 系统NFT类别列表
     */
    List<SysNftTypeResp> queryRespList(String name);

    /**
     * 添加系统NFT类别
     * @param req 系统NFT类别
     */
    void add(SysNftTypeAddReq req);

    /**
     * 根据id获取系统NFT类别
     * @param id NFT类别id
     * @return 系统NFT类别
     */
    SysNftTypeResp detail(Long id);

    /**
     * 修改系统NFT类别
     * @param req 系统NFT类别
     */
    void modify(SysNftTypeModifyReq req);

    /**
     * 根据NFT类别编码获取系统NFT类别
     * @param code NFT类别编码
     * @return 系统NFT类别
     */
    SysNftTypeEntity queryByTypeCode(String code);

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
     * @param codes NFT类别code列表
     * @return 子类别数目
     */
    Long countKidsByCodes(Set<String> codes);


    /**
     * NFT类别code列表
     * @param ids NFT类别id列表
     * @return NFT类别code列表
     */
    Set<String> queryCodesByIds(Set<Long> ids);

    /**
     * 根据父类code获取系统NFT类别列表
     * @param parentCode 父类code
     * @return 系统NFT类别列表
     */
    List<SysNftTypeResp> queryRespByParentCode(String parentCode);

}
