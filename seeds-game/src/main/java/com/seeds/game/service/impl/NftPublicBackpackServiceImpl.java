package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.entity.ServerRoleEntity;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftPublicBackpackMapper;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.service.IServerRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * NFT公共背包 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@Service
public class NftPublicBackpackServiceImpl extends ServiceImpl<NftPublicBackpackMapper, NftPublicBackpackEntity> implements INftPublicBackpackService {

    @Autowired
    private IServerRoleService serverRoleService;

    @Override
    public IPage<NftPublicBackpackResp> queryPage(NftPublicBackpackPageReq req) {

        LambdaQueryWrapper<NftPublicBackpackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(req.getIsConfiguration()), NftPublicBackpackEntity::getIsConfiguration, req.getIsConfiguration())
                .eq(NftPublicBackpackEntity::getUserId, req.getUserId());

        Page<NftPublicBackpackEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<NftPublicBackpackEntity> records = this.page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NftPublicBackpackResp resp = new NftPublicBackpackResp();
            // todo 关联获取物品的详细信息
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void create(NftPublicBackpackReq req) {
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        BeanUtils.copyProperties(req, entity);
        this.save(entity);
    }

    @Override
    public void update(NftPublicBackpackReq req) {
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        BeanUtils.copyProperties(req, entity);
        this.updateById(entity);
    }

    @Override
    public NftPublicBackpackResp detail(Long id) {
        NftPublicBackpackResp nftPublicBackpackResp = new NftPublicBackpackResp();
        NftPublicBackpackEntity entity = this.getById(id);
        if (null != entity) {
            BeanUtils.copyProperties(entity, nftPublicBackpackResp);
        }
        return nftPublicBackpackResp;
    }

    @Override
    public OpenNftPublicBackpackDisResp distribute(NftPublicBackpackDisReq req) {
        NftPublicBackpackEntity nftItem = this.getById(req.getId());

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 1.校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 2.校验当前NFT物品是否是未分配转态
        if (nftItem.getIsConfiguration().equals(NftConfigurationEnum.ASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_HAVE_BEEN_ASSIGNED);
        }
        // 3.校验分配的角色等级是否满10级
        ServerRoleEntity roleEntity = serverRoleService.getById(req.getServerRoleId());
        if (roleEntity.getLevel() < 10) {
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_LEVE_IS_LESS_THAN_TEN);
        }

        // 4.执行分配
        nftItem.setServerRoleId(req.getServerRoleId());
        this.updateById(nftItem);

        // 组装返回值
        OpenNftPublicBackpackDisResp resp = new OpenNftPublicBackpackDisResp();
        resp.setGameServer(roleEntity.getGameServer());
        resp.setItemName(nftItem.getName());
        resp.setRegion(roleEntity.getRegion());
        resp.setRoleName(roleEntity.getName());
        resp.setLevel(roleEntity.getLevel());
        return resp;
    }

    @Override
    public void takeBack(NftPublicBackpackTakeBackReq req) {

        NftPublicBackpackEntity nftItem = this.getById(req.getId());

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 收回
        nftItem.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        nftItem.setServerRoleId(null);
        this.updateById(nftItem);
    }

    @Override
    public OpenNftPublicBackpackDisResp transfer(NftPublicBackpackDisReq req) {
        NftPublicBackpackEntity nftItem = this.getById(req.getId());

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 校验当前NFT物品是否是分配转态
        if (nftItem.getIsConfiguration().equals(NftConfigurationEnum.UNASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_CANNOT_TRANSFER);
        }
        // 校验分配的角色等级是否满10级
        ServerRoleEntity roleEntity = serverRoleService.getById(req.getServerRoleId());
        if (roleEntity.getLevel() < 10) {
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_LEVE_IS_LESS_THAN_TEN);
        }
        // 转移
        nftItem.setServerRoleId(req.getServerRoleId());
        this.updateById(nftItem);

        // 组装返回值
        OpenNftPublicBackpackDisResp resp = new OpenNftPublicBackpackDisResp();
        resp.setGameServer(roleEntity.getGameServer());
        resp.setItemName(nftItem.getName());
        resp.setRegion(roleEntity.getRegion());
        resp.setRoleName(roleEntity.getName());
        resp.setLevel(roleEntity.getLevel());
        return resp;
    }
}
