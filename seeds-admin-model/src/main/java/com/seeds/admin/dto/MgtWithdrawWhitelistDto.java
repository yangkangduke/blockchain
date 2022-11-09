package com.seeds.admin.dto;
import com.google.gson.Gson;
import com.seeds.account.enums.CommonStatus;
import com.seeds.admin.annotation.MgtNumberValidator;
import com.seeds.admin.audit.Auditable;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("用户id")
    private Long userId;
    @NotNull
    @ApiModelProperty("币种")
    private String currency;
    @NotNull
    @MgtNumberValidator
    @ApiModelProperty("最大额度")
    private BigDecimal maxAmount;
    @NotNull

    @MgtNumberValidator
    @ApiModelProperty("单日额度")
    private BigDecimal intradayAmount;
    @NotNull
    @MgtNumberValidator
    @ApiModelProperty("自动额度，不用走人工审批的额度")
    private BigDecimal autoAmount;
    @ApiModelProperty("评语")
    private String comments;
    //1,启用，2，停用
    @ApiModelProperty("状态，1:启用 2：停用")
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
