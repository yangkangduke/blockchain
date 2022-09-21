package com.seeds.wallet.tron;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TronWalletFile {
    String publicKey;
    String privateKey;
    String address;
}
