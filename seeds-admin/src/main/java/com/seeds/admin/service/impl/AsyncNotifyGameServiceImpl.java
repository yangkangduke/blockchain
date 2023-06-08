package com.seeds.admin.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.game.SkinNftMintSuccessDto;
import com.seeds.admin.service.CallGameApiLogService;
import com.seeds.admin.service.IAsyncNotifyGameService;
import com.seeds.admin.service.SysGameApiService;
import com.seeds.common.enums.ApiType;
import com.seeds.game.entity.CallGameApiErrorLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/4/16
 */
@Service
@Slf4j
public class AsyncNotifyGameServiceImpl implements IAsyncNotifyGameService {
    @Autowired
    private SysGameApiService gameApiService;

    @Autowired
    private CallGameApiLogService callGameApiLogService;

    @Override
    @Async
    public void skinMintSuccess(List<SkinNftMintSuccessDto> dtos) {

        List<String> url = gameApiService.queryUrlByGameAndType(1L, ApiType.SEND_TOKEN_ADDRESS.getCode());
        String notifyUrl = url.get(0);
        String param = JSONUtil.toJsonStr(dtos);
        log.info("调用游戏方接口 通知皮肤nft mint成功, url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(notifyUrl)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("调用游戏方接口 通知皮肤nft mint成功,接口返回result:{}", response.body());
        } catch (Exception e) {
            errorLog(notifyUrl, param, e.getMessage());
            log.error("调用游戏方接口 通知皮肤nft mint失败，message：{}", e.getMessage());
        }
    }

    private void errorLog(String url, String params, String msg) {
        CallGameApiErrorLogEntity errorLog = new CallGameApiErrorLogEntity();
        errorLog.setCallTime(System.currentTimeMillis());
        errorLog.setUrl(url);
        errorLog.setMethod(HttpMethod.POST.name());
        errorLog.setParams(params);
        errorLog.setMsg(msg);
        callGameApiLogService.save(errorLog);
    }
}
