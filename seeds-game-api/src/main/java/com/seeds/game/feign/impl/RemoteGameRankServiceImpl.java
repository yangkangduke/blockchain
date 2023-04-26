package com.seeds.game.feign.impl;

import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.feign.RemoteGameRankService;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author hang.yu
 * @date 2023/04/26
 */
public class RemoteGameRankServiceImpl implements RemoteGameRankService {

    @Override
    public GenericDto<List<GameWinRankResp.GameWinRank>> winInfo(GameWinRankReq query) {
        return GenericDto.failure("Internal Error:game winInfo failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
