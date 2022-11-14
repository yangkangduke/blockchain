package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.dto.req.BlackListAddressReq;
import com.seeds.account.dto.req.BlackListAddressSaveOrUpdateReq;
import com.seeds.account.model.BlacklistAddress;
import com.seeds.account.model.SwitchReq;
import com.seeds.common.dto.GenericDto;

import java.util.List;

/**
 * <p>
 * Ethereum黑地址 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IBlacklistAddressService extends IService<BlacklistAddress> {

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<BlacklistAddressDto> loadAll();

    BlacklistAddressDto get(Integer type, Long userId, String address);

    /**
     * 从缓存获取
     * @return
     */
    List<BlacklistAddressDto> getAll();

    /**
     * 添加
     * @param req
     */
    Boolean add(BlackListAddressSaveOrUpdateReq req);

    /**
     * 更新
     * @param req
     */
    Boolean update(BlackListAddressSaveOrUpdateReq req);

    /**
     * 删除(启用/停用)
     * @param req
     */
    Boolean delete(SwitchReq req);
}
