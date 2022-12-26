package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysGameApiEntity;

import java.util.List;


/**
 * 游戏api
 *
 * @author hang.yu
 * @date 2022/10/10
 */
public interface SysGameApiService extends IService<SysGameApiEntity> {

    String queryApiByGameAndType(Long gameId, Integer type);

    SysGameApiEntity queryByGameAndType(Long gameId, Integer type);

    List<String> queryUrlByGameAndType(Long gameId, Integer type);

}
