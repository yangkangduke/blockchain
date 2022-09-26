package com.seeds.wallet.providers;

import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.SignedMessageDto;

import java.util.List;

public interface WalletChainProvider {
    /**
     * generate new wallet file for given password + userId
     * @param chain
     * @return the public address of the generated wallet
     */
    String createNewWallet(int chain) throws Exception;

    /**
     * sign given rawTransaction by wallet of given password + userId + address
     *
     * @param chain
     * @param chainId
     * @param rawTransaction
     * @return the signed message
     */
    String signRawTransaction(int chain, long chainId, RawTransactionDto rawTransaction, String address) throws Exception;

    /**
     * sign given message by wallet of a given address
     *
     * @param chain
     * @param messageToSign the encoded message produced by encoder.encodeParameters.
     * @param address       the address to sign the message
     * @return
     */
    SignedMessageDto signMessage(int chain, String messageToSign, String address) throws Exception;

    /**
     * sign multiple messages by wallet of a given address
     * @param chain
     * @param messageToSign the encoded message produced by encoder.encodeParameters.
     * @param address       the address to sign the message
     * @return
     */
    List<SignedMessageDto> signMessages(int chain, List<String> messageToSign, String address) throws Exception;
}
