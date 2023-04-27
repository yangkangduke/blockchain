package com.seeds.admin.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysKolAddReq;
import com.seeds.admin.dto.request.SysKolPageReq;
import com.seeds.admin.dto.response.SysKolResp;
import com.seeds.admin.entity.GaServerRoleEntity;
import com.seeds.admin.entity.SysKolEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysKolMapper;
import com.seeds.admin.service.*;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.feign.UserCenterFeignClient;
import com.seeds.uc.model.UcUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 系统游戏
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Slf4j
@Service
public class SysKolServiceImpl extends ServiceImpl<SysKolMapper, SysKolEntity> implements SysKolService {

    @Value("${uc.invite.url}")
    private String inviteUrl;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private GaServerRoleService gaServerRoleService;

    @Override
    public IPage<SysKolResp> queryPage(SysKolPageReq query) {
        LambdaQueryWrapper<SysKolEntity> queryWrap = new QueryWrapper<SysKolEntity>().lambda()
                .eq(query.getEmail() != null, SysKolEntity::getEmail, query.getEmail())
                .orderByDesc(SysKolEntity::getCreatedAt);
        Page<SysKolEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysKolEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysKolResp resp = new SysKolResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void add(SysKolAddReq req) {
        // 邮箱校验
        String invitationCode = check(req.getEmail());
        SysKolEntity kol = new SysKolEntity();
        BeanUtils.copyProperties(req, kol);
        String inviteNo = UUID.randomUUID().toString().replace("-", "");
        kol.setInviteUrl(inviteUrl + inviteNo);
        kol.setInviteNo(inviteNo);
        kol.setInviteCode(invitationCode);
        save(kol);
    }

    @Override
    public SysKolResp detail(Long id) {
        SysKolEntity kol = getById(id);
        SysKolResp resp = new SysKolResp();
        if (kol != null) {
            BeanUtils.copyProperties(kol, resp);
        }
        return resp;
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysKolEntity kol = new SysKolEntity();
            kol.setId(p.getId());
            kol.setStatus(p.getStatus());
            updateById(kol);
        });
    }

    @Override
    public String check(String email) {
        GenericDto<UcUser> check = userCenterFeignClient.getByEmail(email);
        // 校验邮箱用户
        UcUser ucUser = check.getData();
        if (ucUser == null) {
            throw new GenericException(AdminErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        // 校验游戏角色
        List<GaServerRoleEntity> roles = gaServerRoleService.queryByUserId(ucUser.getId());
        if (CollectionUtils.isEmpty(roles)) {
            throw new GenericException(AdminErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        return ucUser.getInviteCode();
    }

    @Override
    public String inviteCode(String inviteNo) {
        LambdaQueryWrapper<SysKolEntity> queryWrap = new QueryWrapper<SysKolEntity>().lambda()
                .eq(SysKolEntity::getInviteNo, inviteNo)
                .eq(SysKolEntity::getStatus, WhetherEnum.YES.value());
        SysKolEntity one = getOne(queryWrap);
        if (one == null) {
            return null;
        }
        return one.getInviteCode();
    }
}

