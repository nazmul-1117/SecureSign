package com.securesign.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class DocumentUtils {

    private static final int BUFFER_SIZE = 8192; // 8KB chunks

    public static String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        return getFileHash(file, "SHA-256");
    }

    public static String getFileHash(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        try (InputStream is = Files.newInputStream(file.toPath())) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }

        return HexFormat.of().formatHex(digest.digest());
    }

    public static String hashBytes(byte[] data) throws NoSuchAlgorithmException {
        return hashBytes(data, "SHA-256");
    }

    public static String hashBytes(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(data);
        return HexFormat.of().formatHex(digest.digest());
    }

    public static File saveHashToFile(String hash, File originalFile) throws IOException {
        File hashFile = new File(originalFile.getAbsolutePath() + ".hash");
        Files.writeString(hashFile.toPath(), hash);
        return hashFile;
    }

    public static String loadHashFromFile(File hashFile) throws IOException {
        return Files.readString(hashFile.toPath()).trim();
    }

    public static boolean verifyFileHash(File file, String expectedHash) throws IOException, NoSuchAlgorithmException {
        String actualHash = getFileHash(file);
        return actualHash.equalsIgnoreCase(expectedHash);
    }

    public static String generateVerificationReport(File file, String expectedHash) throws Exception {
        String actualHash = getFileHash(file);
        boolean match = actualHash.equalsIgnoreCase(expectedHash);

        return """
               Verification Report
               -------------------
               File: %s
               Expected Hash: %s
               Actual Hash:   %s
               Result: %s
               """.formatted(file.getName(), expectedHash, actualHash, match ? "VALID ✅" : "INVALID ❌");
    }
}
