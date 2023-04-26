package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysKolAddReq;
import com.seeds.admin.dto.request.SysKolPageReq;
import com.seeds.admin.dto.response.SysKolResp;
import com.seeds.admin.entity.SysKolEntity;

import java.util.List;


/**
 * KOL管理
 * @author hang.yu
 * @date 2023/4/26
 */
public interface SysKolService extends IService<SysKolEntity> {

    /**
     * 分页获取KOL信息
     * @param query 分页查询条件
     * @return KOL信息
     */
    IPage<SysKolResp> queryPage(SysKolPageReq query);


    /**
     * 添加KOL
     * @param req KOL信息
     */
    void add(SysKolAddReq req);

    /**
     * 通过KOL编号获取KOL信息
     * @param id KOL编号
     * @return KOL信息
     */
    SysKolResp detail(Long id);

    /**
     * 批量启用/禁用KOL
     * @param req 状态和id列表
     */
    void enableOrDisable(List<SwitchReq> req);

    /**
     * 邮箱检查
     * @param email 邮箱
     * @return 邀请码
     */
    String check(String email);

}
