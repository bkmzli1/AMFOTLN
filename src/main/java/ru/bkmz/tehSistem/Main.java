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
            SQLFile = FileSeve + "\\AMFOTLN.sqlite";
    public static BD bd;

    public static void main(String[] args) throws IOException {
        rectangle = splash.getBounds();
        g = splash.createGraphics();
        g.setColor(Color.GREEN);
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
        splashUP(30);
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
        scene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/main.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(name);
        splashUP(50);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        stage.setHeight((sSize.height * 50d) / 100d);
        stage.setWidth((sSize.width * 50d) / 100d);
        stage.setMinHeight((sSize.height * 20d) / 100d);
        stage.setMinWidth((sSize.width * 20d) / 100d);
        splashUP(100);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splash.close();
        stage.show();

        this.stage = stage;
        //this.follScren = stage.isMaximized();
        //stage.setResizable(resizable);
    }

    static int rootSize = 0;
    static Thread thread;

    static void splashUP(int size) {
        thread = new Thread(new Runnable() {
            @Override public void run() {
                try {
                    if (String.valueOf(thread).equals("null"))
                        thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = rootSize + 1; i <= size; i++) {
                    int finalI = i;
                    g.fillRect(0, 0, rectangle.width * finalI / 100, 10);
                    splash.update();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                rootSize = size;

            }
        });
        thread.start();



    }
}
