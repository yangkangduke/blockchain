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
