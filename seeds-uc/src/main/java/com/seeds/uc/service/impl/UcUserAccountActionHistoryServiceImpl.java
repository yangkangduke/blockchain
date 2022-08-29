package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.UcUserAccountActionHistoryMapper;
import com.seeds.uc.model.UcUserAccountActionHistory;
import com.seeds.uc.service.IUcUserAccountActionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户账号交易历史表 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Service
@Slf4j
@Transactional
public class UcUserAccountActionHistoryServiceImpl extends ServiceImpl<UcUserAccountActionHistoryMapper, UcUserAccountActionHistory> implements IUcUserAccountActionHistoryService {

}
