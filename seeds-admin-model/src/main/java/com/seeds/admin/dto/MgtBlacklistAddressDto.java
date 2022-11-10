package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.admin.audit.Auditable;
import lombok.Data;

@Data
public class MgtBlacklistAddressDto extends BlacklistAddressDto implements Auditable {
    @Override
    public String generateAuditKey() {
        return String.format("%d:%s:%d", this.getUserId(), this.getAddress(), this.getType());
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
