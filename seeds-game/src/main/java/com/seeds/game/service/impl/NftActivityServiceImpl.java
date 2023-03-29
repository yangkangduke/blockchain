package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftActivity;
import com.seeds.game.mapper.NftActivityMapper;
import com.seeds.game.service.INftActivityService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * nft活动记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Service
public class NftActivityServiceImpl extends ServiceImpl<NftActivityMapper, NftActivity> implements INftActivityService {

}
