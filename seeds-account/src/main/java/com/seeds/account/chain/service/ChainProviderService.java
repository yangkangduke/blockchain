package com.seeds.account.chain.service;


import com.seeds.common.enums.Chain;

/**
 * @author milo
 */
public interface ChainProviderService {


    /**
     * register chain service provider
     * @param chain
     * @param provider
     */
    void registerProvider(Chain chain, ChainService provider);

    ChainService getServiceProvider(Chain chain);

}
