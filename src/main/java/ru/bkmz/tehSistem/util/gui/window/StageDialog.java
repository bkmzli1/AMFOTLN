package ru.bkmz.tehSistem.util.gui.window;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.bkmz.tehSistem.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class StageDialog {

    private Stage newWindow = new Stage();

    public StageDialog(String name) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/" + name + ".fxml")));
        StackPane secondaryLayout = new StackPane();


        Scene secondScene = new Scene(secondaryLayout, 598, 317);
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        // Specifies the modality for new window.

        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(Main.stage);

        // Set position of second window, related to primary window.
        try {
            loader.load();
        } catch (IOException e) {

        }
        Parent root = loader.getRoot();
        InputStream inputStream = ClassLoader.class.getResourceAsStream("/img/icon.png");
        try {
            Image image = new Image(inputStream);
            newWindow.getIcons().add(image);
        } catch (NullPointerException e) {

        }

        Scene scene = new Scene(root);
        scene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/main.css")).toExternalForm());
        newWindow.setScene(scene);
        newWindow.setWidth(590);
        newWindow.setHeight(590 - 10);
        newWindow.setResizable(false);
        newWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            }
        });
        newWindow.show();


    }

    public Stage getNewWindow() {
        return newWindow;
    }

    public void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }
}
