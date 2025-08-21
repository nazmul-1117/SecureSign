package com.securesign.utils;

import com.securesign.models.rsa_key.RSAKeyGenerator;
import javafx.event.ActionEvent;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class StoreKeys {

    private final String privateKeyBase64;
    private final String publicKeyBase64;

    public StoreKeys(String privateKey, String publicKey) {
        this.privateKeyBase64 = privateKey;
        this.publicKeyBase64 = publicKey;
    }

    public void saveKeys(RSAKeyGenerator keyGenerator, ActionEvent actionEvent) {
        try {
            File pubFile = new File("public.pem");
            File privFile = new File("private.pem");

            PublicKey publicKey = keyGenerator.getPublicKey();
            PrivateKey privateKey = keyGenerator.getPrivateKey();

            if (publicKey != null) {
                KeyUtils.savePublicKey(publicKey, pubFile);
            }
            if (privateKey != null) {
                KeyUtils.savePrivateKey(privateKey, privFile);
            }

            System.out.println("✅ Keys saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error saving keys: " + e.getMessage());
        }
    }
}
