package ru.bkmz.tehSistem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.Main;
import ru.bkmz.tehSistem.data.Data;
import ru.bkmz.tehSistem.util.CmdHandler;
import ru.bkmz.tehSistem.util.gui.elements.ItemGetMainController;
import ru.bkmz.tehSistem.util.gui.window.Notification;
import ru.bkmz.tehSistem.util.gui.window.StageDialog;

import java.io.File;
import java.util.ArrayList;

import static ru.bkmz.tehSistem.util.CmdHandler.cmdOut;

public class ControllerMain {
    public static final Logger logger = LogManager.getLogger();
    public ListView<HBox> listIpGet;
    public TextField fileOUT;
    public TextField fileIN;
    public ComboBox discList;
    public HBox panelHboxTool;
    public AnchorPane rootAP;
    public static Circle lite = new Circle(7);
    public CheckBox IE;
    ArrayList<String> arrayIP = Data.IP_LIST.getValuetAL();
    ObservableList<HBox> hBoxObservableList = FXCollections.observableArrayList();
    ObservableList<String> AZList = FXCollections.observableArrayList();
    public static ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    public static ArrayList<String> checkBoxesBoolean = new ArrayList<>();
    public static ItemGetMainController itemGetMainController;
    public static Text proces = new Text("готов к работе");
    public static int sizeEnd = 0;

    public void initialize() {
        logger.info("start initialize FXML ");

        lite.setFill(Color.rgb(0, 250, 0));
        lite.setStroke(Color.rgb(0, 0, 0));
        lite.setStrokeWidth(1);
        //HBox.setMargin(proces,new Insets(0,15,0,0));
        panelHboxTool.getChildren().add(proces);
        panelHboxTool.getChildren().add(lite);
        fileIN.setText(Data.IN_FILE.getValuetS());
        fileOUT.setText(Data.OUT_FILE.getValuetS());
        itemGetMainController = new ItemGetMainController(listIpGet, fileOUT, fileIN, hBoxObservableList, threads);
        for (int i = 65; i <= 90; i++) {
            AZList.add(String.valueOf((char) i));
        }

        discList.setItems(AZList);
        discList.setValue(Data.DISK.getValuetS());
        for (String s :
                arrayIP) {

            HBox hBox = new HBox(10);
            CheckBox checkBox = new CheckBox(s);
            hBox.getChildren().addAll(checkBox);
            checkBoxes.add(checkBox);
            hBoxObservableList.add(hBox);
            listIpGet.setItems(hBoxObservableList);
        }
        checkBoxesBoolean = Data.CBB.getValuetAL();
        for (int i = 0; i < checkBoxesBoolean.size(); i++) {
            checkBoxes.get(i).setSelected(Boolean.parseBoolean(checkBoxesBoolean.get(i)));
        }
        logger.info("stop initialize FXML ");
    }

    static ArrayList<Thread> threads = new ArrayList<>();

    public void copi(ActionEvent actionEvent) {
        try {

            boolean threadRuntime = false;
            for (Thread t :
                    threads) {
                if (t.isAlive()) {
                    threadRuntime = true;
                    break;
                }
            }
            if (!threadRuntime) {
                lite.setFill(Color.rgb(250, 250, 0));
                sizeEnd = 0;
                threads.clear();
                CmdHandler cmdHandler = new CmdHandler();
                for (CheckBox cb :
                        checkBoxes) {
                    String cbTxt = cb.getText(), discListTxt = discList.getValue().toString().toLowerCase(), fileOUText = fileOUT.getText();
                    if (cb.isSelected()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    logger.info("start ip:" + cbTxt);
                                    Runtime rt = Runtime.getRuntime();
                                    Process proc = rt.exec("cmd.exe /c mkdir \\\\" + cbTxt + "\\" + discListTxt + "$\\" + fileOUText);
                                    proc.waitFor();
                                    if (IE.isSelected()) {
                                        cmdOut(proc, cbTxt + " создание папки:" + fileOUText, IE);
                                    }
                                    cmdHandler.cmdRun(fileIN.getText(), cbTxt, discListTxt + "$", fileOUText, IE);

                                } catch (Exception e) {
                                    new Notification("Ошибка:" + cbTxt, e.getMessage(), Alert.AlertType.ERROR);
                                    logger.warn("cmd error", e);
                                }
                            }
                        });
                        threads.add(thread);
                        thread.start();
                        proces.setText("завершено " + sizeEnd + "/" + threads.size());
                    }
                }
            } else {
                new Notification("Ошибка", "Ещё есть не завершённые процес(ы)\n" +
                        "Осталось:" + (threads.size() - sizeEnd), Alert.AlertType.ERROR);
            }
            if (sizeEnd >= threads.size()) {
                proces.setText("готов к работе");
                lite.setFill(Color.rgb(0, 250, 0));

            }
        } catch (Exception e) {
            logger.warn("cmd error", e);
            proces.setText("неизвестная ошибка");
            lite.setFill(Color.rgb(250, 0, 0));
            lite.setStroke(Color.rgb(100, 0, 0));
        }
    }

    public static void addProcent() {
        sizeEnd++;
        proces.setText("завершено " + sizeEnd + "/" + threads.size());
        if (sizeEnd >= threads.size()) {
            proces.setText("готов к работе");
            lite.setFill(Color.rgb(0, 250, 0));

        }
    }

    public void settings(ActionEvent actionEvent) {
        checkBoxes.clear();
        new StageDialog("settings");

    }

    public void saveButton(ActionEvent actionEvent) {
        Data.IN_FILE.setValue(fileIN.getText());
        Data.OUT_FILE.setValue(fileOUT.getText());
        Data.DISK.setValue(discList.getValue().toString());
        checkBoxesBoolean.clear();
        for (CheckBox cb :
                checkBoxes) {
            checkBoxesBoolean.add(String.valueOf(cb.isSelected()));

        }
        Data.CBB.setValue(checkBoxesBoolean);
        Data.save();
    }

    public void auther(ActionEvent actionEvent) {
        new StageDialog("info");
    }

    public void fileDesctop(ActionEvent actionEvent) {
        FileChooser dialog = new FileChooser();
        File file = dialog.showOpenDialog(ru.bkmz.tehSistem.Main.stage);
        if (file != null)
            fileIN.setText(String.valueOf(file));
    }

    public void desctop(ActionEvent actionEvent) {
        DirectoryChooser dialog = new DirectoryChooser();
        File file = dialog.showDialog(Main.stage);
        if (file != null)
            fileIN.setText(String.valueOf(file));
    }
}
