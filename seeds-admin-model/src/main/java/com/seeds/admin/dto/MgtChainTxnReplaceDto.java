package com.kine.mgt.dto.request;

import com.google.gson.Gson;
import com.kine.account.dto.ChainTxnReplaceDto;
import com.kine.mgt.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
