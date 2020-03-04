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
import ru.bkmz.tehSistem.util.CmdHandler;
import ru.bkmz.tehSistem.util.gui.elements.ItemGetMainController;
import ru.bkmz.tehSistem.util.gui.window.Notification;
import ru.bkmz.tehSistem.util.gui.window.StageDialog;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.tehSistem.Main.bd;
import static ru.bkmz.tehSistem.Main.stage;
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
    ;
    ObservableList<HBox> hBoxObservableList = FXCollections.observableArrayList();
    ObservableList<String> AZList = FXCollections.observableArrayList();

    public static ItemGetMainController itemGetMainController;
    public static Text proces = new Text("готов к работе");
    public static int sizeEnd = 0;

    public void initialize() {
        logger.info("start initialize FXML ");

        lite.setFill(Color.rgb(0, 250, 0));
        lite.setStroke(Color.rgb(0, 0, 0));
        lite.setStrokeWidth(1);

        itemGetMainController = new ItemGetMainController(listIpGet, fileOUT, fileIN, hBoxObservableList, threads);
        itemGetMainController.upDate();
        for (int i = 65; i <= 90; i++) {
            AZList.add(String.valueOf((char) i));
        }
        panelHboxTool.getChildren().add(proces);
        panelHboxTool.getChildren().add(lite);

        discList.setItems(AZList);
        try {
            Statement statmt = bd.getConn().createStatement();
            ResultSet resultSet = statmt.executeQuery("SELECT * FROM 'settings'");
            discList.setValue(resultSet.getString("DISK"));
            fileIN.setText(resultSet.getString("FILE_IN"));
            fileOUT.setText(resultSet.getString("FILE_OUT"));
            statmt.close();
            resultSet.close();
        } catch (SQLException e) {

        }


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
                        itemGetMainController.getCheckBoxes()) {
                    String cbTxt = cb.getText(), discListTxt = discList.getValue().toString().toLowerCase(),
                            fileOUText = fileOUT.getText();
                    if (cb.isSelected()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    logger.info("start ip:" + cbTxt);
                                    Runtime rt = Runtime.getRuntime();
                                    Process proc =
                                            rt.exec("cmd.exe /c mkdir \\\\" + cbTxt + "\\" + discListTxt + "$\\" +
                                                    fileOUText);
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

        new StageDialog("settings");

    }

    public void saveButton(ActionEvent actionEvent) {
        //UPDATE settings SET FILE_OUT = 'o', FILE_IN = 'i', DISK = 'd' WHERE id = 1

        try {
            Statement statmt = bd.getConn().createStatement();
            statmt.execute(
                    "UPDATE settings SET FILE_OUT = '" + fileOUT.getText() + "', FILE_IN = '" + fileIN.getText() +
                            "', DISK = '" + discList.getValue() + "' WHERE id = 1");
            for (int i = 0; i < itemGetMainController.getCheckBoxes().size(); i++) {
                statmt.execute("UPDATE IP SET  boll = '" +
                        String.valueOf(itemGetMainController.getCheckBoxes().get(i).isSelected()).replace("true", "1")
                                .replace("false", "0") + "' WHERE id = " + (i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void auther(ActionEvent actionEvent) {
        new StageDialog("info");
    }

    public void fileDesctop(ActionEvent actionEvent) {
        FileChooser dialog = new FileChooser();
        File file = dialog.showOpenDialog(Main.stage);
        if (file != null)
            fileIN.setText(String.valueOf(file));
    }

    public void desctop(ActionEvent actionEvent) {
        DirectoryChooser dialog = new DirectoryChooser();
        File file = dialog.showDialog(Main.stage);
        if (file != null)
            fileIN.setText(String.valueOf(file));
    }

    public void fileDesctopIP(ActionEvent actionEvent) {
        FileChooser dialog = new FileChooser();
        boolean onen = false;
        try {
            for (CheckBox cb :
                    itemGetMainController.getCheckBoxes()) {
                if (InetAddress.getByName(cb.getText()).isReachable(1000)) {
                    logger.trace(cb.getText());
                    logger.trace("\\\\" + cb.getText() + "\\" + discList.getValue().toString().toLowerCase() + "$");
                    dialog.setInitialDirectory(new File(
                            "\\\\" + cb.getText() + "\\" + discList.getValue().toString().toLowerCase() + "$"));
                    try {
                        File file = dialog.showOpenDialog(stage);
                        if (file != null)
                            fileOUT.setText(String.valueOf(file).replace(
                                    "\\\\" + cb.getText() + "\\" + discList.getValue().toString().toLowerCase() + "$",
                                    ""));
                        onen = true;
                        break;
                    } catch (Exception e) {

                    }

                }
            }
        } catch (IOException oiE) {
            logger.error("desctop:", oiE);
        }
        if (!onen) {
            new Notification("Ошибка", "Программе не удолось открыть путь по локальной сети.\n" +
                    "Попробуйте сменить диск или добавить IP адреса");
            dialog = new FileChooser();
            File file = dialog.showOpenDialog(stage);
            if (file != null)
                fileOUT.setText(String.valueOf(file));
        }


    }

    // dialog.setInitialDirectory(new File("\\\\192.168.0.159\\c$"));
    public void desctopIP(ActionEvent actionEvent) {

        DirectoryChooser dialog = new DirectoryChooser();

        boolean onen = false;
        try {
            for (CheckBox cb : itemGetMainController.getCheckBoxes()) {
                if (InetAddress.getByName(cb.getText()).isReachable(1000)) {
                    logger.trace(cb.getText());
                    logger.trace("\\\\" + cb.getText() + "\\" + discList.getValue().toString().toLowerCase() + "$");
                    dialog.setInitialDirectory(new File(
                            "\\\\" + cb.getText() + "\\" + discList.getValue().toString().toLowerCase() + "$"));

                    try {
                        File file = dialog.showDialog(stage);
                        if (file != null)
                            fileOUT.setText(String.valueOf(file).replace(
                                    "\\\\" + cb.getText() + "\\" + discList.getValue().toString().toLowerCase() + "$",
                                    ""));
                        onen = true;
                        break;
                    } catch (Exception e) {

                    }

                }
            }
        } catch (IOException oiE) {
            logger.error("desctop:", oiE);
        }

        if (!onen) {
            new Notification("Ошибка", "Программе не удолось открыть путь по локальной сети.\n" +
                    "Попробуйте сменить диск или добавить IP адреса");
            dialog = new DirectoryChooser();
            File file = dialog.showDialog(stage);
            if (file != null)
                fileOUT.setText(String.valueOf(file));
        }


    }

    public void upDate(ActionEvent actionEvent) {
        itemGetMainController.upDate();
    }
}
