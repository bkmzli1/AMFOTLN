package ru.bkmz.controller;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.Main.bd;

public class ControllerSettings {
    public static final Logger logger = LogManager.getLogger();

    public VBox vbox;

    public void initialize() {
        vbox.getChildren().add(new Text("Аргументы копирования"));
        try {

            ResultSet resultSet = bd.getConn().createStatement().executeQuery("SELECT * FROM 'settings' where id >= " + Integer.parseInt(
                    bd.getConn().createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'settingsId'").getString("value")));
            int score = 0;
            ArrayList<HBox> hBoxes = new ArrayList<>();
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            hBoxes.add(hBox);
            VBox.setVgrow(hBox, Priority.ALWAYS);
            while (resultSet.next()) {
                if (score >= 3) {
                    hBox = new HBox(10);
                    hBox.setAlignment(Pos.CENTER);
                    VBox.setVgrow(hBox, Priority.ALWAYS);

                    hBoxes.add(hBox);
                    score = 0;
                }
                CheckBox checkBox = new CheckBox(resultSet.getString("arg"));
                checkBox.setSelected(resultSet.getBoolean("value"));
                checkBox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(checkBox,Priority.ALWAYS);
                checkBox.setOnMouseClicked(event -> {
                    Statement statmtCB = null;
                    try {
                        statmtCB = bd.getConn().createStatement();
                        if (checkBox.isSelected()) {
                            statmtCB.execute("UPDATE settings SET 'value' = '" + 1 + "' WHERE arg = '" + checkBox.getText() + "' ");
                        } else {
                            statmtCB.execute("UPDATE settings SET 'value' = '" + 0 + "' WHERE arg = '" + checkBox.getText() + "' ");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });
                hBox.getChildren().add(checkBox);
                score++;
                // VBox.setVgrow(hBox, Priority.);

            }
            for (HBox hb :
                    hBoxes) {
                vbox.getChildren().add(hb);
            }
        } catch (SQLException sqlE) {
            logger.trace("sett", sqlE);
        }

    }
}
