package ru.bkmz;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.sql.BD;
import ru.bkmz.util.gui.window.Notification;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static ru.bkmz.controller.ControllerMain.sizeEnd;
import static ru.bkmz.controller.ControllerMain.threads;


public class Main extends Application {

    public static Stage stage = new Stage();
    ;
    final static String name = "AMFOTLN";
    public static final Logger logger = LogManager.getLogger();
    SplashScreen splashScreen = SplashScreen.getSplashScreen();
    public static String FileSeve = System.getenv("APPDATA") + "\\AMFOTLN",
            SQLFile = FileSeve + "\\AMFOTLN.db";
    public static BD bd;
    public static final String APPLICATION_NAME = name;
    Thread window = new Thread();
    boolean windowsB = false;

    public static void main(String[] args) {
        for (String arg :
                args) {
            logger.trace(arg);
        }
        try {
            logger.info("start:launch");
            launch(args);
        } catch (Exception e) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("-------------------------------------------");
            logger.trace("", e);
            System.out.println("-------------------------------------------");
        }
        logger.info("exit");
        System.exit(0);
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
    public void start(Stage hideStage) {
        hideStage.initStyle(StageStyle.UTILITY);
        hideStage.setWidth(0);
        hideStage.setHeight(0);
        hideStage.setMaxWidth(0);
        hideStage.setMinWidth(0);
        hideStage.setMaxHeight(0);
        hideStage.setMinHeight(0);
        hideStage.setOpacity(0);
        hideStage.show();
        stage();
        tryMenu();
    }

    void tryMenu() {
        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("завершить");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayMenu.add(item);
        item = new MenuItem("открыть");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openWindow();
            }
        });
        trayMenu.add(item);
        URL inputStream = ClassLoader.class.getResource("/img/icon.png");
        System.out.println(inputStream);
        java.awt.Image icon = Toolkit.getDefaultToolkit().getImage(inputStream);
        TrayIcon trayIcon = new TrayIcon(icon, APPLICATION_NAME, trayMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        trayIcon.displayMessage(APPLICATION_NAME, "Application started!",
                TrayIcon.MessageType.INFO);
    }

    void stage() {

        logger.info("start loader FXML");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/main.fxml")));
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Parent root = loader.getRoot();
        logger.info("stop loader FXML");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/main.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(APPLICATION_NAME);
        InputStream inputStream = ClassLoader.class.getResourceAsStream("/img/icon.png");
        try {
            javafx.scene.image.Image image = new javafx.scene.image.Image(inputStream);
            stage.getIcons().add(image);
        } catch (NullPointerException ne) {
            System.out.println("icon null");
        }

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (sizeEnd >= threads.size()) {
                    logger.info("Platform exit");
                    Platform.exit();
                }else {
                    window.stop();
                    windowsB = false;
                }
            }
        });
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        stage.setHeight((sSize.height * 30d) / 100d);
        stage.setWidth((sSize.width * 30d) / 100d);
        stage.setMinHeight((sSize.height * 20d) / 100d);
        stage.setMinWidth((sSize.width * 20.1d) / 100d);
        splashScreen.close();
        logger.info("show");
        stage.show();
        windowsB = true;


    }

    private void openWindow() {

        if (!windowsB) {
            window = new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            windowsB = true;
                            stage.show();
                        }
                    });

                }
            }, "windowOpen");
            window.start();
        } else {
            new Notification("", "Окно уже открыто");
        }


    }
}
