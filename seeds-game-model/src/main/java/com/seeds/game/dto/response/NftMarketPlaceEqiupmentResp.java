package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 市场装备分页查询返回列表
 * @author dengyang
 * @since 2023-03-21
 */
@ApiModel(value = "NftMarketPlaceSkinResp")
@Data
public class NftMarketPlaceEqiupmentResp implements Serializable {

    private static final long serialVersionUID = 1L;
}
