package com.seeds.account.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * NFT拍卖信息
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "NFTAuctionResp", description = "NFT拍卖信息")
public class NftAuctionResp {

	@ApiModelProperty(value = "拍卖标记  0：未拍卖 1：正向拍卖 2：反向拍卖 3：正向和反向拍卖")
	private Integer auctionFlag;

}