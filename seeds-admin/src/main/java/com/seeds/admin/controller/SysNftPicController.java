package com.seeds.admin.controller;
import com.seeds.admin.service.SysNftPicService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Api(tags = "NFT上架的管理")
@RestController
@RequestMapping("/nft-pic")
public class SysNftPicController {

    @Autowired
    private SysNftPicService sysNftPicService;
}
