package ru.bkmz.tehSistem.util.gui.window;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ru.bkmz.tehSistem.Main.logger;

public class Notification {
    public static final Logger logger = LogManager.getLogger();
    public Notification(String name, String info) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                logger.info(name+":"+info);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(name);
                alert.setHeaderText(null);
                alert.setContentText(info);
                alert.showAndWait();
            }
        });
    }

    public Notification(String name, String info, Alert.AlertType alert) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                logger.info(name+":"+info+":"+alert);
                Alert nAlert = new Alert(alert);
                nAlert.setTitle(name);
                nAlert.setHeaderText(null);
                nAlert.setContentText(info);
                nAlert.showAndWait();
            }
        });

    }
}
