package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.model.UserAccountActionHis;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * t_user_account_action_his Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface UserAccountActionHisMapper extends BaseMapper<UserAccountActionHis> {

    /**
     * 更新状态
     *
     * @param action
     * @param userId
     * @param source
     * @param status
     * @return
     */
    int updateStatusByActionUserIdSource(@Param("action") AccountAction action, @Param("userId") long userId,
                                         @Param("source") String source, @Param("status") CommonActionStatus status);
}
