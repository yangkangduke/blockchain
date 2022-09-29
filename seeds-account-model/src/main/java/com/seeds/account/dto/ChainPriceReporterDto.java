package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.web3j.abi.datatypes.Utf8String;

import java.util.List;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/9
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChainPriceReporterDto {
    private List<Utf8String> messages;
    private List<Utf8String> signatures;
    private List<Utf8String> symbols;

    public ChainPriceReporterDto(List<Utf8String> messages, List<Utf8String> signatures, List<Utf8String> symbols) {
        this.messages = messages;
        this.signatures = signatures;
        this.symbols = symbols;
    }
}
