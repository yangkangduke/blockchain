package com.seeds.admin.listener;

import com.seeds.admin.entity.SysTransferRecordEntity;
import com.seeds.admin.service.SysTransferRecordService;
import com.seeds.chain.contracts.GameItems;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * 合约服务监听器
 *
 * @author hang.yu
 * @date 2022/9/15
 */
@Slf4j
@Component
public class ContractServiceRunner implements ApplicationRunner {

    @Autowired
    private Web3j web3j;

    //如果多个监听，必须要注入新的过滤器
    @Autowired
    private EthFilter ethFilter;

    @Autowired
    private SysTransferRecordService sysTransferRecordService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        uploadProAuth();
        log.info("This will be execute when the project was started!");
    }

    /**
     * 收到上链事件
     */
    public void uploadProAuth() {
        Event event = new Event(GameItems.FUNC_MINTNEWNFT,
                Arrays.asList(new TypeReference<Address>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Uint256>() {}));
        ethFilter.addSingleTopic(EventEncoder.encode(event));

        log.info("Start contract listener mintNewNft");

        Disposable subscribe = web3j.ethLogFlowable(ethFilter).subscribe(p -> {
            BigInteger blockNumber = p.getBlockNumber();
            log.info("收到合约的mintNewNft事件, blockNumber={}", blockNumber);
            // 持久化监听到的上链transfer事件
            List<String> topics = p.getTopics();
            String fromAddress = topics.get(1);
            String toAddress = topics.get(2);
            String value = p.getData();
            String timestamp = "";

            try {
                EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(p.getBlockNumber()), false).send();
                timestamp = String.valueOf(ethBlock.getBlock().getTimestamp());
            } catch (IOException e) {
                log.warn("Block timestamp get failure,block number is {}", blockNumber);
            }
            SysTransferRecordEntity record = new SysTransferRecordEntity();
            record.setFromAddress("0x" + fromAddress.substring(26));
            record.setToAddress("0x" + toAddress.substring(26));
            record.setValue(new BigDecimal(new BigInteger(value.substring(2), 16)).divide(BigDecimal.valueOf(1000000000000000000.0), 18, RoundingMode.HALF_EVEN));
            record.setTimestamp(timestamp);
            record.setBlockNum(blockNumber.longValue());
            record.setTransHash(p.getTransactionHash());
            sysTransferRecordService.save(record);
        }, Throwable::printStackTrace);
        // subscription.unsubscribe();
    }
}
