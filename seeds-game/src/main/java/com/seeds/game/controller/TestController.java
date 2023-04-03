package com.seeds.game.controller;

import io.ipfs.api.IPFS;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author: hewei
 * @date 2023/4/2
 */
public class TestController {
    public static void main(String[] args) throws IOException {
//        String ipfsApiUrl = "https://cloudflare-ipfs.com/"; // IPFS API URL
////        String ipfsApiUrl = "/ip4/127.0.0.1/tcp/5001"; // IPFS API URL
//        IPFS ipfs = new IPFS(ipfsApiUrl);
//        URL fileUrl = new URL("https://www.theseeds.io/api/v1/uc/public/file/seeds/dbc16db8c1224a108ece8fc4eddba1aa.png");
//
//        // Add file to IPFS
//        Path filePath = Paths.get("1.png");
//        Files.copy(fileUrl.openStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        NamedStreamable file = new NamedStreamable.FileWrapper(filePath.toFile());
//        Multihash fileHash = ipfs.add(file).get(0).hash;
//        System.out.println("File added to IPFS: " + fileHash);
//
//        // publish file to IPNS
//        String ipnsName = "seeds-nft"; // IPNS name
//        ipfs.name.publish(fileHash, java.util.Optional.of(ipnsName));
//        System.out.println("File published to IPNS: " + ipnsName);

    }
}
