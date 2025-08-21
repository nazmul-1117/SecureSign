package com.securesign.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.util.Base64;

public class SignatureUtils {

    /** Sign a file with RSA private key and return the signature bytes */
    public static byte[] signFile(File file, PrivateKey privateKey) throws Exception {
        if (file == null || privateKey == null)
            throw new IllegalArgumentException("File or private key is null");

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(fileBytes);
        return signature.sign();
    }

    /** Verify a file with RSA public key and a given signature */
    public static boolean verifyFile(File file, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        if (file == null || signatureBytes == null || publicKey == null)
            throw new IllegalArgumentException("File, signature, or public key is null");

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(fileBytes);

        try {
            return signature.verify(signatureBytes);
        } catch (SignatureException e) {
            // If verification fails due to bad format, try Base64 decode fallback
            try {
                byte[] decoded = Base64.getDecoder().decode(signatureBytes);
                return signature.verify(decoded);
            } catch (IllegalArgumentException ignored) {
                throw e; // rethrow original if not Base64
            }
        }
    }


    /** Convert signature bytes to Base64 string */
    public static String signatureToBase64(byte[] signatureBytes) {
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    /** Convert Base64 string back to signature bytes */
    public static byte[] base64ToSignature(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    /** Save signature as Base64 text to a file */
    public static void saveSignatureToFileBase64(byte[] signatureBytes, File file) throws IOException {
        String base64 = signatureToBase64(signatureBytes);
        Files.writeString(file.toPath(), base64);
    }

    /** Load signature from Base64 text file */
    public static byte[] loadSignatureFromFileBase64(File file) throws IOException {
        String base64 = Files.readString(file.toPath());
        return base64ToSignature(base64);
    }

    /** Save raw signature bytes to a binary .sig file */
    public static void saveSignatureToFile(byte[] signatureBytes, File file) throws IOException {
        Files.write(file.toPath(), signatureBytes);
    }

    /** Load raw signature bytes from a binary .sig file */
    public static byte[] loadSignatureFromFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

}
