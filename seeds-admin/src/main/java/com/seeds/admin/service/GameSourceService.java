package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.dto.response.GameSrcResp;
import com.seeds.admin.dto.response.PreUploadResp;
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

    Boolean delete(String fileName) throws Exception;

    List<GameSrcLinkResp> getLinks(String ip, Integer type);

    List<GameSrcResp> queryPage(SysGameSrcPageReq req);


    Boolean switchStatus(SwitchReq req);

    List<GameFileResp> getAllPatch();

    PreUploadResp preUpload(UploadFileInfo req);

    PreUploadResp createUpload(UploadFileInfo req);

    String getPartUrl(FilePartReq req);

    String completeMultipartUpload(CompleteUploadReq req);

    void add(List<SysGameSrcAddReq> reqs);

    Boolean abortUpload(CompleteUploadReq req);

    Boolean batchDelete(ListReq req) throws Exception;
}
