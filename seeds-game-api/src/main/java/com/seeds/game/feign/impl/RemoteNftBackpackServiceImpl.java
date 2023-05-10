package com.seeds.game.feign.impl;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.feign.RemoteNftBackpackService;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author hewei
 * @date 2023/04/26
 */
public class RemoteNftBackpackServiceImpl implements RemoteNftBackpackService {

    @Override
    public GenericDto<Object> insertBackpack(List<NftPublicBackpackEntity> backpackEntity) {
        return GenericDto.failure("Internal Error:insert backpack failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
