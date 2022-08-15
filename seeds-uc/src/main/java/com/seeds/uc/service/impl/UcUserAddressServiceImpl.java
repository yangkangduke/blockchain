package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.mapper.UcUserAddressMapper;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.model.UcUserAddress;
import com.seeds.uc.service.IUcUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 用户地址 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-29
 */
@Service
@Slf4j
@Transactional
public class UcUserAddressServiceImpl extends ServiceImpl<UcUserAddressMapper, UcUserAddress> implements IUcUserAddressService {


}
