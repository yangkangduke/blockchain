package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.request.SysGameSrcAddReq;
import com.seeds.admin.dto.request.SysGameSrcPageReq;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.dto.response.GameSrcResp;
import com.seeds.admin.entity.SysGameSourceEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/12/17
 */
public interface GameSourceService extends IService<SysGameSourceEntity> {

    void upload(MultipartFile[] files, SysGameSrcAddReq req);

    List<GameFileResp> getAll();

    void delete(String fileName);

    List<GameSrcLinkResp> getLinks(String ip, Integer type);

    IPage<GameSrcResp> queryPage(SysGameSrcPageReq req);


    Boolean switchStatus(SwitchReq req);

    List<GameFileResp> getAllPatch();
}
