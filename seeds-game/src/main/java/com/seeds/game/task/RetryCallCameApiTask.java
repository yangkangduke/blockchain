package com.seeds.game.task;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.seeds.game.entity.CallGameApiErrorLogEntity;
import com.seeds.game.service.CallGameApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: he.wei
 * @date 2023/5/29
 */
@Component
@Slf4j
public class RetryCallCameApiTask {

    @Autowired
    private CallGameApiLogService gameApiLogService;

    @Scheduled(cron = "3 * * * * ?")
    public void retryCall() {
        log.info("定时任务，请求调用游戏方超时的接口------>");
        List<CallGameApiErrorLogEntity> errorRecord = gameApiLogService.list();
        List<CallGameApiErrorLogEntity> retryList = errorRecord.stream()
                .filter(p -> p.getMsg().contains("timed out") && p.getRetryNum() < p.getMaxRetryNum())
                .collect(Collectors.toList());
        retryList.forEach(p -> {
            this.postRequest(p);
        });
    }

    private void postRequest(CallGameApiErrorLogEntity errorLogEntity) {
        errorLogEntity.setRetryNum(errorLogEntity.getRetryNum() + 1);
        gameApiLogService.updateById(errorLogEntity);
        try {
            HttpResponse response = HttpRequest.post(errorLogEntity.getUrl())
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(errorLogEntity.getParams())
                    .execute();
            log.info("重试调用游戏方请求，URL：{},result:{}", errorLogEntity.getUrl(), response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String ret = jsonObject.getString("ret");
            if ("ok".equalsIgnoreCase(ret) || (judgeByToCharArray(ret) && Integer.parseInt(ret) == 0)) {
                gameApiLogService.removeById(errorLogEntity.getId());
            }
        } catch (HttpException e) {
            log.info("重试调用游戏方请求错误，URL：{},  result:{}", errorLogEntity.getUrl(), e.getMessage());
        }
    }

    private boolean judgeByToCharArray(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }
}