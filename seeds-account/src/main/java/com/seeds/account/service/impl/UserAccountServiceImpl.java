package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.mapper.UserAccountMapper;
import com.seeds.account.model.UserAccount;
import com.seeds.account.service.IUserAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钱包账户 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

}
