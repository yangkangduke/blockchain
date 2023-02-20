package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.PageReq;
import com.seeds.admin.dto.response.SysNftPicUpHisResp;
import com.seeds.admin.entity.SysNftPicUpHisEntity;

public interface SysNftPicUpHisService extends IService<SysNftPicUpHisEntity> {

    IPage<SysNftPicUpHisResp> queryPage(PageReq req);
}
