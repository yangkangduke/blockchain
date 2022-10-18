package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountActionHisDto implements Serializable {
    private static final long serialVersionUID = -1L;

    long createTime;
    long updateTime;
    long userId;
    AccountAction action;
    String source;
    String currency;
    BigDecimal amount;
    CommonActionStatus status;
}
