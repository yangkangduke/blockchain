package com.seeds.uc.dto.request;

import lombok.Data;

/**
 * @author allen
 */
@Data
public class KycReviewReq {
    private long id;
    private int status;
    private String comment;
}
