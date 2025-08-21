package com.securesign.controllers.rsa_key;

import com.jfoenix.controls.JFXRadioButton;
import com.securesign.models.rsa_key.RSAKeyGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;


public class RSAKeyGeneratorController implements Initializable {

    public JFXRadioButton keylen1024RB;
    public JFXRadioButton keylen2048RB;
    public JFXRadioButton keylen3072RB;
    public JFXRadioButton keylen4096RB;

    public JFXRadioButton algorithmRSAB;

    public TextField publicKeyTF;
    public Label messageLB;
    public TextField privateKeyTF;

    private final ToggleGroup keyLengthToggleGroup = new ToggleGroup();
    private final ToggleGroup algorithmToggleGroup = new ToggleGroup();

    String algorithm, keyLength;

    RSAKeyGenerator rsaKeyGenerator=null;
    private File lastDirectory = new File(System.getProperty("user.home")); // or remember last saved dir

    private void initToggle() {
        keylen1024RB.setToggleGroup(keyLengthToggleGroup);
        keylen2048RB.setToggleGroup(keyLengthToggleGroup);
        keylen3072RB.setToggleGroup(keyLengthToggleGroup);
        keylen4096RB.setToggleGroup(keyLengthToggleGroup);

        algorithmRSAB.setToggleGroup(algorithmToggleGroup);
    }

    private void collectData(){
        keyLength = ((JFXRadioButton) keyLengthToggleGroup.getSelectedToggle()).getText().toLowerCase();
        algorithm = ((JFXRadioButton) algorithmToggleGroup.getSelectedToggle()).getText().toLowerCase();
    }

    private void algorithmRun(ActionEvent actionEvent){
        rsaKeyGenerator = new RSAKeyGenerator(algorithm, keyLength);
        boolean isGenerate = rsaKeyGenerator.generateKey(actionEvent);
        if (isGenerate){
            String privateKey = rsaKeyGenerator.getPrivateKeyString();
            String publicKey = rsaKeyGenerator.getPublicKeyString();

            privateKeyTF.setText(privateKey);
            publicKeyTF.setText(publicKey);

            messageLB.setText("Key Generated Successfully ✅");
        }else {
            messageLB.setText("Get Error for Generate Keys ❌");
        }
    }

    private void printData(){
        System.out.println("Algorithm: " + algorithm);
        System.out.println("Key Length: " + keyLength);
    }

    // Save Public Key
//    public void savePublicKey(ActionEvent actionEvent) {
//        try {
//            if (rsaKeyGenerator == null || rsaKeyGenerator.getPublicKey() == null) {
//                messageLB.setText("Generate keys first!");
//                return;
//            }
//            FileChooser chooser = new FileChooser();
//            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM files", "*.pem"));
//            chooser.setTitle("Save Public Key");
//            File file = chooser.showSaveDialog(null);
//            if (file != null) {
//                rsaKeyGenerator.savePublicKey(file);
//                messageLB.setText("Public key saved: " + file.getName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            messageLB.setText("Error saving public key: " + e.getMessage());
//        }
//    }

    public void savePublicKey(ActionEvent actionEvent) {
        try {
            if (rsaKeyGenerator == null || rsaKeyGenerator.getPublicKey() == null) {
                messageLB.setText("Generate keys first!");
                return;
            }

            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM files", "*.pem"));
            chooser.setTitle("Save Public Key");

            // Set initial directory
            if (lastDirectory.exists()) {
                chooser.setInitialDirectory(lastDirectory);
            }

            // Suggest default file name
            chooser.setInitialFileName("public_key.pem");

            File file = chooser.showSaveDialog(null);

            if (file != null) {
                rsaKeyGenerator.savePublicKey(file);
                messageLB.setText("Public key saved: " + file.getName());

                // Store the last directory
                lastDirectory = file.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLB.setText("Error saving public key: " + e.getMessage());
        }
    }


    // Save Private Key
    public void savePrivateKey(ActionEvent actionEvent) {
        try {
            if (rsaKeyGenerator == null || rsaKeyGenerator.getPrivateKey() == null) {
                messageLB.setText("Generate keys first!");
                return;
            }

            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM files", "*.pem"));
            chooser.setTitle("Save Private Key");

            // Set initial directory
            if (lastDirectory.exists()) {
                chooser.setInitialDirectory(lastDirectory);
            }

            // Suggest default file name
            chooser.setInitialFileName("private_key.pem");

            File file = chooser.showSaveDialog(null);

            if (file != null) {
                rsaKeyGenerator.savePrivateKey(file);
                messageLB.setText("Private key saved: " + file.getName());

                // Store the last directory for reuse
                lastDirectory = file.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLB.setText("Error saving private key: " + e.getMessage());
        }
    }


    // Load Public Key
    public void loadPublicKey(ActionEvent actionEvent) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM files", "*.pem"));
            chooser.setTitle("Load Public Key");

            // Set initial directory
            if (lastDirectory.exists()) {
                chooser.setInitialDirectory(lastDirectory);
            }

            File file = chooser.showOpenDialog(null);
            if (file != null) {
                if (rsaKeyGenerator == null) rsaKeyGenerator = new RSAKeyGenerator("rsa", "2048"); // default
                rsaKeyGenerator.loadPublicKey(file);
                publicKeyTF.setText(rsaKeyGenerator.getPublicKeyString());
                messageLB.setText("Public key loaded: " + file.getName());

                // Store the last directory
                lastDirectory = file.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLB.setText("Error loading public key: " + e.getMessage());
        }
    }

    // Load Private Key
    public void loadPrivateKey(ActionEvent actionEvent) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PEM files", "*.pem"));
            chooser.setTitle("Load Private Key");

            // Set initial directory
            if (lastDirectory.exists()) {
                chooser.setInitialDirectory(lastDirectory);
            }

            File file = chooser.showOpenDialog(null);
            if (file != null) {
                if (rsaKeyGenerator == null) rsaKeyGenerator = new RSAKeyGenerator("rsa", "2048"); // default
                rsaKeyGenerator.loadPrivateKey(file);
                privateKeyTF.setText(rsaKeyGenerator.getPrivateKeyString());
                messageLB.setText("Private key loaded: " + file.getName());

                // Store the last directory
                lastDirectory = file.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLB.setText("Error loading private key: " + e.getMessage());
        }
    }







    public void generateKey(ActionEvent actionEvent) {
        collectData();
        algorithmRun(actionEvent);
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initToggle();
    }
}
