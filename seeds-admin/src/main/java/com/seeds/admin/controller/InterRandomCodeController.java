package com.seeds.admin.controller;

import com.seeds.admin.dto.request.RandomCodeUseReq;
import com.seeds.admin.service.SysRandomCodeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 随机码内部调用
 * @author hang.yu
 * @date 2022/11/08
 */
@Slf4j
@Api(tags = "随机码内部调用")
@RestController
@RequestMapping("/internal-random-code")
public class InterRandomCodeController {

    @Autowired
    private SysRandomCodeService sysRandomCodeService;

    @PostMapping("use-random-code")
    @ApiOperation("使用随机码")
    @Inner
    public GenericDto<Object> useRandomCode(@Valid @RequestBody RandomCodeUseReq req) {
        sysRandomCodeService.useRandomCode(req);
        return GenericDto.success(null);
    }

}
