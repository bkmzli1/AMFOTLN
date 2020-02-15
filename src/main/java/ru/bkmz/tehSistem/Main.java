package ru.bkmz.tehSistem;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.data.Data;
import ru.bkmz.tehSistem.util.gui.window.Notification;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {

    public static ArrayList<String> list = new ArrayList<>();
    public static Stage stage;
    final String name = "AMFOTLN";
    public static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        logger.info("start:launch");
        launch(args);
    }

    @Override
    public void init() {
        logger.info("start:init");
        try {
            Data.load();
        } catch (Exception e) {
            File file = new File(Data.fileSave);
            try {
                Data.fiStream.close();
                Data.oiStream.close();
            } catch (IOException ex) {
                logger.fatal(ex);
            }

            if (file.delete()) {
                new Notification("Глобальная ошибка", "Фаил повреждён\n" +
                        "Данные ввернутся к зоводским настройкам");
            } else {
                new Notification("Глобальная  ошибка", "Фаил повреждён\n" +
                        "Не удолось удалить файл.\n" +
                        "Удалите данный фаил \"" + Data.fileSave + "\".\n" +
                        "Программа завершится через 20 секунд.");
                logger.fatal("Фатальная ошибка", e);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException ex) {
                    logger.fatal(ex);
                }
                System.exit(1);
            }

        }
        logger.info("stop:init");
    }

    @Override
    public void start(Stage stage) {
        logger.info("start loader FXML");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/main.fxml")));
        try {
            loader.load();
        } catch (IOException e) {
            logger.warn("load fxml", e);
        }

        Parent root = loader.getRoot();
        logger.info("stop loader FXML");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/main.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(name);
        InputStream inputStream = ClassLoader.class.getResourceAsStream("/img/icon.png");
        try {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        } catch (NullPointerException e) {
            logger.warn("img null");
        }
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
        
        stage.show();

        this.stage = stage;
        //this.follScren = stage.isMaximized();
        //stage.setResizable(resizable);

    }
}
