package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.SkinNFTAttrDto;
import com.seeds.admin.dto.SkinNftPushAutoIdReq;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftPicMIntedResp;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface SysNftPicService extends IService<SysNftPicEntity> {

    IPage<SysNftPicResp> queryPage(SysNftPicPageReq req);

    Integer upload(MultipartFile file);

    String getAttr(Long id);

    SkinNFTAttrDto handleAttr(SysNftPicEntity entity);

    void updateAttribute(SysNftPicAttributeModifyReq req);

    void getPackageDownload(HttpServletResponse response, ListReq req);

    void applyAutoIds(ListReq ids);

    void pushAutoId(SkinNftPushAutoIdReq req);

    void skinMint(SysSkinNftMintReq req);

    void listAsset(SysSkinNftListAssetReq req);

    void englishV2(SysSkinNftEnglishReq req);

    void shadowUploadSuccess(ListStringReq req);

    void cancelAsset(ListReq req);

    IPage<SysNftPicMIntedResp> queryMintedPage(SysNftPicMintedPageReq req);

    void cancelAuction(ListReq req);
}
