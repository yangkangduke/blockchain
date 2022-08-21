package com.seeds.chain.service;

import java.math.BigInteger;

public interface GameItemsService {
    String getUri(BigInteger tokenId);

    BigInteger mintNft(String uri);

    boolean updateNftAttribute(BigInteger tokenId, String newUri);

    boolean burnNft(String tokenId);
}
