package com.securesign.controllers.about;

import com.securesign.models.about.About;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    public Label aboutUsIntroLabel;
    public Label nazmulIntro;
    public Label fuadIntro;


    private void setText(){
        String intro = About.aboutIntro();
        aboutUsIntroLabel.setText(intro);

        String aboutNazmul = About.aboutNazmul();
        nazmulIntro.setText(aboutNazmul);

        String aboutFuad = About.aboutFuad();
        fuadIntro.setText(aboutFuad);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText();
    }
}