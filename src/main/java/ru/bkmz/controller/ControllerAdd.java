package ru.bkmz.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.util.gui.window.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.Main.bd;
import static ru.bkmz.util.gui.elements.BuilderElements.textPropertyTime;

public class ControllerAdd {
    public static final Logger logger = LogManager.getLogger();
    public ListView<HBox> listIpGet;
    public TextField ip4;
    public TextField ip3;
    public TextField ip2;
    public TextField ip1;
    public VBox argsVBox;
    public TextField name;
    public VBox vbox;


    ObservableList<HBox> hBoxObservableList = FXCollections.observableArrayList();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public void initialize() {
        logger.info("start initialize FXML");
        ip4.setText("");
        ip3.setText("");
        ip2.setText("168");
        ip1.setText("192");
        textPropertyTime(ip4, 3, 255);
        textPropertyTime(ip3, 3, 255);
        textPropertyTime(ip2, 3, 255);
        textPropertyTime(ip1, 3, 255);

        update();
        logger.info("stop initialize FXML ");
    }

    public void add(ActionEvent actionEvent) {
        String IP1S = ip1.getText(),
                IP2S = ip2.getText(),
                IP3S = ip3.getText(),
                IP4S = ip4.getText();

        if (!ip1.getText().equals("") & !ip2.getText().equals("") & !ip3.getText().equals("") &
                !ip4.getText().equals("") & !name.getText().equals("")) {
            String ip = IP1S + "." + IP2S + "." + IP3S + "." + IP4S;
            boolean two = false;
            for (CheckBox cb :
                    checkBoxes) {
                if (cb.getText().equals(ip)) {
                    two = true;
                    break;
                }
            }
            if (!two) {
                try {
                    Statement statmt = bd.getConn().createStatement();
                    statmt.execute("INSERT INTO IP ('ip','boll','name') VALUES ('" + ip + "','false','"+name.getText()+"');");
                    statmt.close();
                } catch (SQLException e) {

                }
                ControllerMain.itemGetMainController.addList(ip);
                addIP(ip);
            } else {
                new Notification("БД", "Данный IP уже есть");
            }
        } else {
            new Notification("БД", "Заполните все поля");
        }
        ControllerMain.itemGetMainController.upDate();
        update();
    }

    public void deleteIP(ActionEvent actionEvent) {
        for (CheckBox cb :
                checkBoxes) {
            if (cb.isSelected()) {
                try {
                    Statement statmt = bd.getConn().createStatement();
                    statmt.execute("DELETE FROM IP WHERE ip = '" + cb.getText() + "'");
                } catch (Exception e) {
                    logger.error("deleteIP:", e);
                }
            }
        }
        ControllerMain.itemGetMainController.upDate();
        update();
    }

    public void argSave(ActionEvent actionEvent) {
        update();
    }

    void addIP(String ip) {
        HBox hBox = new HBox(10);
        CheckBox checkBox = new CheckBox(ip);
        checkBoxes.add(checkBox);
        hBox.getChildren().addAll(checkBox);
        hBoxObservableList.addAll(hBox);
        listIpGet.setItems(hBoxObservableList);
    }

    void update() {
        hBoxObservableList.clear();
        checkBoxes.clear();
        try {
            Statement statmt = bd.getConn().createStatement();
            ResultSet resultSet = statmt.executeQuery("SELECT * FROM 'IP'");
            while (resultSet.next()) {
                addIP(resultSet.getString("ip"));
            }
        } catch (Exception e) {

        }
    }


}

