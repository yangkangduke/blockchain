package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.KycMgtDto;
import com.seeds.uc.dto.PageInfo;
import com.seeds.uc.dto.mapstruct.KycDtoMapper;
import com.seeds.uc.dto.request.KycReviewReq;
import com.seeds.uc.mapper.KycDetailMapper;
import com.seeds.uc.model.KycDetail;
import com.seeds.uc.service.KycService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author allen
 */
@Slf4j
@RestController
@RequestMapping("/uc-internal/kyc/")
public class InterKycController {

    @Autowired
    private KycDetailMapper kycDetailMapper;

    @Autowired
    private KycDtoMapper kycDtoMapper;

    @Autowired
    private KycService kycService;

    @GetMapping("/list")
    public GenericDto<PageInfo<KycMgtDto>> getKycList(@RequestParam("status") int status,
                                                      @RequestParam("currentPage") int currentPage,
                                                      @RequestParam("pageSize") int pageSize) {
        if (currentPage < 1) {
            return GenericDto.failure("invalid currentPage", 1);
        }

        if (pageSize < 1) {
            return GenericDto.failure("invalid pageSize", 1);
        }

        PageInfo<KycMgtDto> kycDtoPage = kycService.selectKycDtoByPagination(status, currentPage, pageSize);
        return GenericDto.success(kycDtoPage);
    }

    @PostMapping("/update")
    public GenericDto<Boolean> reviewKyc(@RequestBody KycReviewReq kycReviewReq) {
        KycDetail kycDetails = new KycDetail();
        BeanUtils.copyProperties(kycReviewReq, kycDetails);
        kycDetails.setOperatorId(11);
        kycDetails.setOperatorName("admin");
        kycDetails.setUpdatedAt(System.currentTimeMillis());

        kycDetailMapper.updateKycStatus(kycDetails);
        return GenericDto.success(true);
    }

    @GetMapping("passed")
    public GenericDto<Boolean> checkPassedKyc(@RequestParam("uid") Long uid) {
        KycDetail kycDetail = kycDetailMapper.selectPassedKycDetailByUid(uid);
        Boolean res = kycDetail != null;
        return GenericDto.success(res);
    }
}