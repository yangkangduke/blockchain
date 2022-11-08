package com.seeds.admin.dto;
import com.google.gson.Gson;
import com.seeds.account.enums.CommonStatus;
import com.seeds.admin.annotation.MgtNumberValidator;
import com.seeds.admin.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtWithdrawWhitelistDto implements Auditable {

    @NotNull
    private Long userId;
    @NotNull
    private String currency;
    @NotNull
    @MgtNumberValidator
    private BigDecimal maxAmount;
    @NotNull

    @MgtNumberValidator
    private BigDecimal intradayAmount;
    @NotNull
    @MgtNumberValidator
    private BigDecimal autoAmount;
    private String comments;
    //1,启用，2，停用
    private CommonStatus status;


    @Override
    public String generateAuditKey() {
        return userId == null ? null : userId.toString();
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
