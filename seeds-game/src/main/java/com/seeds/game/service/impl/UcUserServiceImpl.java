package com.seeds.game.service.impl;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.*;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * UC用户 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-30
 */
@Slf4j
@Service
public class UcUserServiceImpl implements UcUserService {

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Override
    public void ownerValidation(String owner) {
        // 归属人权限校验
        Long currentUserId = UserContext.getCurrentUserId();
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(currentUserId);
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (!owner.equals(publicAddress)) {
            throw new GenericException(GameErrorCodeEnum.ERR_507_NO_PERMISSION);
        }
    }

}
