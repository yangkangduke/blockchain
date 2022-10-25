package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysMerchantResp;
import com.seeds.admin.dto.response.SysMerchantUserResp;
import com.seeds.admin.entity.SysMerchantEntity;
import com.seeds.admin.entity.SysMerchantUserEntity;
import com.seeds.admin.entity.SysRoleEntity;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.mapper.SysMerchantMapper;
import com.seeds.admin.service.*;
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
        return convertToResp(list());
    }

    @Override
    public IPage<SysMerchantResp> queryPage(SysMerchantPageReq query) {
        LambdaQueryWrapper<SysMerchantEntity> queryWrap = new QueryWrapper<SysMerchantEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), SysMerchantEntity::getName, query.getNameOrMobile())
                .or().likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), SysMerchantEntity::getMobile, query.getNameOrMobile())
                .orderByDesc(SysMerchantEntity::getCreatedAt);
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
        SysMerchantEntity sysMerchant = getById(id);
        SysMerchantResp resp = new SysMerchantResp();
        if (sysMerchant == null) {
            return resp;
        }
        BeanUtils.copyProperties(sysMerchant, resp);
        // 游戏列表
        resp.setGames(sysGameService.select(id));
        // 用户列表
        List<SysMerchantUserEntity> merchantUser = sysMerchantUserService.queryByMerchantId(id);
        if (!CollectionUtils.isEmpty(merchantUser)) {
            Set<Long> userIds = merchantUser.stream().map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
            List<SysUserEntity> sysUser = sysUserService.listByIds(userIds);
            List<SysMerchantUserResp> userRespList = new ArrayList<>();
            sysUser.forEach(p -> {
                        SysMerchantUserResp userResp = new SysMerchantUserResp();
                        BeanUtils.copyProperties(p, userResp);
                        userResp.setUserId(p.getId());
                        userRespList.add(userResp);
                        if (Objects.equals(p.getId(), sysMerchant.getLeaderId())) {
                            resp.setLeaderName(p.getRealName());
                        }
                    });
            resp.setUsers(userRespList);
        }
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysMerchantModifyReq req) {
        // 商家信息
        SysMerchantEntity sysMerchant = getById(req.getId());
        if (sysMerchant == null) {
            return;
        }
        SysMerchantEntity merchant = new SysMerchantEntity();
        BeanUtils.copyProperties(req, merchant);
        SysUserEntity sysUser = sysUserService.queryByMobile(req.getMobile());
        if (sysUser == null) {
            sysUser = new SysUserEntity();
        }
        sysUser.setRealName(req.getLeaderName());
        sysUser.setMobile(req.getMobile());
        sysUserService.saveOrUpdate(sysUser);
        // 负责人信息变更
        if (!Objects.equals(sysUser.getId(), sysMerchant.getId())) {
            // 新增用户商家关系
            sysMerchantUserService.add(req.getId(), Collections.singletonList(sysUser.getId()));
            // 给用户分配角色
            SysRoleEntity sysRole = sysRoleService.queryByRoleCode(roleCode);
            if (sysRole != null) {
                SysUserRoleReq userRole = new SysUserRoleReq();
                userRole.setUserId(sysUser.getId());
                userRole.setRoleIds(Collections.singletonList(sysRole.getId()));
                sysRoleUserService.assignRoles(userRole);
            }
        }
        // 修改商家信息
        merchant.setLeaderId(sysUser.getId());
        updateById(merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(ListReq req) {
        Set<Long> merchantIds = req.getIds();
        // 删除商家
        List<SysMerchantEntity> sysMerchants = listByIds(merchantIds);
        if (CollectionUtils.isEmpty(sysMerchants)) {
            return;
        }
        removeBatchByIds(sysMerchants);
        List<SysMerchantUserEntity> sysMerchantUser = sysMerchantUserService.queryByMerchantIds(merchantIds);
        if (!CollectionUtils.isEmpty(sysMerchantUser)) {
            Set<Long> userIds = sysMerchantUser.stream().map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
            // 删除商家和用户的关联
            sysMerchantUserService.removeBatchByIds(sysMerchantUser);
            // 删除商家用户
            userIds = screeningUsers(userIds, merchantIds);
            if (!CollectionUtils.isEmpty(userIds)) {
                sysUserService.removeBatchByIds(userIds);
                // 删除用户和角色的关联
                sysRoleUserService.deleteByUserIds(userIds);
            }
        }
        // 删除商家和游戏的关联
        sysMerchantGameService.deleteByMerchantIds(merchantIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableOrDisable(List<SwitchReq> req) {
        Set<Long> merchantIds = req.stream().map(SwitchReq::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> merchantUserMap = sysMerchantUserService.queryMapByMerchantIds(merchantIds);
        Set<Long> disableUsers = new HashSet<>();
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysMerchantEntity sysMerchant = new SysMerchantEntity();
            sysMerchant.setId(p.getId());
            sysMerchant.setStatus(p.getStatus());
            // 停用/启用商家
            updateById(sysMerchant);
            Set<Long> userIds = merchantUserMap.get(p.getId());
            if (!CollectionUtils.isEmpty(userIds)) {
                if (SysStatusEnum.ENABLED.value() == p.getStatus()) {
                    // 启用商家用户
                    userIds.forEach(u -> {
                        SysUserEntity user = new SysUserEntity();
                        user.setId(u);
                        user.setStatus(p.getStatus());
                        sysUserService.updateById(user);
                    });
                } else {
                    // 需要停用的商家用户
                    disableUsers.addAll(userIds);
                }
            }
        });
        if (!CollectionUtils.isEmpty(disableUsers)) {
            // 停用商家用户
            Set<Long> users = screeningUsers(disableUsers, merchantIds);
            if (!CollectionUtils.isEmpty(users)) {
                users.forEach(p -> {
                    SysUserEntity user = new SysUserEntity();
                    user.setId(p);
                    user.setStatus(SysStatusEnum.DISABLE.value());
                    sysUserService.updateById(user);
                });
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(ListReq req, Long merchantId) {
        SysMerchantEntity sysMerchant = getById(merchantId);
        if (sysMerchant == null) {
            return;
        }
        // 排除负责人
        Set<Long> userIds = req.getIds().stream().filter(p -> !Objects.equals(p, sysMerchant.getLeaderId())).collect(Collectors.toSet());
        List<SysMerchantUserEntity> sysMerchantUser = sysMerchantUserService.queryByMerchantId(merchantId);
        if (CollectionUtils.isEmpty(sysMerchantUser)) {
            return;
        }
        Set<Long> finalUserIds = userIds;
        List<SysMerchantUserEntity> deleteList = sysMerchantUser.stream().filter(p -> finalUserIds.contains(p.getUserId())).collect(Collectors.toList());
        // 删除商家和用户的关联
        sysMerchantUserService.removeBatchByIds(deleteList);
        // 删除商家用户
        userIds = screeningUsers(userIds, Sets.newHashSet(merchantId));
        if (!CollectionUtils.isEmpty(userIds)) {
            sysUserService.removeByIds(userIds);
        }
        // 删除用户和角色的关联
        sysRoleUserService.deleteByUserIds(userIds);
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

    @Override
    public SysMerchantEntity queryByUrl(String url) {
        LambdaQueryWrapper<SysMerchantEntity> query = new QueryWrapper<SysMerchantEntity>().lambda()
                .eq(SysMerchantEntity::getUrl, url);
        return getOne(query);
    }

    private Set<Long> screeningUsers(Set<Long> userIds, Set<Long> merchantIds){
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptySet();
        }
        // 排除同时在其他商家的用户
        List<SysMerchantUserEntity> merchantUsers = sysMerchantUserService.queryByUserIds(userIds);
        if (!CollectionUtils.isEmpty(merchantUsers)) {
            Set<Long> needUserIds = merchantUsers.stream().filter(p -> !merchantIds.contains(p.getMerchantId()))
                    .map(SysMerchantUserEntity::getUserId).collect(Collectors.toSet());
            userIds = userIds.stream().filter(p -> !needUserIds.contains(p)).collect(Collectors.toSet());
        }
        return userIds;
    }

    private List<SysMerchantResp> convertToResp(List<SysMerchantEntity> list){
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Set<Long> leaderIds = list.stream().map(SysMerchantEntity::getLeaderId).collect(Collectors.toSet());
        Map<Long, String> userMap = sysUserService.queryNameMapByIds(leaderIds);
        List<SysMerchantResp> respList = new ArrayList<>();
        list.forEach(p -> {
            SysMerchantResp resp = new SysMerchantResp();
            BeanUtils.copyProperties(p, resp);
            resp.setLeaderName(userMap.get(p.getLeaderId()));
            respList.add(resp);
        });
        return respList;
    }
}