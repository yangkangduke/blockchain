package com.seeds.wallet.tron;

import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.abi.datatypes.Bool;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.generated.Uint256;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.transaction.TransactionBuilder;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;

import java.util.Arrays;
import java.util.Base64;

/**
 * @author milo
 *
 *
 *
 */
public class TronUtils {
    /**
     * TRX 的最小单位是 SUN, 1_TRX = 1_000_000_SUN
     *
     */
    public final static int TRX_DECIMALS = 6;

    /**
     * 生成发送TRX的交易
     * @param client
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @return
     * @throws Exception
     */
    public static Response.TransactionExtention createTrxTransfer(ApiWrapper client, String fromAddress, String toAddress, long amount) throws Exception {
        return client.transfer(fromAddress, toAddress, amount);
    }

    public static String createTrxTransferBase64(ApiWrapper client, String fromAddress, String toAddress, long amount) throws Exception {
        Response.TransactionExtention txnExtension =  createTrxTransfer(client, fromAddress, toAddress, amount);
        return Base64.getEncoder().encodeToString(txnExtension.toByteArray());
    }

    /**
     * 生成发送TRC20币的交易
     * @param client
     * @param fromAddress
     * @param toAddress
     * @param contractAddress
     * @param amount
     * @return
     * @throws Exception
     */
    public static Chain.Transaction createTrc20Transfer(ApiWrapper client, String fromAddress, String toAddress, String contractAddress, long amount) throws Exception {
        Function trc20Transfer = new Function("transfer",
                Arrays.asList(new Address(toAddress), new Uint256(amount)),
                Arrays.asList(new TypeReference<Bool>() {}));

        //the params are: owner address, contract address, function
        TransactionBuilder builder = client.triggerCall(fromAddress, contractAddress, trc20Transfer); //JST
        //set extra params
        builder.setFeeLimit(100000000L);
        return builder.build();
    };


    public static String createTrc20TransferBase64(ApiWrapper client, String fromAddress, String toAddress, String contractAddress, long amount) throws Exception {
        Chain.Transaction txn = createTrc20Transfer(client, fromAddress, toAddress, contractAddress, amount);
        return Base64.getEncoder().encodeToString(txn.toByteArray());
    }
}
