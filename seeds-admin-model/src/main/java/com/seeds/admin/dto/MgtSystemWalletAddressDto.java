package com.seeds.admin.dto;

import com.google.gson.Gson;
import com.seeds.admin.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MgtSystemWalletAddressDto  implements Auditable {

    private long createTime;
    private int type;
    private String address;
    private String tag;
    private String comments;

    private int status;

    private int chain;

    @Override
    public String generateAuditKey() {
        return address;
    }

    @Override
    public String getAuditData() {
        return new Gson().toJson(this);
    }
}
