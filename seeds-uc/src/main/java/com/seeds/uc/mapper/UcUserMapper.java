package com.seeds.uc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.uc.dto.request.AllUserReq;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.model.UcUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * user table Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-07-14
 */
public interface UcUserMapper extends BaseMapper<UcUser> {

    Page<UcUserResp> getAllUser(Page page, @Param("query") AllUserReq allUserReq);
}
