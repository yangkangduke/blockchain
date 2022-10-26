package com.kine.mgt.dto.request;

import com.google.gson.Gson;
import com.kine.account.dto.ChainTxnReplayDto;
import com.kine.mgt.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MgtChainTxnReplayDto extends ChainTxnReplayDto implements Auditable {

    private String comment;

    @Override
    public String generateAuditKey() {
        return getId().toString();
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
