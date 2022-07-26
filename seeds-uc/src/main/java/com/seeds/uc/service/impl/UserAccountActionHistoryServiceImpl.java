package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.UserAccountActionHistoryMapper;
import com.seeds.uc.model.UserAccountActionHistory;
import com.seeds.uc.service.IUserAccountActionHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * t_user_account_action_history 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Service
public class UserAccountActionHistoryServiceImpl extends ServiceImpl<UserAccountActionHistoryMapper, UserAccountActionHistory> implements IUserAccountActionHistoryService {

}
