package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto implements Serializable {
    private static final long serialVersionUID = -1L;

    long userId;
    String currency;
    BigDecimal available;
    BigDecimal freeze;
    BigDecimal locked;

    BigDecimal price;
    BigDecimal totalValue;

    @JsonIgnore
    long version;
}
