package com.seeds.common.crypto;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/21
 */

import com.seeds.common.exception.BadSigException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Generate HMAC-based signature with secret key and other properties
 */
@Slf4j
public class SignatureHandler {
    private static final char DELIMITER = '\n';


    public static String generate(String secretKey, String method, String timestamp, String path) {
        return generate(secretKey, method, timestamp, path, null);
    }

    public static String generate(String secretKey, String method, String timestamp, String path, byte[] content) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(method.toUpperCase().getBytes(StandardCharsets.UTF_8));
            out.write(DELIMITER);
            out.write(timestamp.getBytes(StandardCharsets.UTF_8));
            out.write(DELIMITER);
            out.write(path.getBytes(StandardCharsets.UTF_8));
            if (content != null && content.length > 0) {
                out.write(DELIMITER);
                out.write(content);
            }
            Signer signer = new Signer(secretKey);
            String signRes = signer.createSignature(out.toByteArray());
            log.debug("generate sign: {}, {}, {}, {}, {}, res: {}", secretKey, method, timestamp, path, content, signRes);
            return signRes;
        } catch (IOException e) {
            throw new RuntimeException("Exception while generating signature", e);
        }
    }

    public static void check(String signature, String secretKey, String method, String timestamp, String path) {
        check(signature, secretKey, method, timestamp, path, null);
    }

    public static void check(String signature, String secretKey, String method, String timestamp, String path, byte[] content) {
        String signatureGen = generate(secretKey, method, timestamp, path, content);
        if (!signature.equals(signatureGen)) {
            throw new BadSigException("Bad Signature");
        }
    }
}
