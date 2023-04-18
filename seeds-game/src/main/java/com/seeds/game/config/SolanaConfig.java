package com.seeds.game.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class SolanaConfig {

    @Value("${solana.equipment.contract.address:DUzYwZxVpBmyKjhBWJEDHWV88v88C72iNsZskEMx6ySb}")
    private String equipmentContractAddress;

    @Value("${solana.skin.contract.address:A5pEUGqsUUmeioTAQxCiNJKotwDQD7GZPehXUykUxoiA}")
    private String skinContractAddress;

    @Value("${solana.token.standard:Metaplex}")
    private String tokenStandard;

    @Value("${solana.chain:Solana}")
    private String chain;

    @Value("${solana.creator.earnings:5%}")
    private String creatorEarnings;

    @Value("${solana.custodian.fee:1%/24h}")
    private String custodianFee;

}
