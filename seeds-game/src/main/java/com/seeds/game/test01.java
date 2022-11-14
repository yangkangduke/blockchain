package com.seeds.game;

import cn.hutool.crypto.SecureUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test01 {
    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        params.put("accessKey", "11111111");
        params.put("timestamp", 1676558488853L);

        params.put("nftId", 47);
        params.put("price", 23);
        params.put("unit", "USDT");

        String sign = getSign(params, "1bbd886460827015e5d605ed44252251");
        System.out.println(sign);
    }

    /**
     * 计算签名
     * @param params 入参
     * @return 签名
     */
    public static String getSign(Map<String, Object> params, String secretKey) {
        // 参数进行字典排序
        String sortStr = getFormatParams(params);
        // 将密钥key拼接在字典排序后的参数字符串中,得到待签名字符串。
        sortStr += "secretKey=" + secretKey;
        // 使用md5算法加密待加密字符串并转为大写即为sign
        return SecureUtil.md5(sortStr).toUpperCase();
    }

    private static String getFormatParams(Map<String, Object> params) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(params.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        StringBuilder ret = new StringBuilder();
        for (Map.Entry<String, Object> entry : infoIds) {
            ret.append(entry.getKey());
            ret.append("=");
            ret.append(entry.getValue());
            ret.append("&");
        }
        return ret.toString();
    }
}

