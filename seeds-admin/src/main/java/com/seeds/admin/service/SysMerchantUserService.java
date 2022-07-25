package com.seeds.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysMerchantUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统商家与用户关联
 *
 * @author hang.yu
 * @date 2022/7/20
 */
public interface SysMerchantUserService extends IService<SysMerchantUserEntity> {

    /**
     * 保存
     * @param merchantId  商家ID
     * @param userIds  用户ID列表
     */
    void add(Long merchantId, Collection<Long> userIds);

    /**
     * 通过商家id查询系统商家与用户关联
     * @param  merchantId  商家ID
     * @return  系统商家与用户关联
     */
    List<SysMerchantUserEntity> queryByMerchantId(Long merchantId);

    /**
     * 通过商家id列表查询系统商家与用户关联
     * @param  merchantIds  商家ID列表
     * @return  系统商家与用户关联
     */
    List<SysMerchantUserEntity> queryByMerchantIds(Collection<Long> merchantIds);

    /**
     * 通过商家id查询与系统商家关联的用户id列表
     * @param  merchantId  商家id
     * @return  系统商家关联的用户id列表
     */
    Set<Long> queryUserIdByMerchantId(Long merchantId);

    /**
     * 通过商家id列表查询系统商家与用户关联
     * @param  merchantIds  商家ID列表
     * @return  系统商家与用户关联
     */
    Map<Long, Set<Long>> queryMapByMerchantIds(Collection<Long> merchantIds);

    /**
     * 批量删除系统商家与用户关联
     * @param  merchantUsers  商家与用户关联信息
     */
    void batchDelete(List<SysMerchantUserEntity> merchantUsers);

    /**
     * 通过用户id列表查询商家与用户关联
     * @param  userIds 用户id列表
     * @return 商家与用户的关联
     */
    List<SysMerchantUserEntity> queryByUserIds(Collection<Long> userIds);

    /**
     * 通过用户id查询商家与用户关联
     * @param  userId 用户id
     * @return 商家与用户的关联
     */
    List<SysMerchantUserEntity> queryByUserId(Long userId);

    /**
     * 通过用户id查询所属商家下所有用户id
     * @param  userId 用户id
     * @return 所属商家下所有用户id
     */
    Set<Long> queryAllMerchantUserByUserId(Long userId);

}
