package com.securesign.controllers.signing;

import com.securesign.utils.KeyUtils;
import com.securesign.utils.SignatureUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SignatureController {

    public Label documentName;
    public Label keyName;
    public Label messageLB;
    public TextArea signatureTextArea;

    private File selectedDocument;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private byte[] signatureBytes;

    private File lastDirectory = new File(System.getProperty("user.home"));

    /** Open a document to sign or verify */
    public void openDocument(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Document");
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        selectedDocument = chooser.showOpenDialog(null);
        if (selectedDocument != null) {
            documentName.setText(selectedDocument.getName());
            lastDirectory = selectedDocument.getParentFile();
            messageLB.setText("");
        }
    }

    /** Open a private key to sign document */
    public void openKey(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Private Key (PEM)");
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        File keyFile = chooser.showOpenDialog(null);
        if (keyFile != null) {
            try {
                privateKey = KeyUtils.loadPrivateKey(keyFile);
                keyName.setText(keyFile.getName());
                lastDirectory = keyFile.getParentFile();
                messageLB.setText("Private key loaded");
            } catch (Exception e) {
                messageLB.setText("Error loading key: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /** Sign the document using the loaded private key */
    public void signDocument(ActionEvent event) {
        try {
            if (selectedDocument == null) {
                messageLB.setText("Select a document first!");
                return;
            }
            if (privateKey == null) {
                messageLB.setText("Load a private key first!");
                return;
            }

            if (!selectedDocument.exists() || !selectedDocument.canRead()) {
                messageLB.setText("Cannot read the selected file.");
                return;
            }

            signatureBytes = SignatureUtils.signFile(selectedDocument, privateKey);
            String base64Signature = SignatureUtils.signatureToBase64(signatureBytes);
            signatureTextArea.setText(base64Signature);
            messageLB.setText("Document signed successfully!");

        } catch (Exception e) {
            messageLB.setText("Error signing document: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Save signature as Base64 text */
    public void saveSignature(ActionEvent event) {
        if (signatureBytes == null || selectedDocument == null) {
            messageLB.setText("No signature to save!");
            return;
        }

        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Signature");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Signature Files", "*.sig"));
            if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

            String defaultName = selectedDocument.getName().replaceAll("\\.[^.]*$", "") + ".sig";
            chooser.setInitialFileName(defaultName);

            File sigFile = chooser.showSaveDialog(null);
            if (sigFile != null) {
                SignatureUtils.saveSignatureToFileBase64(signatureBytes, sigFile);
                lastDirectory = sigFile.getParentFile();
                messageLB.setText("Signature saved: " + sigFile.getName());
            }
        } catch (Exception e) {
            messageLB.setText("Error saving signature: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Load external signature from Base64 .sig file */
    public void loadSignature(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Signature File (.sig)");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Signature Files", "*.sig"));
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        File sigFile = chooser.showOpenDialog(null);
        if (sigFile != null) {
            try {
                signatureBytes = SignatureUtils.loadSignatureFromFileBase64(sigFile);
                String base64Signature = SignatureUtils.signatureToBase64(signatureBytes);
                signatureTextArea.setText(base64Signature);
                lastDirectory = sigFile.getParentFile();
                messageLB.setText("Signature loaded: " + sigFile.getName());
            } catch (Exception e) {
                messageLB.setText("Error loading signature: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /** Copy signature text to clipboard */
    public void copySignature(ActionEvent event) {
        if (signatureTextArea.getText().isEmpty()) {
            messageLB.setText("No signature to copy!");
            return;
        }

        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(signatureTextArea.getText());
        clipboard.setContent(content);

        messageLB.setText("Signature copied to clipboard");
    }

    /** Verify signature using public key */
    public void verifySignature(ActionEvent event) {
        if (selectedDocument == null || signatureBytes == null || publicKey == null) {
            messageLB.setText("Document, signature, or public key missing!");
            return;
        }

        try {
            boolean valid = SignatureUtils.verifyFile(selectedDocument, signatureBytes, publicKey);
            messageLB.setText(valid ? "✅ Signature valid" : "❌ Signature invalid");
        } catch (Exception e) {
            messageLB.setText("Error verifying signature: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Load public key for verification */
    public void loadPublicKey(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Public Key (PEM)");
        if (lastDirectory.exists()) chooser.setInitialDirectory(lastDirectory);

        File pubKeyFile = chooser.showOpenDialog(null);
        if (pubKeyFile != null) {
            try {
                publicKey = KeyUtils.loadPublicKey(pubKeyFile);
                lastDirectory = pubKeyFile.getParentFile();
                messageLB.setText("Public key loaded: " + pubKeyFile.getName());
            } catch (Exception e) {
                messageLB.setText("Error loading public key: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
