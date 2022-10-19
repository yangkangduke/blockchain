package com.seeds.account.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: hewei
 * @date 2022/10/17
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateEvent {
    private static final long serialVersionUID = -1L;

    long ts;
    long userId;
    int action;
}
