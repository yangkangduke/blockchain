package com.seeds.admin.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.enums.NftInitStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mq.producer.KafkaProducer;
import com.seeds.admin.service.SysNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.NFTShelvesReq;
import com.seeds.uc.dto.request.NFTSoldOutReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    @Resource
    private KafkaProducer kafkaProducer;

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
        query.setLockFlag(WhetherEnum.NO.value());
        return GenericDto.success(sysNftService.queryPage(query));
    }

    @GetMapping("uc-detail")
    @ApiOperation("uc查询NFT信息")
    @Inner
    public GenericDto<SysNftDetailResp> ucDetail(@RequestParam Long id) {
        return GenericDto.success(sysNftService.ucDetail(id));
    }

    @PostMapping("uc-collection")
    @ApiOperation("uc收藏")
    @Inner
    public GenericDto<Object> ucCollection(@RequestParam Long id) {
        sysNftService.ucCollection(id);
        return GenericDto.success(null);
    }

    @PostMapping("uc-view")
    @ApiOperation("uc浏览")
    @Inner
    public GenericDto<Object> ucView(@RequestParam Long id) {
        sysNftService.ucView(id);
        return GenericDto.success(null);
    }

    @PostMapping("uc-switch")
    @ApiOperation("上架/下架")
    @Inner
    public GenericDto<Object> ucUpOrDown(@Valid @RequestBody UcSwitchReq req) {
        sysNftService.ucUpOrDown(req);
        return GenericDto.success(null);
    }

    @PostMapping("create")
    @ApiOperation("NFT创建")
    @Inner
    public GenericDto<Long> create(@RequestPart("image") MultipartFile image, @RequestParam String metaData) {
        return GenericDto.success(sysNftService.addSend(image, JSONUtil.toBean(metaData, SysNftAddReq.class), KafkaTopic.GAME_NFT_SAVE_SUCCESS));
    }

    @PostMapping("modify")
    @ApiOperation("NFT修改")
    @Inner
    public GenericDto<Object> modify(@Valid @RequestBody SysNftModifyReq req) {
        sysNftService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("honor-modify")
    @ApiOperation("NFT战绩更新")
    @Inner
    public GenericDto<Object> honorModify(@Valid @RequestBody List<SysNftHonorModifyReq> req) {
        kafkaProducer.send(KafkaTopic.GAME_NFT_HONOR_MODIFY, JSONUtil.toJsonStr(req));
        return GenericDto.success(null);
    }

    @PostMapping("upgrade")
    @ApiOperation("NFT升级")
    @Inner
    public GenericDto<Long> upgrade(@RequestPart("image") MultipartFile image, @RequestParam String data) {
        return GenericDto.success(sysNftService.upgradeSend(image, JSONUtil.toBean(data, SysNftUpgradeReq.class)));
    }

    @PostMapping("lock")
    @ApiOperation("NFT锁定")
    @Inner
    public GenericDto<Object> lock(@Valid @RequestBody SysNftLockReq req) {
        sysNftService.lock(req);
        return GenericDto.success(null);
    }

    @PostMapping("settlement")
    @ApiOperation("NFT结算")
    @Inner
    public GenericDto<Object> settlement(@Valid @RequestBody SysNftSettlementReq req) {
        sysNftService.settlement(req);
        return GenericDto.success(null);
    }

    @PostMapping("page-api")
    @ApiOperation("NFT列表")
    @Inner
    public GenericDto<IPage<SysNftResp>> pageApi(@Valid @RequestBody SysNftPageReq query) {
        query.setInitStatus(NftInitStatusEnum.NORMAL.getCode());
        query.setLockFlag(WhetherEnum.NO.value());
        return GenericDto.success(sysNftService.queryPage(query));
    }

    @GetMapping("detail-api")
    @ApiOperation("NFT详情")
    @Inner
    public GenericDto<SysNftDetailResp> detailApi(@RequestParam Long id) {
        return GenericDto.success(sysNftService.detailApi(id));
    }
    @PostMapping("shelves")
    @ApiOperation("NFT上架")
    @Inner
    public GenericDto<Object> shelves(@Valid @RequestBody NFTShelvesReq req) {
        sysNftService.shelves(req);
        return GenericDto.success(null);
    }

    @PostMapping("sold-out")
    @ApiOperation("NFT下架")
    @Inner
    public GenericDto<Object> soldOut(@Valid @RequestBody NFTSoldOutReq req) {
        sysNftService.soldOut(req);
        return GenericDto.success(null);
    }
}
