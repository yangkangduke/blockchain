package com.seeds.admin.audit;

public interface Auditable {
    String generateAuditKey();

    String getAuditData();
}
