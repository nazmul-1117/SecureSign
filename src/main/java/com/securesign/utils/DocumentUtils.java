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

    /**
     * Compute SHA-256 hash of a file and return as hex string.
     */
    public static String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        return getFileHash(file, "SHA-256");
    }

    /**
     * Compute hash of a file with configurable algorithm.
     */
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

    /**
     * Compute SHA-256 hash of raw byte array (e.g., public key fingerprint).
     */
    public static String hashBytes(byte[] data) throws NoSuchAlgorithmException {
        return hashBytes(data, "SHA-256");
    }

    /**
     * Compute hash of raw byte array with configurable algorithm.
     */
    public static String hashBytes(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(data);
        return HexFormat.of().formatHex(digest.digest());
    }

    /**
     * Save a given hash string to a .hash file.
     */
    public static File saveHashToFile(String hash, File originalFile) throws IOException {
        File hashFile = new File(originalFile.getAbsolutePath() + ".hash");
        Files.writeString(hashFile.toPath(), hash);
        return hashFile;
    }

    /**
     * Load a hash string from a .hash file.
     */
    public static String loadHashFromFile(File hashFile) throws IOException {
        return Files.readString(hashFile.toPath()).trim();
    }

    /**
     * Verify file's SHA-256 hash matches expected.
     */
    public static boolean verifyFileHash(File file, String expectedHash) throws IOException, NoSuchAlgorithmException {
        String actualHash = getFileHash(file);
        return actualHash.equalsIgnoreCase(expectedHash);
    }

    /**
     * Generate a simple verification report.
     */
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
