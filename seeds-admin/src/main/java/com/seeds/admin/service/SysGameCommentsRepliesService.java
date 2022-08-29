package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.SysGameCommentsRepliesReq;
import com.seeds.admin.dto.response.SysGameCommentsRepliesResp;
import com.seeds.admin.entity.SysGameCommentsRepliesEntity;

import java.util.List;

/**
 * @author hewei
 * @date 2022-08-26
 */
public interface SysGameCommentsRepliesService extends IService<SysGameCommentsRepliesEntity> {

    Integer reply(SysGameCommentsRepliesReq req);

    List<SysGameCommentsRepliesResp> getReplies(Long commentsId);
}
