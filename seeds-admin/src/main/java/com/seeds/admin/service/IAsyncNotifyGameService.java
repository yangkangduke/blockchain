package com.seeds.admin.service;


import com.seeds.admin.dto.game.SkinNftMintSuccessDto;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/4/16
 */
public interface IAsyncNotifyGameService {

    void skinMintSuccess(List<SkinNftMintSuccessDto> dtos);
}
