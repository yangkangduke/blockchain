package com.seeds.admin.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysDictDataResp;
import com.seeds.admin.dto.response.SysOrgResp;
import com.seeds.admin.entity.SysDictDataEntity;

import java.util.List;

/**
 * 字典数据
 * @author yang.deng
 * @date 2022/8/15
 */
public interface SysDictDataService extends IService<SysDictDataEntity> {

    /**
     *通过id获取字典数据
     * @param id 字典id列表
     * @return 字典信息
     */
    SysDictDataEntity queryById(Long id);

    /**
     *  添加字典数据
     * @param req 字典信息
     */
    void add(SysDictDataAddReq req);


    /**
     * 修改字典
     * @param req 字典信息
     */
    void modify(SysDictDataModifyReq req);

    /**
     * 根据DictTypeId和字典标签查询字典
     * @param dictTypeId 字典类型id
     * @param dictLabel 字典标签
     * @return 字典信息
     */
    SysDictDataEntity queryByDictTypeIdAndLabel(Long dictTypeId, String dictLabel);

    /**
     *根据 id获取字典
     * @param id 字典id
     * @return 字典信息
     */
    SysDictDataResp detail(Long id);


    /**
     * 批量删除字典
     * @param req 删除字典
     */
    void delete(ListReq req);

    /**
     * 字典分页
     * @param query 分页查询条件
     * @return 字典信息
     */
    IPage<SysDictDataResp> queryPage(SysDictDataPageReq query);

    /**
     * 获取树结构
     *
     * @param dictLabel 字典名称
     * @return 组织树形结构
     */
    List<SysDictDataResp> queryRespList(String dictLabel);
}
