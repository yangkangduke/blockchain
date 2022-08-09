package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysOrgAddOrModifyReq;
import com.seeds.admin.dto.response.SysOrgResp;
import com.seeds.admin.entity.SysOrgEntity;

import java.util.List;

/**
 * @author hewei
 * @createDate 2022/07/22
 */
public interface SysOrgService extends IService<SysOrgEntity> {

    /**
     * 新增组织
     *
     * @param req 组织信息
     */
    void add(SysOrgAddOrModifyReq req);

    /**
     * 编辑组织信息
     *
     * @param req 组织信息
     */
    void modify(SysOrgAddOrModifyReq req);

    /**
     * 逻辑删除
     *
     * @param req id集合
     */
    void delete(ListReq req);

    /**
     * 组织详情
     *
     * @param id 组织id
     * @return 组织信息
     */
    SysOrgResp detail(Long id);

    /**
     * 获取组织树结构
     *
     * @param orgName 组织名称
     * @return 组织树形结构
     */
    List<SysOrgResp> queryRespList(String orgName);
}
