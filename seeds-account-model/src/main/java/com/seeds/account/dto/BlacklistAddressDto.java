package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 充提币黑名单
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistAddressDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private int type;

    @NotNull
    private Long userId;

    @NotNull
    private String address;

    @NotNull
    private String reason;
    private CommonStatus status;
}
