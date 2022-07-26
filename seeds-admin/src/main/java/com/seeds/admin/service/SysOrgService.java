package com.seeds.admin.service;

import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysOrgAddOrModifyReq;
import com.seeds.admin.dto.response.SysOrgResp;
import com.seeds.admin.entity.SysOrgEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author hewei
 * @createDate 2022/07/22
 */
public interface SysOrgService extends IService<SysOrgEntity> {

    void add(SysOrgAddOrModifyReq req);

    void modify(SysOrgAddOrModifyReq req);

    void delete(ListReq req);

    SysOrgResp detail(Long id);

    List<SysOrgResp> queryRespList(String orgName);
}
