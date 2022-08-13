package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysNftPropertiesTypeAddReq;
import com.seeds.admin.dto.request.SysNftPropertiesTypePageReq;
import com.seeds.admin.dto.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.response.SysNftPropertiesTypeResp;
import com.seeds.admin.entity.SysNftPropertiesTypeEntity;

import java.util.Collection;
import java.util.Map;


/**
 * NFT属性类别
 *
 * @author hang.yu
 * @date 2022/8/13
 */
public interface SysNftPropertiesTypeService extends IService<SysNftPropertiesTypeEntity> {

    /**
     * 通过code查询NFT属性类别
     * @param  code NFT属性类别code
     * @return  NFT属性类别
     */
    SysNftPropertiesTypeEntity queryByCode(String code);

    /**
     * 添加NFT属性类别
     * @param  req NFT属性类别
     */
    void add(SysNftPropertiesTypeAddReq req);

    /**
     * 修改NFT属性类别
     * @param  req NFT属性类别
     */
    void modify(SysNftTypeModifyReq req);

    /**
     * 批量删除系统NFT属性类别信息
     * @param req NFT属性类别的id列表
     */
    void batchDelete(ListReq req);

    /**
     * 通过id查询NFT属性类别
     * @param  id NFT属性类别id
     * @return  NFT属性类别
     */
    SysNftPropertiesTypeEntity queryById(Long id);

    /**
     * 通过id列表查询NFT属性类别
     * @param  ids NFT属性类别id列表
     * @return  NFT属性类别
     */
    Map<Long, SysNftPropertiesTypeEntity> queryMapByIds(Collection<Long> ids);

    /**
     * 通过id查询NFT属性类别
     * @param  id NFT属性类别id
     * @return  NFT属性类别
     */
    SysNftPropertiesTypeResp detail(Long id);

    /**
     * 分页获取系统NFT属性类别信息
     * @param query 分页查询条件
     * @return 系统NFT属性类别信息
     */
    IPage<SysNftPropertiesTypeResp> queryPage(SysNftPropertiesTypePageReq query);

}
