package com.seeds.game.feign.impl;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.entity.NftEquipment;
import com.seeds.game.feign.RemoteNftEquipService;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2023/04/26
 */
public class RemoteNftEquipServiceImpl implements RemoteNftEquipService {

    @Override
    public GenericDto<Map<String, NftEquipment>> getOwnerByMintAddress(List<String> mintAddresses) {
        return GenericDto.failure("Internal Error:getOwner failed", HttpStatus.INTERNAL_SERVER_ERROR.value());

    }
}
