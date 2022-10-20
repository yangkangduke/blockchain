package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.ChainBlockMapper;
import com.seeds.account.model.ChainBlock;
import com.seeds.account.service.IChainBlockService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Ethereum块跟踪 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@Service
public class ChainBlockServiceImpl extends ServiceImpl<ChainBlockMapper, ChainBlock> implements IChainBlockService {

}
