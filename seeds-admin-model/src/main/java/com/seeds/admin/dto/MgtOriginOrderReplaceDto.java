package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.account.dto.ChainTxnReplaceDto;
import com.seeds.admin.audit.Auditable;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MgtOriginOrderReplaceDto extends ChainTxnReplaceDto implements Auditable {

    @Override
    public String generateAuditKey() {
        return getTxHash();
    }

    @Override
    public String getAuditData() {
        return (new Gson()).toJson(this);
    }
}
