package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.SysNFTAttrJSONDto;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SysNftPicService extends IService<SysNftPicEntity> {

    IPage<SysNftPicResp> queryPage(SysNftPicPageReq req);

    void upload(MultipartFile file, Integer type);

    String getAttr(Long id);
}
