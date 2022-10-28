package com.seeds.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MgtWalletTransferRequestDto {

    private long id;
    @NotNull
    String currency;
    @NotNull
    String fromAddress;
    @NotNull
    String toAddress;
    @NotNull
    String amount;
    @NotNull
    String comments;
    @NotNull
    int chain;
}
