package com.seeds.admin.config;

import com.seeds.chain.config.SmartContractConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 合约配置
 *
 * @author hang.yu
 * @date 2022/9/15
 */
@Configuration
public class ContractConfig {

    @Autowired
    private SmartContractConfig smartContractConfig;

    //监听这里才用每次都生成一个新的对象，因为同时监听多个事件不能使用同一个实例
    @Bean
    @Scope("prototype")
    @Autowired
    public EthFilter filter(Web3j  web3j) throws IOException {
        //获取启动时监听的区块
        /*Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
        BigInteger block = request.send().getBlockNumber();
        return new EthFilter(DefaultBlockParameter.valueOf(block),
                DefaultBlockParameterName.LATEST, smartContractConfig.getGameAddress());*/
        return new EthFilter();
    }

    @Bean
    @Scope("prototype")
    public Web3j web3j() {
        return Web3j.build(new HttpService(smartContractConfig.getRpcProviderUrl()));
    }

}
