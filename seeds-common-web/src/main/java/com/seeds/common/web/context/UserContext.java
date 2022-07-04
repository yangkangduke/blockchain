package com.seeds.common.web.context;

import com.seeds.common.web.exception.AuthException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContext {

    private final ThreadLocal<Long> USER_ID_THREAD_LOCAL = new ThreadLocal<>();

    public void setCurrentUserId(Long userId) {
        USER_ID_THREAD_LOCAL.set(userId);
    }

    public Long getCurrentUserId() {
        Long userId = USER_ID_THREAD_LOCAL.get();
        if (userId == null) {
            throw new AuthException("No user id found from context");
        }
        return userId;
    }

    public void removeCurrentUserId() {
        USER_ID_THREAD_LOCAL.remove();
    }
}
