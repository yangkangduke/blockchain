package com.seeds.admin.web.game.service;


import com.seeds.admin.dto.game.request.SysNftTypeAddReq;
import com.seeds.admin.dto.game.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.game.response.SysNftTypeResp;
import com.seeds.admin.entity.game.SysNftTypeEntity;

import java.util.List;

/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/7/21
 */
public interface SysNftTypeService {

    /**
     * 获取系统NFT类别列表
     * @return 系统NFT类别列表
     */
    List<SysNftTypeEntity> queryList();

    /**
     * 根据id获取系统NFT类别
     * @param id NFT类别id
     * @return 系统NFT类别
     */
    SysNftTypeEntity queryById(Long id);

    /**
     * 获取系统NFT类别列表
     * @return 系统NFT类别列表
     */
    List<SysNftTypeResp> queryRespList();

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

}
