package com.seeds.account.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.util.ObjectUtils;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.utils.BasicUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 账户系统提供的内部调用接口，调用方包括
 * 1. seeds-account-job-service
 *
 * @author milo
 */
@RestController
@Slf4j
@Api(tags = "账户-内部调用")
@RequestMapping("/account-internal")
public class AccountInternalController {

    @Autowired
    private IChainActionService chainActionService;

    @PostMapping("/job/scan-and-create-addresses")
    @ApiOperation("扫描并创建空闲地址")
    public GenericDto<Boolean> scanAndCreateAddresses() {
        try {
            chainActionService.scanAndCreateAddresses();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanAndCreateAddresses", e);
            return Utils.returnFromException(e);
        }
    }
}
