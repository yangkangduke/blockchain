//package com.seeds.account.service;
//
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.seeds.account.utils.MetricsGaugeUtils;
//import io.micrometer.core.instrument.Tags;
//import lombok.Builder;
//import lombok.Data;
//import lombok.ToString;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RMap;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.Serializable;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@Slf4j
//public class TaskMonitorService {
//
//    /**
//     * Key: taskName_totalShard_shardId
//     * Value: TaskStatus
//     */
//    private static final Map<String, TaskRunResult> tasks = new ConcurrentHashMap<>();
//
//    String REDIS_KEY = "seeds:account:job:taskRunResultMap";
//
//    @Autowired
//    RedissonClient redissonClient;
//
//    public synchronized void save(String key, TaskMonitorService.TaskRunResult taskRunResult) {
//        tasks.put(key, taskRunResult);
//        RMap<String, TaskMonitorService.TaskRunResult> map = redissonClient.getMap(REDIS_KEY);
//        map.put(key, taskRunResult);
//    }
//
//    public void success(ShardingContext shardingContext) {
//        String key = getKey(shardingContext);
//        TaskRunResult taskRunResult = TaskRunResult.builder()
//                .taskId(shardingContext.getTaskId())
//                .jobName(shardingContext.getJobName())
//                .shardId(shardingContext.getShardingItem())
//                .totalShard(shardingContext.getShardingTotalCount())
//                .status(TaskRunResult.SUCCESS)
//                .timestamp(System.currentTimeMillis())
//                .build();
//        save(key, taskRunResult);
//    }
//
//    private String getKey(ShardingContext shardingContext) {
//        return String.format("%s_%s_%s", shardingContext.getJobName(), shardingContext.getShardingTotalCount(), shardingContext.getShardingItem());
//    }
//
//    public void error(ShardingContext shardingContext) {
//        String key = getKey(shardingContext);
//        TaskRunResult taskRunResult = TaskRunResult.builder()
//                .taskId(shardingContext.getTaskId())
//                .jobName(shardingContext.getJobName())
//                .shardId(shardingContext.getShardingItem())
//                .totalShard(shardingContext.getShardingTotalCount())
//                .status(TaskRunResult.ERROR)
//                .timestamp(System.currentTimeMillis())
//                .build();
//
//        save(key, taskRunResult);
//    }
//
//
//    /**
//     * 定时将task运行结果打点prometheus.
//     */
//    @Scheduled(initialDelay = 10, fixedRate = 1000 * 10)
//    public void metrics() {
//
//        syncWithRedis();
//
//        tasks.entrySet().stream().forEach(entry -> {
//            MetricsGaugeUtils.gauge("account.job.task.timestamp", Tags.of(
//                    "jobName", entry.getValue().getJobName()
//                    , "totalShard", String.valueOf(entry.getValue().getTotalShard())
//                    , "shardId", String.valueOf(entry.getValue().getShardId())
//                    , "status", entry.getValue().getStatus()
//            ), System.currentTimeMillis() - entry.getValue().getTimestamp());
//
////            log.info("job metrics: {}", entry.getValue().toString());
//        });
//    }
//
//    /**
//     * 并且保存到Redis。  10分钟保存一次
//     */
//    public synchronized void syncWithRedis() {
//
//        RMap<String, TaskMonitorService.TaskRunResult> map = redissonClient.getMap(REDIS_KEY);
//
//        // Redis同步到本地
//        map.entrySet().forEach(entry -> {
//            try {
//                // 如果本地的result比较新，则更新到redis
//
//                if (tasks.get(entry.getKey()) == null) {
//                    tasks.put(entry.getKey(), entry.getValue());
//                } else {
//                    long redisTimestamp = entry.getValue().getTimestamp();
//                    long localTimestamp = tasks.get(entry.getKey()).getTimestamp();
//                    if (redisTimestamp > localTimestamp) {
//                        tasks.put(entry.getKey(), entry.getValue());
//                    }
//                }
//
//            } catch (Exception e) {
//
//            }
//        });
//
//    }
//
//
//    @Data
//    @Builder
//    @ToString
//    static class TaskRunResult implements Serializable {
//        private static final long serialVersionUID = -1L;
//
//        public static final String SUCCESS = "SUCCESS";
//        public static final String ERROR = "ERROR";
//
//        String taskId;
//        String jobName;
//        int totalShard;
//        int shardId;
//        Long timestamp;
//        /**
//         * success
//         * error
//         */
//        String status;
//    }
//}
