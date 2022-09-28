package com.seeds.common.web.util;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 签名工具类
 *
 * @author hang.yu
 * @date 2022/9/27
 **/
public class SignatureUtils {

    /**
     * 验签
     *
     * @param body     请求参数
     * @return result
     */
    public static boolean validation(JSONObject body) {
        // 拿出请求签名
        String sign = body.getString("signature");
        body.remove("signature");
        // 根据APPID查询的密钥进行重签
        String newSign = getSign(body);

        // 校验签名
        return StringUtils.equals(newSign, sign);
    }

    /**
     * 计算签名
     *
     * @param params 入参
     * @return 签名
     */
    public static String getSign(JSONObject params) {
        // todo 获取密钥
        String secretKey = "";
        // 参数进行字典排序
        String sortStr = getFormatParams(params);
        // 将密钥key拼接在字典排序后的参数字符串中,得到待签名字符串。
        sortStr += "secretKey=" + secretKey;
        // 使用md5算法加密待加密字符串并转为大写即为sign
        return SecureUtil.md5(sortStr).toUpperCase();
    }

    /**
     * 参数字典排序
     *
     * @param params 入参
     * @return 排序后的参数
     */
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
