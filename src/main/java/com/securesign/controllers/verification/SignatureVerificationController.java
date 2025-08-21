package com.securesign.controllers.verification;

import com.securesign.utils.DocumentUtils;
import com.securesign.utils.KeyUtils;
import com.securesign.utils.SignatureUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.security.PublicKey;

public class SignatureVerificationController {

    public Label docNameLabel;
    public Label keyNameLabel;
    public Label resultLabel;
    public TextArea signatureTextArea;

    private File selectedDocument;
    private PublicKey publicKey;
    private byte[] signatureBytes;

    private File lastDirectory = new File(System.getProperty("user.home"));

    /** Open document */
    public void openDocument(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Document");
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        selectedDocument = chooser.showOpenDialog(null);
        if (selectedDocument != null) {
            docNameLabel.setText(selectedDocument.getName());
            resultLabel.setText("");
            lastDirectory = selectedDocument.getParentFile();
        }
    }

    /** Load public key */
    public void loadPublicKey(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Public Key (PEM)");
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        File keyFile = chooser.showOpenDialog(null);
        if (keyFile != null) {
            try {
                publicKey = KeyUtils.loadPublicKey(keyFile);
                keyNameLabel.setText(keyFile.getName());
                resultLabel.setText("Public key loaded.");
                lastDirectory = keyFile.getParentFile();
            } catch (Exception ex) {
                resultLabel.setText("Error loading public key: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /** Load signature file */
    public void loadSignatureFile(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Signature File (.sig)");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Signature Files", "*.sig"));
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        File sigFile = chooser.showOpenDialog(null);
        if (sigFile != null) {
            try {
                signatureBytes = SignatureUtils.loadSignatureFromFile(sigFile);
                signatureTextArea.setText(SignatureUtils.signatureToBase64(signatureBytes));
                resultLabel.setText("Signature loaded.");
                lastDirectory = sigFile.getParentFile();
            } catch (Exception ex) {
                resultLabel.setText("Error loading signature: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /** Paste Base64 signature */
    public void pasteSignature(ActionEvent e) {
        try {
            signatureBytes = SignatureUtils.base64ToSignature(signatureTextArea.getText().trim());
            resultLabel.setText("Signature pasted.");
        } catch (Exception ex) {
            resultLabel.setText("Invalid Base64 signature.");
        }
    }

    /** Verify signature */
    public void verifySignature(ActionEvent e) {
        try {
            if (selectedDocument == null || signatureBytes == null || publicKey == null) {
                resultLabel.setText("Missing input (doc / sig / key).");
                return;
            }
            boolean valid = SignatureUtils.verifyFile(selectedDocument, signatureBytes, publicKey);
            resultLabel.setText(valid ? "✅ VALID" : "❌ INVALID");
        } catch (Exception ex) {
            resultLabel.setText("Error verifying: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /** Generate verification report */
    public void generateReport(ActionEvent e) {
        try {
            if (selectedDocument == null || signatureBytes == null || publicKey == null) {
                resultLabel.setText("Cannot generate report: missing input.");
                return;
            }

            String docHash = DocumentUtils.getFileHash(selectedDocument);
            String pubKeyFingerprint = DocumentUtils.hashBytes(publicKey.getEncoded());
            String result = resultLabel.getText();

            String report = String.format(
                    "Verification Report\n" +
                            "=====================\n" +
                            "File: %s\n" +
                            "SHA-256: %s\n" +
                            "Public Key Fingerprint: %s\n" +
                            "Result: %s\n",
                    selectedDocument.getName(), docHash, pubKeyFingerprint, result
            );

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Verification Report");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

            String defaultName = selectedDocument.getName().replaceAll("\\.[^.]*$", "") + "-verification-report.txt";
            chooser.setInitialFileName(defaultName);

            File reportFile = chooser.showSaveDialog(null);
            if (reportFile != null) {
                Files.writeString(reportFile.toPath(), report);
                lastDirectory = reportFile.getParentFile();
                resultLabel.setText("Report saved: " + reportFile.getName());
            }
        } catch (Exception ex) {
            resultLabel.setText("Error generating report: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
