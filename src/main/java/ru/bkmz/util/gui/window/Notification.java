package ru.bkmz.util.gui.window;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ru.bkmz.util.Decoder.UTFtoWin1251;

public class Notification {
    public static final Logger logger = LogManager.getLogger();

    public Notification(String name, String info) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                logger.info(UTFtoWin1251(name + ":" + info));
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
                logger.info(UTFtoWin1251(name + ":" + info + ":" + alert));
                Alert nAlert = new Alert(alert);
                nAlert.setTitle(name);
                nAlert.setHeaderText(null);
                nAlert.setContentText(info);
                nAlert.showAndWait();
            }
        });

    }
}
