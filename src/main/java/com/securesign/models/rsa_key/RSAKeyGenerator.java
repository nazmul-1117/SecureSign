package com.securesign.models.rsa_key;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class RSAKeyGenerator {

    private final String algorithm;
    private final int keyLength;
    private KeyPair keyPair;

    public RSAKeyGenerator(String algorithm, String keyLength) {
        this.algorithm = algorithm;
        this.keyLength = Integer.parseInt(keyLength);
    }

    // 🔑 Generate RSA keys
    public boolean generateKey(ActionEvent e) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keyLength);
            keyPair = generator.generateKeyPair();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ----- Getters -----
    public String getPublicKeyString() {
        if (keyPair == null) return null;
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKeyString() {
        if (keyPair == null) return null;
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    public PublicKey getPublicKey() { return keyPair != null ? keyPair.getPublic() : null; }
    public PrivateKey getPrivateKey() { return keyPair != null ? keyPair.getPrivate() : null; }

    // ----- Save -----
    public void savePublicKey(File file) throws IOException {
        savePemFile(file, "PUBLIC KEY", keyPair.getPublic().getEncoded());
    }

    public void savePrivateKey(File file) throws IOException {
        savePemFile(file, "PRIVATE KEY", keyPair.getPrivate().getEncoded());
    }

    // ----- Load -----
    public void loadPublicKey(File file) throws Exception {
        byte[] bytes = decodePem(file, "PUBLIC KEY");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        PublicKey pub = KeyFactory.getInstance("RSA").generatePublic(spec);
        keyPair = new KeyPair(pub, (keyPair != null ? keyPair.getPrivate() : null));
    }

    public void loadPrivateKey(File file) throws Exception {
        byte[] bytes = decodePem(file, "PRIVATE KEY");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        PrivateKey priv = KeyFactory.getInstance("RSA").generatePrivate(spec);
        keyPair = new KeyPair((keyPair != null ? keyPair.getPublic() : null), priv);
    }

    // ----- Helpers -----
    private void savePemFile(File file, String type, byte[] encoded) throws IOException {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded);
        String pem = "-----BEGIN " + type + "-----\n" + base64 + "\n-----END " + type + "-----";
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(pem);
        }
    }

    private byte[] decodePem(File file, String type) throws IOException {
        String pem = Files.readString(file.toPath());
        String base64 = pem.replaceAll("-----BEGIN " + type + "-----", "")
                .replaceAll("-----END " + type + "-----", "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(base64);
    }
}
