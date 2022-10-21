package com.seeds.admin.config;

import com.seeds.chain.config.SmartContractConfig;
import com.seeds.chain.contracts.GameItems;
import lombok.extern.slf4j.Slf4j;
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
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 合约配置
 *
 * @author hang.yu
 * @date 2022/9/15
 */
@Configuration
@Slf4j
public class ContractConfig {

    @Autowired
    private SmartContractConfig smartContractConfig;

    //监听这里才用每次都生成一个新的对象，因为同时监听多个事件不能使用同一个实例
    @Bean
    @Scope("prototype")
    @Autowired
    public EthFilter filter(Web3j  web3j) throws IOException {
        //获取启动时监听的区块
        Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
        BigInteger block = request.send().getBlockNumber();
        return new EthFilter(DefaultBlockParameter.valueOf(block),
                DefaultBlockParameterName.LATEST, smartContractConfig.getGameAddress());
    }

    @Bean
    @Scope("prototype")
    public Web3j web3j() {
        return Web3j.build(new HttpService(smartContractConfig.getRpcProviderUrl()));
    }

    @Bean
    @Autowired
    public GameItems gameItems(Web3j web3j) throws IOException {
        GameItems contract;
        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("连接智能合约，clientVersion= {}", clientVersion);
            // 以某个用户的身份调用合约
            TransactionManager transactionManager = new ClientTransactionManager(web3j, smartContractConfig.getOwnerAddress());
            //加载智能合约
            contract = GameItems.load(smartContractConfig.getGameAddress(), web3j, transactionManager, new DefaultGasProvider());
            return contract;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
            //throw new RRException("连接智能合约异常");
        }
    }

}
