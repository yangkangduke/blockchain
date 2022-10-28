package com.seeds.admin.mapstruct;


import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.AddressCollectOrderHisDto;
import com.seeds.admin.dto.MgtAddressCollectOrderHisDto;
import com.seeds.admin.dto.MgtWalletTransferDto;
import com.seeds.common.enums.Chain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetManagementMapper extends BaseMapstructMapper {

    @Mappings({
            @Mapping(source = "amount", target = "amount", qualifiedByName = "convertBigDecimalToString"),
            @Mapping(source = "dto", target = "feeAmount", qualifiedByName = "convertBigDecimalToStringTransfer"),
    })
    List<MgtAddressCollectOrderHisDto> convertToMgtAddressCollectOrderHisDtos(List<AddressCollectOrderHisDto> dtos);


    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.createTime", target = "createTime"),
            @Mapping(source = "dto.updateTime", target = "updateTime"),
            @Mapping(source = "dto.version", target = "version"),
            @Mapping(source = "dto.fromAddress", target = "fromAddress"),
            @Mapping(source = "dto.toAddress", target = "toAddress"),
            @Mapping(source = "dto.currency", target = "currency"),
            @Mapping(source = "dto.amount", target = "amount", qualifiedByName = "convertBigDecimalToString"),
            @Mapping(source = "dto", target = "gasPrice", qualifiedByName = "convertGasPriceToString"),
            @Mapping(source = "dto.gasLimit", target = "gasLimit"),
            @Mapping(source = "dto", target = "txFee", qualifiedByName = "getTxFee"),
            @Mapping(source = "dto.blockNumber", target = "blockNumber"),
            @Mapping(source = "dto.blockHash", target = "blockHash"),
            @Mapping(source = "dto.txHash", target = "txHash"),
            @Mapping(source = "dto.nonce", target = "nonce"),
            @Mapping(source = "dto.status", target = "status"),
            @Mapping(source = "dto.comments", target = "comments"),
    })
    MgtWalletTransferDto convertToMgtWalletTransferDto(AddressCollectHisDto dto);

    List<MgtWalletTransferDto> convertToMgtWalletTransferDtos(List<AddressCollectHisDto> dtos);

    @Named("getTxFee")
    default String getTxFee(AddressCollectHisDto dto){
        if(dto != null) {
            if(dto.getChain() == Chain.TRON) {
                return dto.getTxFee().divide(BigDecimal.valueOf(Math.pow(10, dto.getChain().getDecimals())), 6, RoundingMode.DOWN).toPlainString();
            }
            return convertBigDecimalToString(dto.getTxFee());
        }
        return null;
    }

    @Named("convertBigDecimalToStringTransfer")
    default String convertBigDecimalToStringTransfer(AddressCollectOrderHisDto dto){
        if(dto != null) {
            if(dto.getChain() == Chain.TRON) {
                return dto.getFeeAmount().divide(BigDecimal.valueOf(Math.pow(10, dto.getChain().getDecimals())), 6,
                        RoundingMode.DOWN).toPlainString();
            }
            return convertBigDecimalToString(dto.getFeeAmount());
        }
        return null;
    }

    @Named("convertGasPriceToString")
    default String convertGasPriceToString(AddressCollectHisDto dto){
        if(dto != null) {
            if(dto.getChain() == Chain.TRON) {
                return "1";
            }
            return BigDecimal.valueOf(dto.getGasPrice()).divide(BigDecimal.valueOf(Math.pow(10,
                    dto.getChain().getDecimals() / 2))).toPlainString();
        }
        return null;
    }

}
