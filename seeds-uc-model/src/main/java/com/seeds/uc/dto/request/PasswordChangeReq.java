package com.seeds.uc.dto.request;

import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/24
 */
@Data
public class PasswordChangeReq {
    private String oldPassword;
    private String newPassword;
}
