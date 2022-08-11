package com.seeds.chain.service;

import java.math.BigInteger;

public interface GameItemsService {
    String getUri(BigInteger tokenId);

    boolean mintNft(String uri);

    boolean updateNftAttribute(BigInteger tokenId, String newUri);
}
