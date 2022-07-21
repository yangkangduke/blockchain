package com.seeds.admin.web.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.seeds.admin.dto.common.ListReq;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.merchant.request.SysMerchantAddReq;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.dto.merchant.request.SysMerchantPageReq;
import com.seeds.admin.dto.merchant.request.SysMerchantUserAddReq;
import com.seeds.admin.dto.merchant.response.SysMerchantResp;
import com.seeds.admin.dto.merchant.response.SysMerchantUserResp;
import com.seeds.admin.dto.sys.request.SysUserAddReq;
import com.seeds.admin.dto.sys.request.SysUserRoleReq;
import com.seeds.admin.entity.merchant.SysMerchantEntity;
import com.seeds.admin.entity.merchant.SysMerchantUserEntity;
import com.seeds.admin.entity.sys.SysRoleEntity;
import com.seeds.admin.entity.sys.SysUserEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.web.game.service.SysGameService;
import com.seeds.admin.web.merchant.mapper.SysMerchantMapper;
import com.seeds.admin.web.merchant.service.SysMerchantGameService;
import com.seeds.admin.web.merchant.service.SysMerchantService;
import com.seeds.admin.web.merchant.service.SysMerchantUserService;
import com.seeds.admin.web.sys.service.SysRoleService;
import com.seeds.admin.web.sys.service.SysRoleUserService;
import com.seeds.admin.web.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统商家
 *
 * @author hang.yu
 * @date 2022/7/19
 */
@Service
public class SysMerchantServiceImpl extends ServiceImpl<SysMerchantMapper, SysMerchantEntity> implements SysMerchantService {

    @Value("${admin.merchant.role.code:merchant}")
    private String roleCode;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysGameService sysGameService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysMerchantUserService sysMerchantUserService;

    @Autowired
    private SysMerchantGameService sysMerchantGameService;

    @Override
    public List<SysMerchantResp> queryList() {
        QueryWrapper<SysMerchantEntity> query = new QueryWrapper<>();
        query.eq("delete_flag", WhetherEnum.NO.value());
        List<SysMerchantEntity> list = list(query);
        return convertToResp(list);
    }

    @Override
    public SysMerchantEntity queryById(Long id) {
        QueryWrapper<SysMerchantEntity> query = new QueryWrapper<>();
        query.eq("id", id);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    public List<SysMerchantEntity> queryByIds(Collection<Long> ids) {
        QueryWrapper<SysMerchantEntity> query = new QueryWrapper<>();
        query.in("id", ids);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return list(query);
    }

    @Override
    public IPage<SysMerchantResp> queryPage(SysMerchantPageReq query) {
        QueryWrapper<SysMerchantEntity> queryWrap = new QueryWrapper<>();
        queryWrap.likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), "name", query.getNameOrMobile())
                .or().likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), "mobile", query.getNameOrMobile());
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        Page<SysMerchantEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysMerchantEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> userIds = records.stream().map(SysMerchantEntity::getLeaderId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> userMap = sysUserService.queryNameMapByIds(userIds);
        return page.convert(p -> {
            SysMerchantResp resp = new SysMerchantResp();
            BeanUtils.copyProperties(p, resp);
            resp.setLeaderName(userMap.get(p.getLeaderId()));
            return resp;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysMerchantAddReq req) {
        SysMerchantEntity merchant = new SysMerchantEntity();
        Long leaderId = req.getLeaderId();
        if (req.getLeaderId() == null) {
            // 手机号查询用户
            SysUserEntity adminUser = sysUserService.queryByMobile(req.getMobile());
            if (adminUser == null) {
                // 添加商家负责人
                SysUserAddReq addReq = new SysUserAddReq();
                addReq.setMobile(req.getMobile());
                addReq.setRealName(req.getLeaderName());
                adminUser = sysUserService.add(addReq);
            }
            leaderId = adminUser.getId();
        }
        // 添加商家
        BeanUtils.copyProperties(req, merchant);
        merchant.setLeaderId(leaderId);
        save(merchant);
        // 建立用户和商家负责人关联
        sysMerchantUserService.add(merchant.getId(), Collections.singletonList(leaderId));
        // 给负责人分配角色
        SysRoleEntity sysRole = sysRoleService.queryByRoleCode(roleCode);
        if (sysRole != null) {
            SysUserRoleReq userRole = new SysUserRoleReq();
            userRole.setUserId(merchant.getLeaderId());
            userRole.setRoleIds(Collections.singletonList(sysRole.getId()));
            sysRoleUserService.assignRoles(userRole);
        }
    }

    @Override
    public SysMerchantResp detail(Long id) {
        // 商家详情
        SysMerchantEntity sysMerchant = queryById(id);
        SysMerchantResp resp = new SysMerchantResp();
        if (sysMerchant != null) {
            BeanUtils.copyProperties(sysMerchant, resp);
        }
        // 游戏列表
        resp.setGames(sysGameService.queryList(id));
        // 用户列表
        List<SysMerchantUserEntity> merchantUser = sysMerchantUserService.queryByMerchantId(id);
        if (!CollectionUtils.isEmpty(merchantUser)) {
            Set<Long> userIds = merchantUser.stream().map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
            List<SysUserEntity> sysUser = sysUserService.queryByIds(userIds);
            List<SysMerchantUserResp> userRespList = new ArrayList<>();
            sysUser.forEach(p -> {
                        SysMerchantUserResp userResp = new SysMerchantUserResp();
                        BeanUtils.copyProperties(p, userResp);
                        userRespList.add(userResp);
                    });
            resp.setUsers(userRespList);
        }
        return resp;
    }

    @Override
    public void modify(SysMerchantModifyReq req) {
        // 商家信息
        SysMerchantEntity sysMerchant = queryById(req.getId());
        if (sysMerchant == null) {
            return;
        }
        // 修改商家信息
        SysMerchantEntity merchant = new SysMerchantEntity();
        BeanUtils.copyProperties(req, merchant);
        updateById(merchant);
        // 负责人信息变更
        if (!req.getMobile().equals(sysMerchant.getMobile())) {
            SysUserEntity user = new SysUserEntity();
            user.setId(sysMerchant.getLeaderId());
            user.setRealName(req.getLeaderName());
            user.setMobile(req.getMobile());
            sysUserService.modifyById(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(ListReq req) {
        Set<Long> merchantIds = req.getIds();
        // 删除商家
        List<SysMerchantEntity> sysMerchants = queryByIds(merchantIds);
        if (CollectionUtils.isEmpty(sysMerchants)) {
            return;
        }
        sysMerchants.forEach(p -> p.setDeleteFlag(WhetherEnum.YES.value()));
        updateBatchById(sysMerchants);
        List<SysMerchantUserEntity> sysMerchantUser = sysMerchantUserService.queryByMerchantIds(merchantIds);
        if (!CollectionUtils.isEmpty(sysMerchantUser)) {
            Set<Long> userIds = sysMerchantUser.stream().map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
            // 删除商家和用户的关联
            sysMerchantUserService.batchDelete(sysMerchantUser);
            // 删除商家用户
            deleteUserByIds(userIds, merchantIds);
        }
        // 删除商家和游戏的关联
        sysMerchantGameService.deleteByMerchantIds(merchantIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableOrDisable(List<SwitchReq> req) {
        Set<Long> merchantIds = req.stream().map(SwitchReq::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> merchantUserMap = sysMerchantUserService.queryMapByMerchantIds(merchantIds);
        List<SysMerchantEntity> sysMerchants = new ArrayList<>();
        List<SwitchReq> userReqs = new ArrayList<>();
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysMerchantEntity sysMerchant = new SysMerchantEntity();
            sysMerchant.setId(p.getId());
            sysMerchant.setStatus(p.getStatus());
            sysMerchants.add(sysMerchant);
            Set<Long> userIds = merchantUserMap.get(p.getId());
            if (!CollectionUtils.isEmpty(userIds)) {
                userIds.forEach(u -> {
                    SwitchReq userReq = new SwitchReq();
                    userReq.setId(u);
                    userReq.setStatus(p.getStatus());
                    userReqs.add(userReq);
                });
            }
        });
        // 停用/启用商家
        updateBatchById(sysMerchants);
        // 停用/启用商家用户
        sysUserService.enableOrDisable(userReqs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(ListReq req, Long merchantId) {
        Set<Long> userIds = req.getIds();
        List<SysMerchantUserEntity> sysMerchantUser = sysMerchantUserService.queryByMerchantId(merchantId);
        if (CollectionUtils.isEmpty(sysMerchantUser)) {
            return;
        }
        List<SysMerchantUserEntity> deleteList = sysMerchantUser.stream().filter(p -> req.getIds().contains(p.getUserId())).collect(Collectors.toList());
        // 删除商家和用户的关联
        sysMerchantUserService.batchDelete(deleteList);
        // 删除商家用户
        deleteUserByIds(userIds, Sets.newHashSet(merchantId));
    }

    @Override
    public void deleteGame(ListReq req, Long merchantId) {
        // 删除商家和游戏的关联
        sysMerchantGameService.deleteByMerchantIdAndGameIds(merchantId, req.getIds());
    }

    @Override
    public void addGame(ListReq req, Long merchantId) {
        // 添加商家和游戏的关联
        sysMerchantGameService.add(merchantId, req.getIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(SysMerchantUserAddReq req, Long merchantId) {
        Long userId = req.getUserId();
        if (req.getUserId() == null) {
            // 手机号查询用户
            SysUserEntity adminUser = sysUserService.queryByMobile(req.getMobile());
            if (adminUser == null) {
                // 添加商家用户
                SysUserAddReq addReq = new SysUserAddReq();
                addReq.setMobile(req.getMobile());
                addReq.setRealName(req.getUserName());
                adminUser = sysUserService.add(addReq);
            }
            userId = adminUser.getId();
        }
        // 添加商家和用户的关联
        sysMerchantUserService.add(merchantId, Collections.singletonList(userId));
        // 给商家用户添加默认商家角色
        SysRoleEntity sysRole = sysRoleService.queryByRoleCode(roleCode);
        if (sysRole != null) {
            SysUserRoleReq userRole = new SysUserRoleReq();
            userRole.setUserId(userId);
            userRole.setRoleIds(Collections.singletonList(sysRole.getId()));
            sysRoleUserService.assignRoles(userRole);
        }
    }

    private void deleteUserByIds(Set<Long> userIds, Set<Long> merchantIds){
        // 排除同时在其他商家的用户
        List<SysMerchantUserEntity> merchantUsers = sysMerchantUserService.queryByUserIds(userIds);
        if (!CollectionUtils.isEmpty(merchantUsers)) {
            Set<Long> needUserIds = merchantUsers.stream().filter(p -> !merchantIds.contains(p.getMerchantId()))
                    .map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
            userIds = userIds.stream().filter(p -> !needUserIds.contains(p)).collect(Collectors.toSet());
        }
        // 删除商家用户
        if (!CollectionUtils.isEmpty(userIds)) {
            List<SysUserEntity> users = new ArrayList<>();
            userIds.forEach(p -> {
                SysUserEntity user = new SysUserEntity();
                user.setId(p);
                user.setDeleteFlag(WhetherEnum.YES.value());
                users.add(user);
            });
            sysUserService.batchModifyById(users);
        }
    }

    private List<SysMerchantResp> convertToResp(List<SysMerchantEntity> list){
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysMerchantResp> respList = new ArrayList<>();
        list.forEach(p -> {
            SysMerchantResp resp = new SysMerchantResp();
            BeanUtils.copyProperties(p, resp);
            respList.add(resp);
        });
        return respList;
    }
}