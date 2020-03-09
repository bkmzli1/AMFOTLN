package ru.bkmz.controller;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.util.gui.elements.WrappedImageView;

import java.io.InputStream;

public class ControllerAuther {
    public static final Logger logger = LogManager.getLogger();
    public VBox vBoxAether;
    public VBox vBoxGit;
    public VBox vBoxVK;
    public VBox vBoxGoogle;
    public Label title;

    private InputStream inputStreamAether = ClassLoader.class.getResourceAsStream("/img/fon1.png");
    private InputStream inputStreamGit = ClassLoader.class.getResourceAsStream("/img/github_PNG20.png");
    private InputStream inputStreamVK = ClassLoader.class.getResourceAsStream("/img/vk.png");
    private InputStream inputStreamGoogle = ClassLoader.class.getResourceAsStream("/img/gmail.png");

    private Image imageAether = new Image(inputStreamAether);
    private Image imageGit = new Image(inputStreamGit);
    private Image imageVk = new Image(inputStreamVK);
    private Image imageGoogle = new Image(inputStreamGoogle);


    public void initialize() {
        logger.info("start initialize FXML ");
        title.setId("title");
        imageBuilder(imageGit, "https://github.com/bkmzli1", vBoxGit);
        imageBuilder(imageAether, "https://github.com/bkmzli1", vBoxAether);
        imageBuilder(imageVk, "https://vk.com/id409602224", vBoxVK);
        imageBuilder(imageGoogle, "https://myaccount.google.com/?utm_source=OGB&tab=mk&utm_medium=act", vBoxGoogle);

        logger.info("stop initialize FXML ");
    }


    private void imageBuilder(Image image, String url, VBox vBox) {
        WrappedImageView imageV;

        imageV = new WrappedImageView(image, url);

        vBox.getChildren().set(0, imageV);
    }

}
