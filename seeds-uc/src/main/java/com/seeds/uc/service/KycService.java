package com.seeds.uc.service;

import com.seeds.uc.dto.KycMgtDto;
import com.seeds.uc.dto.PageInfo;

/**
 * @author allen
 */
public interface KycService {

    PageInfo<KycMgtDto> selectKycDtoByPagination(int status, int currentPage, int pageSize);
}