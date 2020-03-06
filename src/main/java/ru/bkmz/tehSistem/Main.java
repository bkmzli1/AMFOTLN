package ru.bkmz.tehSistem;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.sql.BD;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;
    final String name = "AMFOTLN";
    public static final Logger logger = LogManager.getLogger();
    static final SplashScreen splash = SplashScreen.getSplashScreen();
    static Graphics2D g;
    static Rectangle rectangle;
    public static String FileSeve = System.getenv("APPDATA") + "\\AMFOTLN",
            SQLFile = FileSeve + "\\AMFOTLN.db";
    public static BD bd;

    public static void main(String[] args) throws IOException {
        g = splash.createGraphics();
        rectangle = splash.getBounds();

        logger.info("start:launch");
        launch(args);
    }

    @Override
    public void init() {
        File file = new File(FileSeve);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            file = new File(SQLFile);
            boolean f = !file.exists();
            bd = new BD(SQLFile);

            if (f) {
                bd.start();
            }
        } catch (Exception e) {
            logger.error("BD:", e);
        }

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
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        stage.setHeight((sSize.height * 30d) / 100d);
        stage.setWidth((sSize.width * 30d) / 100d);
        stage.setMinHeight((sSize.height * 20d) / 100d);
        stage.setMinWidth((sSize.width * 20.1d) / 100d);
        splash.close();
        stage.initStyle(StageStyle.DECORATED);
        stage.show();

        this.stage = stage;
        //this.follScren = stage.isMaximized();
        //stage.setResizable(resizable);
    }
}
