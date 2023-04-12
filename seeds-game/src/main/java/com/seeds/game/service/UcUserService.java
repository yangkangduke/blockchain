package com.seeds.game.service;


/**
 * <p>
 * UC用户 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-30
 */
public interface UcUserService {

    /**
     * 归属人校验
     * @param owner 归属人地址
     */
    void ownerValidation(String owner);

}