package com.seeds.admin.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.seeds.admin.annotation.DataFilter;
import com.seeds.admin.dto.DataScope;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.service.SysMerchantUserService;
import com.seeds.admin.service.SysUserService;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 数据过滤
 *
 * @author hang.yu
 * @date 2022/7/25
 */
@Slf4j
@Aspect
@Component
public class DataFilterInterceptor implements InnerInterceptor {

    @Autowired
    private SysMerchantUserService sysMerchantUserService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 通过ThreadLocal记录属性值
     */
    private final ThreadLocal<DataScope> threadLocal = new ThreadLocal<>();

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.seeds.admin.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) {
        try {
            // 获取登录用户
            Long userId = UserContext.getCurrentAdminUserId();
            SysUserEntity sysUser = sysUserService.queryById(userId);
            // 如果是超级管理员，则不进行数据过滤
            if(sysUser.getSuperAdmin() == WhetherEnum.YES.value()) {
                return;
            }
            // 获取所属商家下所有用户id
            Set<Long> userIds = sysMerchantUserService.queryAllMerchantUserByUserId(userId);
            // 进行数据过滤
            String sqlFilter = getSqlFilter(point, userIds);
            threadLocal.set(new DataScope(sqlFilter));
        } catch (Exception e) {
            throw new PermissionException("No permission, access not allowed");
        }
    }


    /**
     * 清空当前线程上次保存的权限信息
     */
    @After("dataFilterCut()")
    public void clearThreadLocal(){
        threadLocal.remove();
        log.debug("threadLocal.remove()");
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        DataScope scope = threadLocal.get();
        // 不进行数据过滤
        if(scope == null || StringUtils.isEmpty(scope.getSqlFilter())){
            return;
        }

        // 拼接新SQL
        String buildSql = getSelect(boundSql.getSql(), scope);

        // 重写SQL
        PluginUtils.mpBoundSql(boundSql).sql(buildSql);
    }

    private String getSelect(String buildSql, DataScope scope){
        try {
            Select select = (Select) CCJSqlParserUtil.parse(buildSql);
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            Expression expression = plainSelect.getWhere();
            if(expression == null){
                plainSelect.setWhere(new StringValue(scope.getSqlFilter()));
            }else{
                AndExpression andExpression =  new AndExpression(expression, new StringValue(scope.getSqlFilter()));
                plainSelect.setWhere(andExpression);
            }

            return select.toString().replaceAll("'", "");
        }catch (JSQLParserException e){
            return buildSql;
        }
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSqlFilter(JoinPoint point, Set<Long> userIds) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        DataFilter dataFilter = method.getAnnotation(DataFilter.class);

        // 获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        // 用户ID列表
        if (!CollectionUtils.isEmpty(userIds)) {
            sqlFilter.append(tableAlias).append(dataFilter.userId());
            sqlFilter.append(" in(").append(StringUtils.join(userIds, ",")).append(")");
        }

        sqlFilter.append(")");

        return sqlFilter.toString();
    }
}