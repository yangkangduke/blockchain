package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysGameCommentsAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameCommentsPageReq;
import com.seeds.admin.dto.response.SysGameCommentsResp;
import com.seeds.admin.entity.SysGameCommentsEntity;

import java.util.List;

/**
 * @author hewei
 * @date 2022-08-08 15:14:11
 */
public interface SysGameCommentsService extends IService<SysGameCommentsEntity> {

    /**
     * 游戏评论分页
     *
     * @param req 分页条件
     * @return 评论信息
     */
    IPage<SysGameCommentsResp> queryPage(SysGameCommentsPageReq req);

    /**
     * 添加评论
     *
     * @param req 评论信息
     */
    void add(SysGameCommentsAddOrModifyReq req);

    /**
     * 获取评论详情
     *
     * @param id 评论id
     * @return 评论信息
     */
    SysGameCommentsResp detail(Long id);

    /**
     * 编辑评论信息
     *
     * @param req 评论信息
     */
    void modify(SysGameCommentsAddOrModifyReq req);

    /**
     * 批量逻辑删除
     *
     * @param req id集合
     */
    void delete(ListReq req);

    /**
     * 批量启用/停用
     *
     * @param req id和状态集合
     */
    void enableOrDisable(List<SwitchReq> req);
}
