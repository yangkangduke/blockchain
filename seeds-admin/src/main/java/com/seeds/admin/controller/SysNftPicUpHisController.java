package com.seeds.admin.controller;


import com.seeds.admin.service.SysNftPicUpHisService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NFT上传文件记录
 * @author yang.deng
 * @date 2023/2/20
 */
@Slf4j
@Api(tags = "NFT上传文件记录")
@RestController
@RequestMapping("/nft-pic-up-history")
public class SysNftPicUpHisController {

    @Autowired
    private SysNftPicUpHisService sysNftPicUpHisService;

}
