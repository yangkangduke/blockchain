package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.anno.AssignDataSource;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.model.UserAccountActionHis;
import com.seeds.common.enums.DataSources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

//    @AssignDataSource(DataSources.READONLY)
    List<UserAccountActionHis> selectByUserAndTime(@Param("userId") Long userId,
                                                   @Param("startTimestamp") Long startTimestamp,
                                                   @Param("endTimestamp") Long endTimestamp,
                                                   @Param("currency") String currency,
                                                   @Param("action") AccountAction action);


}
