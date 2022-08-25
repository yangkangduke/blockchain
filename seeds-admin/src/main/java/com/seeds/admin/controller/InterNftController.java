package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.enums.NftInitStatusEnum;
import com.seeds.admin.service.SysNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * NFT管理内部调用
 * @author hang.yu
 * @date 2022/7/22
 */
@Slf4j
@Api(tags = "NFT管理内部调用")
@RestController
@RequestMapping("/internal-nft")
public class InterNftController {

    @Autowired
    private SysNftService sysNftService;

    @PostMapping("owner-change")
    @ApiOperation("归属人变更")
    @Inner
    public GenericDto<Object> ownerChange(@Valid @RequestBody List<NftOwnerChangeReq> req) {
        sysNftService.ownerChange(req);
        return GenericDto.success(null);
    }

    @PostMapping("properties-modify")
    @ApiOperation("属性值修改")
    public GenericDto<Object> propertiesModify(@Valid @RequestBody List<NftPropertiesValueModifyReq> req) {
        sysNftService.propertiesValueModify(req);
        return GenericDto.success(null);
    }

    @PostMapping("uc-page")
    @ApiOperation("uc分页查询NFT")
    @Inner
    public GenericDto<IPage<SysNftResp>> ucPage(@Valid @RequestBody SysNftPageReq query) {
        query.setInitStatus(NftInitStatusEnum.NORMAL.getCode());
        return GenericDto.success(sysNftService.queryPage(query));
    }

    @GetMapping("uc-detail")
    @ApiOperation("uc查询NFT信息")
    @Inner
    public GenericDto<SysNftDetailResp> ucDetail(@RequestParam Long id) {
        return GenericDto.success(sysNftService.ucDetail(id));
    }

}
