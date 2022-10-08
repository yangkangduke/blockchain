package com.seeds.admin.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 公共字段，自动填充值
 *
 * @author hang.yu
 * @date 2022/07/13
 */
@Component
@Slf4j
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = "createdAt";
    private final static String CREATOR = "createdBy";
    private final static String UPDATE_DATE = "updatedAt";
    private final static String UPDATER = "updatedBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        Long adminUserId;
        try {
            adminUserId = UserContext.getCurrentAdminUserId();
        } catch (Exception e) {
            log.info("getCurrentAdminUserId error" + e);
            adminUserId = 0L;
        }
        //创建者
        strictInsertFill(metaObject, CREATOR, Long.class, adminUserId);
        //更新者
        strictInsertFill(metaObject, UPDATER, Long.class, adminUserId);
        Date date = new Date();
        long time = date.getTime();


        //创建时间
        strictInsertFill(metaObject, CREATE_DATE, Long.class, time);

        //更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Long.class, time);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long adminUserId;
        try {
            adminUserId = UserContext.getCurrentAdminUserId();
        } catch (AuthException e) {
            adminUserId = 0L;
        }
        Date date = new Date();
        long time = date.getTime();

        //更新者
        strictUpdateFill(metaObject, UPDATER, Long.class, adminUserId);
        //更新时间
        strictUpdateFill(metaObject, UPDATE_DATE, Long.class, time);
    }
}