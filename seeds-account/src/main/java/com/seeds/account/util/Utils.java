package com.seeds.account.util;

import com.google.common.collect.ImmutableSet;
import com.seeds.account.ex.*;
import com.seeds.account.exception.OrderException;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ErrorCode;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * 包装restful接口的异常
 *
 * @author milo
 */
@Slf4j
public class Utils {

    private final static Set<String> LOCAL_IP_SET
            = ImmutableSet.of("127.0.0.1", "::1", "0:0:0:0:0:0:0:1", "[0:0:0:0:0:0:0:1]", "");

    public static <T> GenericDto<T> returnFromException(Throwable t) {
        GenericDto<T> result = returnFromException0(t);

        // 把错误报告给监控系统
        Metrics.counter("error", Tags.of(
                Tag.of("app_name", "kine-account-service"),
                Tag.of("module", "restful"))
        ).increment();

        if (t instanceof OrderException) {
            log.warn("returnFromException result={} exception={} ", result, t.getClass().getSimpleName() + " " + t.getMessage(), t);
        } else {
            log.error("returnFromException result={} exception={} ", result, t.getClass().getSimpleName() + " " + t.getMessage(), t);
        }

        return result;
    }

    private static <T> GenericDto<T> returnFromException0(Throwable t) {
        if (t instanceof IllegalArgumentException) {
            return GenericDto.<T>failure(ErrorCode.ACCOUNT_VALIDATION_ERROR.getCode(), t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName());
        }

        if (t instanceof PriceNotUpdateException
                || t instanceof MissingElementException
                || t instanceof ActionDeniedException
                || t instanceof DataInconsistencyException) {
            return GenericDto.<T>failure(ErrorCode.ACCOUNT_BUSINESS_ERROR.getCode(), t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName());
        }

        if (t instanceof AccountException) {
            return GenericDto.<T>failure(((AccountException) t).getErrorCode().getCode(), t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName());
        }

        if (t instanceof OrderException) {
            return GenericDto.<T>failure(((OrderException) t).getErrorCode().getCode(), t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName());
        }

        return GenericDto.<T>failure(ErrorCode.ACCOUNT_UNKNOWN_ERROR.getCode(), ErrorCode.ACCOUNT_UNKNOWN_ERROR.getDescEn());
    }

    public static void check(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new AccountException(errorCode);
        }
    }

}
