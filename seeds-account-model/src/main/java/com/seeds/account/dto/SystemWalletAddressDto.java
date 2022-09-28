package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统使用的钱包
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemWalletAddressDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private long createTime;
    private int type;
    private String address;
    private String tag;
    private String comments;

    private int status;

    private int chain;
}
