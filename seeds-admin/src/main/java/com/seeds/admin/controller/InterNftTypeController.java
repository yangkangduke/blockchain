package com.seeds.admin.controller;

import com.seeds.admin.dto.response.SysNftTypeResp;
import com.seeds.admin.service.SysNftTypeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NFT类别管理内部调用
 * @author hang.yu
 * @date 2022/8/24
 */
@Slf4j
@Api(tags = "NFT类别管理内部调用")
@RestController
@RequestMapping("/internal-nft-type")
public class InterNftTypeController {

    @Autowired
    private SysNftTypeService sysNftTypeService;

    @GetMapping("uc-dropdown")
    @ApiOperation("uc下拉列表")
    @Inner
    public GenericDto<List<SysNftTypeResp>> ucDropdown() {
        return GenericDto.success(sysNftTypeService.queryRespList(null));
    }

}
