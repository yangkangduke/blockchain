package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.account.dto.ChainTxnReplaceDto;
import com.seeds.admin.audit.Auditable;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MgtChainTxnReplaceDto extends ChainTxnReplaceDto implements Auditable {

    private Long id;

    @Override
    public String generateAuditKey() {
        return id == null ? null : id.toString();
    }

    @Override
    public String getAuditData() {
        return (new Gson()).toJson(this);
    }
}
