package com.seeds.admin.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MgtWithdrawWhitelistDto implements Auditable {

    private Long id;

    /**
     * 用户邮箱
     */
    private String email;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty(value = "1：ETH 3：TRON")
    private  Integer chain;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("最大额度")
    private BigDecimal maxAmount;

    @ApiModelProperty("单日额度")
    private BigDecimal intradayAmount;

    @ApiModelProperty("自动额度，不用走人工审批的额度")
    private BigDecimal autoAmount;

    @ApiModelProperty("评语")
    private String comments;

    @ApiModelProperty("状态，1:启用 2：停用")
    private Integer status;


    @Override
    public String generateAuditKey() {
        return userId == null ? null : userId.toString();
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
