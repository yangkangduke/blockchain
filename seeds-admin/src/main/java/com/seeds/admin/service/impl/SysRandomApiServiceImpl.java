package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysRandomCodeEntity;
import com.seeds.admin.mapper.SysRandomCodeMapper;
import com.seeds.admin.service.SysRandomCodeService;
import org.springframework.stereotype.Service;


/**
 * 随机数
 *
 * @author hang.yu
 * @date 2022/10/10
 */
@Service
public class SysRandomApiServiceImpl extends ServiceImpl<SysRandomCodeMapper, SysRandomCodeEntity> implements SysRandomCodeService {

}

