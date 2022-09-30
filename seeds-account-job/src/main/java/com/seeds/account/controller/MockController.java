package com.seeds.account.controller;

import com.seeds.account.feign.AccountFeignClient;
import com.seeds.common.dto.GenericDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guocheng
 * @date 2020/12/30
 */
@RestController
@Slf4j
@RequestMapping("/mock")
public class MockController {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @GetMapping("scan-block")
    public GenericDto<Boolean> scanBlock() {
//        accountFeignClient.scanBlock();
        return GenericDto.success(true);
    }
}
