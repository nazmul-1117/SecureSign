package com.securesign.controllers.documents;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.securesign.utils.DocumentUtils;

public class DocumentController implements Initializable {

    public Label fileNameLabel;
    public Label fileSizeLabel;
    public Label fileModifiedLabel;
    public Label fileHashLabel;
    public Label messageLB;

    private File selectedFile;
    private String fileHash;

    private File lastDirectory = new File(System.getProperty("user.home"));

    public void openDocument(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Document");

        if (lastDirectory.exists()) {
            chooser.setInitialDirectory(lastDirectory);
        }

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            selectedFile = file;
            lastDirectory = file.getParentFile(); // remember folder

            fileNameLabel.setText("File Name: " + file.getName());
            fileSizeLabel.setText("File Size: " + file.length() + " bytes");
            fileModifiedLabel.setText("Last Modify: " + new Date(file.lastModified()).toString());
            fileHashLabel.setText(""); // clear previous hash
        }
    }

    public void computeHash(ActionEvent event) {
        try {
            if (selectedFile != null) {
                fileHash = DocumentUtils.getFileHash(selectedFile);
                fileHashLabel.setText(fileHash);
            } else {
                fileHashLabel.setText("No file selected!");
            }
        } catch (Exception e) {
            fileHashLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveHash(ActionEvent event) {
        try {
            if (selectedFile != null && fileHash != null) {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Hash Files", "*.hash"));
                chooser.setTitle("Save Hash File");

                if (lastDirectory.exists()) {
                    chooser.setInitialDirectory(lastDirectory);
                }

                // Suggest default file name like: filename.hash
                String defaultName = selectedFile.getName().replaceAll("\\.[^.]*$", "");
                chooser.setInitialFileName(defaultName);

                File hashFile = chooser.showSaveDialog(null);
                if (hashFile != null) {
                    DocumentUtils.saveHashToFile(fileHash, hashFile);
                    lastDirectory = hashFile.getParentFile(); // update dir
                    messageLB.setText("Hash saved: " + hashFile.getName());
                }
            } else {
                messageLB.setText("Compute hash first!");
            }
        } catch (Exception e) {
            messageLB.setText("Error saving hash: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void verifyHash(ActionEvent event) {
        try {
            if (selectedFile == null) {
                fileHashLabel.setText("Select a file first!");
                return;
            }

            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Hash Files", "*.hash"));
            chooser.setTitle("Select Hash File");

            if (lastDirectory.exists()) {
                chooser.setInitialDirectory(lastDirectory);
            }

            File hashFile = chooser.showOpenDialog(null);
            if (hashFile != null) {
                lastDirectory = hashFile.getParentFile(); // update dir

                String expectedHash = DocumentUtils.loadHashFromFile(hashFile);
                boolean valid = DocumentUtils.verifyFileHash(selectedFile, expectedHash);
                fileHashLabel.setText(valid ? "Hash matches" : "Hash mismatch");
            }
        } catch (Exception e) {
            fileHashLabel.setText("Error verifying hash: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // optional initialization
    }
}
