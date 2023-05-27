package com.seeds.game.dto.request;

import com.seeds.common.dto.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author hang.yu
 * @since 2023-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NftMyOfferPageReq extends PageReq {

    private String publicAddress;

}
