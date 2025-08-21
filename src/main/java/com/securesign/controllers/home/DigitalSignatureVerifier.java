//package com.securesign.controllers.home;
//
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.security.*;
//import java.security.spec.*;
//import java.util.Base64;
//
//public class DigitalSignatureVerifier extends Application {
//
//    private PrivateKey privateKey;
//    private PublicKey publicKey;
//    private byte[] currentDocBytes;
//    private byte[] currentSignature;
//
//    private Label keyStatus = new Label("Keys: none");
//    private Label docStatus = new Label("Document: none");
//    private Label hashLabel = new Label("SHA-256: —");
//    private TextArea signatureArea = new TextArea();
//    private Label verifyLabel = new Label("Verification: —");
//
//    @Override
//    public void start(Stage stage) {
//        stage.setTitle("Digital Signature Verifier (RSA + SHA-256)");
//
//        Button genKeys = new Button("Generate RSA Key Pair");
//        Button savePub = new Button("Export Public Key (.pem)");
//        Button savePriv = new Button("Export Private Key (.pem)");
//        Button loadPub = new Button("Import Public Key (.pem)");
//        Button loadPriv = new Button("Import Private Key (.pem)");
//        HBox keyRow = new HBox(8, genKeys, savePub, savePriv, loadPub, loadPriv);
//
//        Button openDoc = new Button("Open Document");
//        Button signBtn = new Button("Sign with Private Key");
//        Button verifyBtn = new Button("Verify with Public Key");
//        HBox docRow = new HBox(8, openDoc, signBtn, verifyBtn);
//
//        signatureArea.setPromptText("Signature (Base64 will appear here after signing; paste here to verify an external signature)");
//        signatureArea.setWrapText(true);
//
//        VBox root = new VBox(10,
//                new Label("Keys & Document"),
//                keyRow, keyStatus,
//                docRow, docStatus, hashLabel,
//                new Label("Signature (Base64)"),
//                signatureArea,
//                verifyLabel
//        );
//        root.setPadding(new Insets(12));
//
//        // Handlers
//        genKeys.setOnAction(e -> {
//            try {
//                KeyPair kp = Crypto.generateRsaKeyPair(3072);
//                privateKey = kp.getPrivate();
//                publicKey = kp.getPublic();
//                keyStatus.setText("Keys: generated (RSA 3072)");
//                verifyLabel.setText("Verification: —");
//            } catch (Exception ex) {
//                alert("Key Generation Error", ex);
//            }
//        });
//
//        savePub.setOnAction(e -> {
//            if (publicKey == null) { info("No public key to export."); return; }
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Save Public Key");
//            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM", "*.pem"));
//            File f = fc.showSaveDialog(stage);
//            if (f != null) {
//                try { Files.writeString(f.toPath(), Crypto.exportPublicKeyPem(publicKey), StandardCharsets.US_ASCII); }
//                catch (Exception ex) { alert("Export Public Key Error", ex); }
//            }
//        });
//
//        savePriv.setOnAction(e -> {
//            if (privateKey == null) { info("No private key to export."); return; }
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Save Private Key");
//            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM", "*.pem"));
//            File f = fc.showSaveDialog(stage);
//            if (f != null) {
//                try { Files.writeString(f.toPath(), Crypto.exportPrivateKeyPem(privateKey), StandardCharsets.US_ASCII); }
//                catch (Exception ex) { alert("Export Private Key Error", ex); }
//            }
//        });
//
//        loadPub.setOnAction(e -> {
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Open Public Key (.pem)");
//            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM", "*.pem"));
//            File f = fc.showOpenDialog(stage);
//            if (f != null) {
//                try {
//                    publicKey = Crypto.importPublicKeyPem(Files.readString(f.toPath()));
//                    keyStatus.setText("Keys: public key loaded");
//                } catch (Exception ex) { alert("Import Public Key Error", ex); }
//            }
//        });
//
//        loadPriv.setOnAction(e -> {
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Open Private Key (.pem)");
//            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM", "*.pem"));
//            File f = fc.showOpenDialog(stage);
//            if (f != null) {
//                try {
//                    privateKey = Crypto.importPrivateKeyPem(Files.readString(f.toPath()));
//                    keyStatus.setText("Keys: private key loaded");
//                } catch (Exception ex) { alert("Import Private Key Error", ex); }
//            }
//        });
//
//        openDoc.setOnAction(e -> {
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Open Document (any file)");
//            File f = fc.showOpenDialog(stage);
//            if (f != null) {
//                try {
//                    currentDocBytes = Files.readAllBytes(f.toPath());
//                    String hashHex = Crypto.sha256Hex(currentDocBytes);
//                    docStatus.setText("Document: " + f.getName() + " (" + currentDocBytes.length + " bytes)");
//                    hashLabel.setText("SHA-256: " + hashHex);
//                    verifyLabel.setText("Verification: —");
//                } catch (Exception ex) { alert("Read Document Error", ex); }
//            }
//        });
//
//        signBtn.setOnAction(e -> {
//            if (privateKey == null) { info("Load or generate a private key first."); return; }
//            if (currentDocBytes == null) { info("Open a document to sign."); return; }
//            try {
//                currentSignature = Crypto.signSha256withRsa(currentDocBytes, privateKey);
//                String b64 = Base64.getEncoder().encodeToString(currentSignature);
//                signatureArea.setText(b64);
//                verifyLabel.setText("Verification: signature created.");
//            } catch (Exception ex) { alert("Signing Error", ex); }
//        });
//
//        verifyBtn.setOnAction(e -> {
//            if (publicKey == null) { info("Load or generate a public key first."); return; }
//            if (currentDocBytes == null) { info("Open a document to verify."); return; }
//            try {
//                byte[] sig = signatureArea.getText().isBlank() ? currentSignature :
//                        Base64.getDecoder().decode(signatureArea.getText().trim());
//                if (sig == null) { info("No signature available."); return; }
//                boolean ok = Crypto.verifySha256withRsa(currentDocBytes, sig, publicKey);
//                verifyLabel.setText("Verification: " + (ok ? "VALID ✅" : "INVALID ❌"));
//            } catch (Exception ex) { alert("Verification Error", ex); }
//        });
//
//        stage.setScene(new Scene(root, 820, 520));
//        stage.show();
//    }
//
//    private void alert(String title, Exception ex) {
//        ex.printStackTrace();
//        Alert a = new Alert(Alert.AlertType.ERROR, ex.getClass().getSimpleName() + ": " + ex.getMessage(), ButtonType.OK);
//        a.setHeaderText(title);
//        a.showAndWait();
//    }
//
//    private void info(String msg) {
//        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
//        a.setHeaderText(null);
//        a.showAndWait();
//    }
//
//    public static class Crypto {
//
//        static KeyPair generateRsaKeyPair(int bits) throws GeneralSecurityException {
//            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//            kpg.initialize(bits, SecureRandom.getInstanceStrong());
//            return kpg.generateKeyPair();
//        }
//
//        static byte[] signSha256withRsa(byte[] data, PrivateKey priv) throws GeneralSecurityException {
//            Signature s = Signature.getInstance("SHA256withRSA"); // PKCS#1 v1.5
//            s.initSign(priv);
//            s.update(data);
//            return s.sign();
//        }
//
//        static boolean verifySha256withRsa(byte[] data, byte[] sig, PublicKey pub) throws GeneralSecurityException {
//            Signature s = Signature.getInstance("SHA256withRSA");
//            s.initVerify(pub);
//            s.update(data);
//            return s.verify(sig);
//        }
//
//        static String sha256Hex(byte[] data) throws GeneralSecurityException {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hash = md.digest(data);
//            StringBuilder sb = new StringBuilder(2 * hash.length);
//            for (byte b : hash) sb.append(String.format("%02x", b));
//            return sb.toString();
//        }
//
//        // ----- PEM helpers (PKCS#8 for private, X.509 SubjectPublicKeyInfo for public)
//        static String exportPrivateKeyPem(PrivateKey priv) {
//            String b64 = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII))
//                    .encodeToString(priv.getEncoded());
//            return "-----BEGIN PRIVATE KEY-----\n" + b64 + "\n-----END PRIVATE KEY-----\n";
//        }
//
//        static String exportPublicKeyPem(PublicKey pub) {
//            String b64 = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII))
//                    .encodeToString(pub.getEncoded());
//            return "-----BEGIN PUBLIC KEY-----\n" + b64 + "\n-----END PUBLIC KEY-----\n";
//        }
//
//        static PrivateKey importPrivateKeyPem(String pem) throws GeneralSecurityException {
//            byte[] der = decodePem(pem, "PRIVATE KEY");
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            return kf.generatePrivate(new PKCS8EncodedKeySpec(der));
//        }
//
//        static PublicKey importPublicKeyPem(String pem) throws GeneralSecurityException {
//            byte[] der = decodePem(pem, "PUBLIC KEY");
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            return kf.generatePublic(new X509EncodedKeySpec(der));
//        }
//
//        private static byte[] decodePem(String pem, String type) {
//            String normalized = pem.replace("\r", "");
//            String header = "-----BEGIN " + type + "-----\n";
//            String footer = "\n-----END " + type + "-----";
//            int start = normalized.indexOf(header);
//            int end = normalized.indexOf(footer);
//            if (start < 0 || end < 0) throw new IllegalArgumentException("Invalid PEM: " + type);
//            String base64 = normalized.substring(start + header.length(), end);
//            return Base64.getMimeDecoder().decode(base64);
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//
