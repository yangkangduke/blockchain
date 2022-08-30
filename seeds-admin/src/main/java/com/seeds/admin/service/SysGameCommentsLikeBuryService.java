package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.SysGameCommentsLikeOrBuryReq;
import com.seeds.admin.dto.response.SysGameCommentsLikeOrBuryResp;
import com.seeds.admin.entity.SysGameCommentsLikeBuryEntity;

/**
 * @author hewei
 * @date 2022-08-26
 */
public interface SysGameCommentsLikeBuryService extends IService<SysGameCommentsLikeBuryEntity> {

    SysGameCommentsLikeOrBuryResp like(SysGameCommentsLikeOrBuryReq req);

    SysGameCommentsLikeOrBuryResp bury(SysGameCommentsLikeOrBuryReq req);
}
