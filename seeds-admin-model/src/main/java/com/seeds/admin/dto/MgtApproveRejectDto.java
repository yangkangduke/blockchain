package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.admin.audit.Auditable;
import lombok.Data;

@Data
public class MgtApproveRejectDto extends ChainDepositWithdrawHisDto implements Auditable {

    private String comment;

    @Override
    public String generateAuditKey() {
        return String.valueOf(getId());
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
