package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.mq.NftMintMsgDTO;
import com.seeds.admin.dto.mq.NftUpgradeMsgDTO;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftGasFeesResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.SysNftEntity;
import com.seeds.uc.dto.request.NFTShelvesReq;
import com.seeds.uc.dto.request.NFTSoldOutReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 系统NFT
 *
 * @author hang.yu
 * @date 2022/7/22
 */
public interface SysNftService extends IService<SysNftEntity> {

    /**
     * 分页获取系统NFT信息
     * @param query 分页查询条件
     * @return 系统NFT信息
     */
    IPage<SysNftResp> queryPage(SysNftPageReq query);

    /**
     * 添加系统NFT信息
     * @param imageUrl NFT图片
     * @param req NFT信息
     * @return NFT的唯一标识
     */
    Long add(String imageUrl, SysNftAddReq req);

    /**
     * 添加系统NFT信息发送消息
     * @param image NFT图片
     * @param req NFT信息
     * @return NFT的唯一标识
     */
    String addUpload(MultipartFile image, SysNftAddReq req);

    /**
     * 添加系统NFT信息发送消息
     * @param req NFT信息
     * @param topic 主题
     * @return NFT的唯一标识
     */
    Long createSend(SysNftCreateReq req, String topic);

    /**
     * 通过id获取系统NFT信息
     * @param id NFT的id
     * @return 系统NFT信息
     */
    SysNftDetailResp detail(Long id);

    /**
     * 修改系统NFT信息
     * @param req NFT信息
     */
    void modify(SysNftModifyReq req);

    /**
     * 上架/下架系统NFT信息
     * @param req NFT的id列表和状态
     */
    void upOrDown(List<SwitchReq> req);

    /**
     * 批量删除系统NFT信息
     * @param req NFT的id列表
     */
    void batchDelete(ListReq req);

    /**
     * 修改NFT属性值
     * @param req NFT属性
     */
    void propertiesValueModify(List<NftPropertiesValueModifyReq> req);

    /**
     * 修改NFT归属人
     * @param req NFT归属人
     */
    void ownerChange(List<NftOwnerChangeReq> req);

    /**
     * 通过id获取系统NFT信息
     * @param id NFT的id
     * @return 系统NFT信息
     */
    SysNftDetailResp ucDetail(Long id);

    /**
     * 收藏
     * @param id NFT的id
     */
    void ucCollection(Long id);

    /**
     * 浏览
     * @param id NFT的id
     */
    void ucView(Long id);

    /**
     * uc上架/下架系统NFT信息
     * @param req NFT的id列表和状态
     */
    void ucUpOrDown(UcSwitchReq req);

    /**
     * 查询用户拥有的NFT
     *
     * @param ownerId 归属人id
     * @return 拥有的NFT信息
     */
    List<SysNftEntity> queryNormalByOwnerId(Long ownerId);

    /**
     * mint NFT
     *
     * @param msgDTO mintNFT消息DTO
     * @return 结果
     */
    Boolean mintNft(NftMintMsgDTO msgDTO);

    /**
     * burn NFT
     *
     * @param sysNftEntities NFT信息
     */
    void burnNft(List<SysNftEntity> sysNftEntities);

    /**
     * NFT战绩更新
     *
     * @param req NFT战绩记录
     */
    void honorModify(List<SysNftHonorModifyReq> req);

    /**
     * NFT升级发消息
     * @param req NFT信息
     * @return 新NFT的唯一标识
     */
    Long upgradeSend(SysNftUpgradeReq req);

    /**
     * NFT升级
     * @param req NFT信息
     */
    void upgrade(NftUpgradeMsgDTO req);

    /**
     * NFT锁定
     * @param req NFT信息
     */
    void lock(SysNftLockReq req);

    /**
     * NFT结算
     * @param req NFT对局记录
     */
    void settlement(SysNftSettlementReq req);

    /**
     * NFT交易详情
     * @param id NFT的id
     * @return 系统NFT信息
     */
    SysNftDetailResp detailApi(Long id);

    /**
     * NFT上架
     * @param req NFT信息
     */
    void shelves(NFTShelvesReq req);

    /**
     * NFT下架
     * @param req NFT信息
     */
    void soldOut(NFTSoldOutReq req);

    /**
     * NFT费用
     * @param req 上链参数
     * @return NFT费用
     */
    SysNftGasFeesResp gasFees(SysNftGasFeesReq req);

}
