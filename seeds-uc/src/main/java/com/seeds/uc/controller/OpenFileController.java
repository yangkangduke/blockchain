package com.seeds.uc.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.ChangePasswordReq;
import com.seeds.uc.dto.request.NickNameReq;
import com.seeds.uc.dto.request.UcFileQueryResp;
import com.seeds.uc.dto.request.UcFileResp;
import com.seeds.uc.dto.response.ProfileResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.exceptions.PasswordException;
import com.seeds.uc.model.UcFile;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcFileService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;

/**
 * <p>
 * file table 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-08-05
 */
@RestController
@Api(tags = "文件")
public class OpenFileController {

    @Autowired
    private IUcFileService ucFileService;

    /**
     * 分页查询
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public GenericDto<Page<UcFile>> getSysFilePage(UcFileQueryResp fileQueryResp) {
        Page<UcFile> page = new Page();
        page.setSize(fileQueryResp.getSize());
        page.setCurrent(fileQueryResp.getCurrent());
        UcFile ucFile = UcFile.builder().build();
        BeanUtil.copyProperties(fileQueryResp, ucFile);
        return GenericDto.success(ucFileService.page(page, Wrappers.query(ucFile)));
    }

    /**
     * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     * @param file 资源
     */
    @PostMapping("/upload")
    @ApiOperation("上传")
    public GenericDto<UcFileResp> upload(@RequestPart("file") MultipartFile file) {
        return ucFileService.upload(file);
    }

    @GetMapping("download/{objectName}")
    @ApiOperation("下载")
    public void download(HttpServletResponse response, @PathVariable String objectName) {
        ucFileService.download(response, objectName);
    }

    @GetMapping("link/{objectName}")
    @ApiOperation("链接")
    public GenericDto<String> getFile(@PathVariable String objectName) {
        return GenericDto.success(ucFileService.getFile(objectName));
    }

    @PostMapping("delete/{fileId}")
    @ApiOperation("删除")
    public GenericDto<String> delete(@PathVariable Long fileId) {
        ucFileService.delete(fileId);
        return GenericDto.success(null);
    }


}
