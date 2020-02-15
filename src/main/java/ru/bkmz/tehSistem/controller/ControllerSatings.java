package ru.bkmz.tehSistem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.data.Data;
import ru.bkmz.tehSistem.util.gui.window.Notification;

import java.util.ArrayList;

import static ru.bkmz.tehSistem.controller.ControllerMain.itemGetMainController;
import static ru.bkmz.tehSistem.util.gui.elements.BuilderElements.textProperty;

public class ControllerSatings {
    public static final Logger logger = LogManager.getLogger();
    public ListView<HBox> listIpGet;
    public TextField ip4;
    public TextField ip3;
    public TextField ip2;
    public TextField ip1;


    ArrayList<String> arrayIP = Data.IP_LIST.getValuetAL();
    ObservableList<HBox> hBoxObservableList = FXCollections.observableArrayList();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public void initialize() {
        logger.info("start initialize FXML");
        textProperty(ip4, 3);
        textProperty(ip3, 3);
        textProperty(ip2, 3);
        textProperty(ip1, 3);
        for (String s :
                Data.IP_LIST.getValuetAL()) {
            HBox hBox = new HBox(10);
            CheckBox checkBox = new CheckBox(s);
            hBox.getChildren().addAll(checkBox);
            checkBoxes.add(checkBox);
            hBoxObservableList.add(hBox);
            listIpGet.setItems(hBoxObservableList);
            itemGetMainController.setItems(arrayIP);
        }
        logger.info("stop initialize FXML ");
    }

    public void add(ActionEvent actionEvent) {
        int
                IP1 = Integer.parseInt(ip1.getText()),
                IP2 = Integer.parseInt(ip2.getText()),
                IP3 = Integer.parseInt(ip3.getText()),
                IP4 = Integer.parseInt(ip4.getText());
        String IP1S = ip1.getText(),
                IP2S = ip2.getText(),
                IP3S = ip3.getText(),
                IP4S = ip4.getText();

        if (IP1 <= 225 & IP2 <= 225 & IP3 <= 225 & IP4 <= 225) {
            boolean b = false;
            for (String s :
                    arrayIP) {
                if ((IP1 + "." + IP2 + "." + IP3 + "." + IP4).equals(s)) {
                    b = true;
                    new Notification("Уведомление", "IP уже есть");
                    break;
                }
            }
            if (!b) {
                hBoxObservableList.clear();
                checkBoxes.clear();
                arrayIP.add(IP1S + "." + IP2S + "." + IP3S + "." + IP4S);
                Data.IP_LIST.setValuetAL(arrayIP);
                Data.save();
                for (String s :
                        Data.IP_LIST.getValuetAL()) {

                    HBox hBox = new HBox(10);
                    CheckBox checkBox = new CheckBox(s);
                    hBox.getChildren().addAll(checkBox);
                    checkBoxes.add(checkBox);
                    hBoxObservableList.add(hBox);
                    listIpGet.setItems(hBoxObservableList);
                }

            }
        } else {
            new Notification("информация", "Привышен адрес 225");
        }
        itemGetMainController.setItems(arrayIP);
    }

    public void deleteIP(ActionEvent actionEvent) {
        ArrayList<String> remList = new ArrayList<>();

        for (CheckBox cb :
                checkBoxes) {
            if (cb.isSelected()) {
                remList.add(cb.getText());
            }
        }
        for (int i = 0; i < arrayIP.size(); i++) {
            for (String s :
                    remList) {
                if (arrayIP.get(i).equals(s)) {
                    arrayIP.remove(i);
                }
            }
        }
        hBoxObservableList.clear();
        checkBoxes.clear();
        Data.IP_LIST.setValuetAL(arrayIP);
        Data.save();
        for (String s :
                Data.IP_LIST.getValuetAL()) {

            HBox hBox = new HBox(10);
            CheckBox checkBox = new CheckBox(s);
            hBox.getChildren().addAll(checkBox);
            checkBoxes.add(checkBox);
            hBoxObservableList.add(hBox);
            listIpGet.setItems(hBoxObservableList);
        }

        itemGetMainController.setItems(arrayIP);
    }

}

