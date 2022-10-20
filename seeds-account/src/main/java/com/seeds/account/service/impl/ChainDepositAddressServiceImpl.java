package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.ChainDepositAddressMapper;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.service.IChainDepositAddressService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Ethereum地址 充币地址（交易所分配） 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
@Service
public class ChainDepositAddressServiceImpl extends ServiceImpl<ChainDepositAddressMapper, ChainDepositAddress> implements IChainDepositAddressService {

}
