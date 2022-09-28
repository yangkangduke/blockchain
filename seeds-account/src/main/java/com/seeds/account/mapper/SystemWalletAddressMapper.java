package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.SystemWalletAddress;

import java.util.List;

/**
 * <p>
 * 系统使用的钱包地址 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
public interface SystemWalletAddressMapper extends BaseMapper<SystemWalletAddress> {

    /**
     * 获取所有
     * @return
     */
    List<SystemWalletAddress> selectAll();
}
