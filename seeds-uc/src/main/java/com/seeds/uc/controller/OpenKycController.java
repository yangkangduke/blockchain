package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.KycDto;
import com.seeds.uc.dto.mapstruct.KycDtoMapper;
import com.seeds.uc.dto.request.SaveKycReq;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.mapper.KycDetailMapper;
import com.seeds.uc.model.File;
import com.seeds.uc.model.KycDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */
@Slf4j
@RestController
@RequestMapping("/uc/kyc")
public class OpenKycController {
    @Autowired
    private KycDetailMapper kycDetailMapper;

    @Autowired
    private KycDtoMapper kycDtoMapper;

    @GetMapping("get")
    public GenericDto<KycDto> getKycDetail() {
        List<KycDetail> kycDetails = kycDetailMapper.selectByUid(UserContext.getCurrentUserId());

        KycDto kycDto = null;
        if (kycDetails != null && kycDetails.size() > 0) {
            KycDetail kycDetail = kycDetails.get(0);
            kycDto = kycDtoMapper.kycToDto(kycDetail);
        }
        return GenericDto.success(kycDto);
    }

    @PostMapping("save")
    public GenericDto<Object> saveKycInfo(@RequestBody SaveKycReq saveKycReq) {
        Long uid = UserContext.getCurrentUserId();
        List<KycDetail> kycDetails = kycDetailMapper.selectByUid(uid);

        KycDetail kycDetail = null;
        //查看是否有审核拒绝的记录
        if (kycDetails != null && kycDetails.size() > 0) {
            kycDetail = kycDetails.get(0);
        }

        if (kycDetail != null && kycDetail.getStatus() != 3) {
            return GenericDto.failure(UcErrorCode.ERR_12000_KYC_ALREADY_EXIST.getDescEn(),
                    UcErrorCode.ERR_12000_KYC_ALREADY_EXIST.getCode());
        } else {
            File file = new File();
            file.setUuid(saveKycReq.getIdentityFileUuid());
            // TODO field validations
            kycDetailMapper.insert(KycDetail.builder()
                    .uid(uid)
                    .countryCode(saveKycReq.getCountryCode())
                    .identity(saveKycReq.getIdentity())
                    //.identityFileUuid(saveKycReq.getIdentityFileUuid())
                    .firstName(saveKycReq.getFirstName())
                    .lastName(saveKycReq.getLastName())
                    .createdAt(System.currentTimeMillis())
                    .updatedAt(System.currentTimeMillis())
                    .status(1)
                    .comment("")
                    .operatorId(0)
                    .operatorName("")
                    .file(file)
                    .build());
        }

        return GenericDto.success(null);
    }

}
