package com.seeds.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysDictTypeAddReq;
import com.seeds.admin.dto.request.SysDictTypeModifyReq;
import com.seeds.admin.dto.response.SysDictTypeResp;
import com.seeds.admin.entity.SysDictTypeEntity;

import java.util.List;
import java.util.Set;


/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public interface SysDictTypeService extends IService<SysDictTypeEntity> {

    /**
     *通过id获取字典数据
     * @param id 字典id列表
     * @return 字典信息
     */
    SysDictTypeEntity queryById(Long id);

    /**
     *添加字典
     * @param req 字典信息
     */
    void add(SysDictTypeAddReq req);

    /**
     * 修改字典
     * @param req 字典信息
     */
    void modify(SysDictTypeModifyReq req);

    /**
     * 删除字典
     * @param req 字典信息
     */
    void delete(ListReq req);

    /**
     *根据id获取字典类别
     * @param id 字典类别id
     * @return 系统字典类别
     */
    SysDictTypeResp detail(Long id);

    /**
     * 根据字典编码获取系统字典类别
     * @param code 字典类别编码
     * @return 系统字典类别
     */
    SysDictTypeEntity queryByTypeCode(String code);

    /**
     * 统计字典子类别
     * @param codes 字典类别code列表
     * @return 子类别数目
     */
    Long countKidsByCodes(Set<String> codes);

    /**
     * 字典类别code列表
     * @param ids 字典类别id列表
     * @return 字典类别code列表
     */
    Set<String> queryCodesByIds(Set<Long> ids);

    /**
     * 获取系统字典类别列表
     * @param name 字典类别名称
     * @return 系统字典类别列表
     */
    List<SysDictTypeResp> queryRespList(String name);
}


