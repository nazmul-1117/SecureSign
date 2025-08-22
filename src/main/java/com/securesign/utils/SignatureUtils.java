package com.securesign.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.util.Base64;

public class SignatureUtils {

    public static byte[] signFile(File file, PrivateKey privateKey) throws Exception {
        if (file == null || privateKey == null)
            throw new IllegalArgumentException("File or private key is null");

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(fileBytes);
        return signature.sign();
    }

    public static boolean verifyFile(File file, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        if (file == null || signatureBytes == null || publicKey == null) {
            throw new IllegalArgumentException("File, signature, or public key is null");
        }

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(fileBytes);

        return signature.verify(signatureBytes);
    }

    public static String signatureToBase64(byte[] signatureBytes) {
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static byte[] base64ToSignature(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public static void saveSignatureToFileBase64(byte[] signatureBytes, File file) throws IOException {
        if (signatureBytes == null || file == null)
            throw new IllegalArgumentException("Signature bytes or file is null");

        String base64 = signatureToBase64(signatureBytes);
        // Save as a single line to prevent line break issues
        Files.writeString(file.toPath(), base64.trim());
    }

    public static byte[] loadSignatureFromFileBase64(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("File is null");

        String base64 = Files.readString(file.toPath()).trim();
        return base64ToSignature(base64);
    }
}