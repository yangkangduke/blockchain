package com.seeds.uc.service.impl;

import com.seeds.uc.dto.KycMgtDto;
import com.seeds.uc.dto.PageInfo;
import com.seeds.uc.dto.mapstruct.FileDtoMapper;
import com.seeds.uc.dto.mapstruct.KycDtoMapper;
import com.seeds.uc.mapper.KycDetailMapper;
import com.seeds.uc.model.KycDetail;
import com.seeds.uc.service.KycService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KycServiceImpl implements KycService {

    @Autowired
    private KycDetailMapper kycDetailMapper;

    @Autowired
    private KycDtoMapper kycDtoMapper;

    @Autowired
    private FileDtoMapper fileDtoMapper;

    @Override
    public PageInfo<KycMgtDto> selectKycDtoByPagination(int status, int currentPage, int pageSize) {
        KycDetail kycDetail = new KycDetail();
        kycDetail.setStatus(status);
        int totalRecords = kycDetailMapper.countKycRecords(kycDetail);
        int totalPage = totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;
        List<KycDetail> kycDetails = kycDetailMapper.selectKycByPagination(currentPage - 1, pageSize, status);

        List<KycMgtDto> kycMgtDtos = kycDetails.stream().map(k ->
        {
            KycMgtDto kycMgtDto = kycDtoMapper.kycToMgtDto(k);
            kycMgtDto.setFileDto(fileDtoMapper.kycToDto(k.getFile()));
            return kycMgtDto;
        }).collect(Collectors.toList());

        PageInfo<KycMgtDto> kycDtoPages = new PageInfo<>();

        kycDtoPages.setCurrentPage(currentPage);
        kycDtoPages.setPageSize(pageSize);
        kycDtoPages.setTotalPage(totalPage);
        kycDtoPages.setData(kycMgtDtos);
        return kycDtoPages;
    }
}
