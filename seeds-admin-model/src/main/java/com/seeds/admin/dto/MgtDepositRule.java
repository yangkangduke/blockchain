package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.admin.annotation.MgtNumberValidator;
import com.seeds.admin.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtDepositRule implements Auditable {

    @NotNull
    private String currency;

    @Min(1)
    @Max(2)
    private Integer status;

    @MgtNumberValidator
    String autoAmount;

    int chain;

    String chainDesc;

    @Override
    public String generateAuditKey() {
        return String.format("%s:%s", currency, chain);
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
