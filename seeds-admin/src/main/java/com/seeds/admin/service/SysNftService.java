package com.seeds.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.response.SysNftResp;

/**
 * 系统NFT
 *
 * @author hang.yu
 * @date 2022/7/22
 */
public interface SysNftService {

    /**
     * 分页获取系统NFT信息
     * @param query 分页查询条件
     * @return 系统NFT信息
     */
    IPage<SysNftResp> queryPage(SysNftPageReq query);

}
