package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.SysLogPageReq;
import com.seeds.admin.dto.response.SysLogResp;
import com.seeds.admin.entity.SysLogEntity;

/**
 * @author: hewei
 * @date 2022/7/26
 */
public interface SysLogService extends IService<SysLogEntity> {

    IPage<SysLogResp> queryPage(SysLogPageReq req);
}
