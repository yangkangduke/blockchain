package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.account.dto.ActionControlDto;
import com.seeds.admin.audit.Auditable;
import lombok.Data;

@Data
public class MgtActionControlDto extends ActionControlDto implements Auditable {
    @Override
    public String generateAuditKey() {
        return String.format("%s:%s", getType(), getKey());
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
