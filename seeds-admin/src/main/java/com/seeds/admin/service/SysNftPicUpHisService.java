package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.PageReq;
import com.seeds.admin.dto.request.SysNftPicUpHisReq;
import com.seeds.admin.dto.response.SysNftPicUpHisResp;
import com.seeds.admin.entity.SysNftPicUpHisEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SysNftPicUpHisService extends IService<SysNftPicUpHisEntity> {

    IPage<SysNftPicUpHisResp> queryPage(PageReq req);

    void upload(MultipartFile[] files, SysNftPicUpHisReq req);
}
