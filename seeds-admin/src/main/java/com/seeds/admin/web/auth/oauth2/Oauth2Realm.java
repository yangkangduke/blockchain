package com.seeds.admin.web.auth.oauth2;

import com.seeds.admin.web.auth.service.AdminShiroService;
import com.seeds.common.web.context.UserContext;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 权限
 *
 * @author hang.yu
 * @date 2022/7/15
 */
@Component
public class Oauth2Realm extends AuthorizingRealm {

    @Autowired
    private AdminShiroService adminShiroService;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取登录用户
        Long userId = UserContext.getCurrentAdminUserId();
        // 用户权限列表
        Set<String> permsSet = adminShiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }

}