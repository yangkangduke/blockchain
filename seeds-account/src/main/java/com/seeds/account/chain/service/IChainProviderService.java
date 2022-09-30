package com.seeds.account.chain.service;


import com.seeds.common.enums.Chain;

/**
 * @author milo
 */
public interface IChainProviderService {


    /**
     * register chain service provider
     * @param chain
     * @param provider
     */
    void registerProvider(Chain chain, IChainService provider);

    IChainService getServiceProvider(Chain chain);

}
