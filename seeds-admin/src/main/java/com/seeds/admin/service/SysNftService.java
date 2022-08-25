package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.SysNftEntity;
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
     * @param image NFT图片
     * @param req NFT信息
     */
    void add(MultipartFile image, SysNftAddReq req);

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
    void enableOrDisable(List<SwitchReq> req);

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

}
