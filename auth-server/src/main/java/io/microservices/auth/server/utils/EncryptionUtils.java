package io.microservices.auth.server.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public final class EncryptionUtils {

    public static PrivateKey readPrivateKey(String keyPath)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Path path = Paths.get(keyPath);
        byte[] content = Files.readAllBytes(path);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(content);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(privateKeySpec);
    }

    private EncryptionUtils() {}
}
