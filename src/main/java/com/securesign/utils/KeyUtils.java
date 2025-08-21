package com.securesign.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KeyUtils {

    // Save Public Key in PEM format
    public static void savePublicKey(PublicKey publicKey, File file) throws IOException {
        savePemFile(file, "PUBLIC KEY", publicKey.getEncoded());
    }

    // Save Private Key in PEM format
    public static void savePrivateKey(PrivateKey privateKey, File file) throws IOException {
        savePemFile(file, "PRIVATE KEY", privateKey.getEncoded());
    }

    private static void savePemFile(File file, String type, byte[] encoded) throws IOException {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes())
                .encodeToString(encoded);
        String pem = "-----BEGIN " + type + "-----\n" + base64 + "\n-----END " + type + "-----";
        Files.writeString(file.toPath(), pem);
    }

    // ---------------- Loading Keys ----------------

    public static PrivateKey loadPrivateKey(File file) throws Exception {
        String pem = Files.readString(file.toPath());
        String base64 = pem.replaceAll("-----\\w+ PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(base64);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    public static PublicKey loadPublicKey(File file) throws Exception {
        String pem = Files.readString(file.toPath());
        String base64 = pem.replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(base64);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }
}
