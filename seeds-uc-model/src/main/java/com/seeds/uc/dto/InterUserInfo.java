package com.seeds.uc.dto;

import com.seeds.common.enums.Language;
import lombok.*;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class InterUserInfo extends UserDto{
    private Language language;
}
