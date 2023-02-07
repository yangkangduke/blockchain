package com.seeds.uc.dto.request;

import lombok.Data;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class PhantomBindReq {

    private String authToken;
    private String emailCode;

}
