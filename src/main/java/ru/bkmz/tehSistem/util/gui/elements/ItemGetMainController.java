package ru.bkmz.tehSistem.util.gui.elements;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.controller.ControllerMain;

import java.util.ArrayList;

public class ItemGetMainController {
    public static final Logger logger = LogManager.getLogger();
    private ListView<HBox> listIpGet;
    private TextField fileOUT;
    private TextField fileIN;
    ObservableList<HBox> hBoxObservableList = FXCollections.observableArrayList();
    ArrayList<Thread> threads;

    public ItemGetMainController(ListView<HBox> listIpGet, TextField fileOUT, TextField fileIN, ObservableList<HBox> hBoxObservableList, ArrayList<Thread> threads) {
        this.listIpGet = listIpGet;
        this.fileOUT = fileOUT;
        this.fileIN = fileIN;
        this.hBoxObservableList = hBoxObservableList;
        this.threads = threads;
    }

    public ListView<HBox> getListIpGet() {
        return listIpGet;
    }

    public void setListIpGet(ListView<HBox> listIpGet) {
        this.listIpGet = listIpGet;
    }

    public TextField getFileOUT() {
        return fileOUT;
    }

    public void setFileOUT(TextField fileOUT) {
        this.fileOUT = fileOUT;
    }

    public TextField getFileIN() {
        return fileIN;
    }

    public void setFileIN(TextField fileIN) {
        this.fileIN = fileIN;
    }

    public ObservableList<HBox> gethBoxObservableList() {
        return hBoxObservableList;
    }

    public void sethBoxObservableList(ObservableList<HBox> hBoxObservableList) {
        this.hBoxObservableList = hBoxObservableList;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<Thread> threads) {
        this.threads = threads;
    }

    public void setItems(ArrayList<String> arrayIP) {
        ArrayList<CheckBox> checkBoxes2 = new ArrayList<>();
        hBoxObservableList.clear();
        for (String s :
                arrayIP) {
            HBox hBox = new HBox(10);
            CheckBox checkBox = new CheckBox(s);
            hBox.getChildren().addAll(checkBox);
            hBoxObservableList.add(hBox);
            listIpGet.setItems(hBoxObservableList);
            checkBoxes2.add(checkBox);
        }
        ControllerMain.checkBoxes = checkBoxes2;
    }
}
