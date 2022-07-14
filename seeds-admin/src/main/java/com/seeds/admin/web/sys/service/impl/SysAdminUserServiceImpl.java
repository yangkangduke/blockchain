package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.response.SysAdminUserResp;
import com.seeds.admin.dto.request.AdminUserReq;
import com.seeds.admin.web.sys.mapper.SysAdminUserMapper;
import com.seeds.admin.entity.sys.SysAdminUserEntity;
import com.seeds.admin.web.sys.service.SysAdminUserService;
import com.seeds.admin.utils.HashUtil;
import com.seeds.admin.utils.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Service
public class SysAdminUserServiceImpl extends ServiceImpl<SysAdminUserMapper, SysAdminUserEntity> implements SysAdminUserService {

    @Override
    public SysAdminUserEntity queryByMobile(String mobile) {
        QueryWrapper<SysAdminUserEntity> query = new QueryWrapper<>();
        query.eq("mobile", mobile);
        query.eq("delete_flag", 0);
        return getOne(query);
    }

    @Override
    public SysAdminUserEntity queryByAccount(String account) {
        QueryWrapper<SysAdminUserEntity> query = new QueryWrapper<>();
        query.eq("account", account);
        query.eq("delete_flag", 0);
        return getOne(query);
    }

    @Override
    public SysAdminUserResp queryDtoById(Long userId) {
        SysAdminUserEntity adminUser = getById(userId);
        SysAdminUserResp dto = new SysAdminUserResp();
        if (adminUser != null) {
            BeanUtils.copyProperties(adminUser, dto);
        }
        return dto;
    }

    @Override
    public SysAdminUserEntity queryById(Long userId) {
        return getById(userId);
    }

    @Override
    public IPage<SysAdminUserResp> queryPage(AdminUserReq query) {
        QueryWrapper<SysAdminUserEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq("real_name", query.getNameOrMobile()).or().eq("mobile", query.getNameOrMobile());
        // todo 用户类型
        queryWrap.eq("delete_flag", 0);
        Page<SysAdminUserEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysAdminUserEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysAdminUserResp dto = new SysAdminUserResp();
            BeanUtils.copyProperties(p, dto);
            return dto;
        });
    }

    @Override
    public SysAdminUserResp add(SysAdminUserResp user) {
        SysAdminUserEntity adminUser = new SysAdminUserEntity();
        BeanUtils.copyProperties(user, adminUser);
        String salt = RandomUtil.getRandomSalt();
        String password = HashUtil.sha256(user.getInitPassport() + salt);
        adminUser.setPassword(password);
        adminUser.setSalt(salt);
        adminUser.setSuperAdmin(0);
        adminUser.setStatus(1);
        adminUser.setDeleteFlag(1);
        save(adminUser);
        return user;
    }

    @Override
    public void updatePassword(SysAdminUserEntity adminUser, String newPassword) {
        String salt = RandomUtil.getRandomSalt();
        String password = HashUtil.sha256(newPassword + salt);
        adminUser.setSalt(salt);
        adminUser.setPassword(password);
        updateById(adminUser);
    }
}