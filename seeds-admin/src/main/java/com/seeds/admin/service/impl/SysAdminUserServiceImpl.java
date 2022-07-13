package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.SysAdminUserDto;
import com.seeds.admin.dto.request.AdminUserQuery;
import com.seeds.admin.mapper.SysAdminUserMapper;
import com.seeds.admin.entity.SysAdminUserEntity;
import com.seeds.admin.service.SysAdminUserService;
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
    public SysAdminUserDto queryDtoById(Long userId) {
        SysAdminUserEntity adminUser = getById(userId);
        SysAdminUserDto dto = new SysAdminUserDto();
        if (adminUser != null) {
            BeanUtils.copyProperties(adminUser, dto);
        }
        return dto;
    }

    @Override
    public IPage<SysAdminUserDto> queryPage(AdminUserQuery query) {
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
            SysAdminUserDto dto = new SysAdminUserDto();
            BeanUtils.copyProperties(p, dto);
            return dto;
        });
    }

    @Override
    public SysAdminUserDto add(SysAdminUserDto user) {
        SysAdminUserEntity adminUser = new SysAdminUserEntity();
        BeanUtils.copyProperties(user, adminUser);
        String salt = RandomUtil.getRandomSalt();
        String password = HashUtil.sha256(user.getNewPassport() + salt);
        adminUser.setPassword(password);
        adminUser.setSalt(salt);
        adminUser.setSuperAdmin(0);
        adminUser.setStatus(1);
        adminUser.setDeleteFlag(1);
        save(adminUser);
        return user;
    }
}