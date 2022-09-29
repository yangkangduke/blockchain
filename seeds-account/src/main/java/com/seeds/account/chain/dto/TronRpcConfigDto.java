package com.seeds.account.chain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seeds.account.util.JsonUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TronRpcConfigDto {
    long chainId;

    /**
     * MAIN,
     * SHASTA,
     * NILE,
     * SELF, 自搭节点， 需要设置endpoint信息
     */
    String network;
    /**
     * When network is SELF, need setup gRpcEndpoint and gRpcEndpointSolidity
     */

    String endPoint;
    /**
     * When network is SELF, need setup gRpcEndpoint and gRpcEndpointSolidity
     */
    String endPointSolidity;
    /**
     * The api key, setup it when required
     */
    String auth;


    public static void main(String[] args) {
       TronRpcConfigDto rpc = new TronRpcConfigDto() ;


        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        System.out.println(gson.toJson(Arrays.asList(rpc)));


        String config = "[\n" +
                "  {\n" +
                "    \"chainId\": 0,\n" +
                "    \"network\": \"SELF\",\n" +
                "    \"endPoint\": \"54.238.2.63:50051\",\n" +
                "    \"endPointSolidity\": \"54.238.2.63:50052\",\n" +
                "    \"auth\": \"\"\n" +
                "  }\n" +
                "]";
        List<TronRpcConfigDto> web3RPCs = (config != null && config.length() > 0)
                ? JsonUtils.readValue(config, new TypeReference<List<TronRpcConfigDto>>() {
        })
                : Lists.newArrayList();

        System.out.println();
    }

    /**
     [
     {
     "chainId": 0,
     "network": null,
     "gRpcEndpoint": null,
     "gRpcEndpointSolidity": null,
     "auth": null
     }
     ]

     */
}
