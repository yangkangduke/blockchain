package com.seeds.account.service;

public interface IChainActionService {

    /**
     * 检查空闲地址的数量，不够的时候就创建新的
     *
     * @throws Exception
     */
    void scanAndCreateAddresses() throws Exception;
}
