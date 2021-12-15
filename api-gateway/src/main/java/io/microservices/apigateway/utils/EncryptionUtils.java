package io.microservices.apigateway.utils;

import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class EncryptionUtils {

    public static PublicKey readPublicKey(String keyPath)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        final KeyFactory factory = KeyFactory.getInstance("RSA");
        try (Reader reader = new FileReader(keyPath);
             PemReader pemReader = new PemReader(reader)) {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(pemReader.readPemObject().getContent());
            return factory.generatePublic(spec);
        }
    }

}
