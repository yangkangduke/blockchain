package com.seeds.uc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.model.UcUserAccount;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户账户表 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
public interface UcUserAccountMapper extends BaseMapper<UcUserAccount> {

    /**
     * 分页查询账户冲提币历史信息
     *
     * @param page
     * @param historyReq
     * @return
     */
    Page<AccountActionResp> actionHistory(Page page, @Param("historyReq") AccountActionHistoryReq historyReq);
}
