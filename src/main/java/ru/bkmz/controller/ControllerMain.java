package ru.bkmz.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.Main;
import ru.bkmz.util.CmdHandler;
import ru.bkmz.util.Table;
import ru.bkmz.util.gui.elements.CustomTableView;
import ru.bkmz.util.gui.elements.ItemGetMainController;
import ru.bkmz.util.gui.window.Notification;
import ru.bkmz.util.gui.window.StageDialog;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.Main.bd;
import static ru.bkmz.Main.stage;
import static ru.bkmz.util.CmdHandler.cmdOut;

public class ControllerMain {
    public static final Logger logger = LogManager.getLogger();
    public CustomTableView<Table> tableIP = new CustomTableView<>();
    ;
    public TextField fileOUT;
    public TextField fileIN;
    public ComboBox discList;
    public HBox panelHboxTool;
    public AnchorPane rootAP;
    public CheckBox IE;
    public HBox inHB;
    public HBox outHB;
    public Button saveButton;
    public VBox tabeVB;
    StageDialog auther = new StageDialog("auther", "Связь", false);
    StageDialog settings = new StageDialog("settings", "Настройка", false);
    StageDialog inf = new StageDialog("inf", "Информация", false);

    public static Circle lite = new Circle(7);


    ObservableList<Table> hBoxObservableList = FXCollections.observableArrayList();
    ObservableList<String> AZList = FXCollections.observableArrayList();

    public static ItemGetMainController itemGetMainController;
    public static Text proces = new Text("готов к работе");
    public static int sizeEnd = 0;

    public void initialize() {
        VBox.setVgrow(tableIP, Priority.ALWAYS);
        tableIP.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabeVB.getChildren().add(tableIP);
        logger.info("start initialize FXML ");
        lite.setFill(Color.rgb(0, 250, 0));

        lite.setStroke(Color.rgb(0, 0, 0));
        lite.setStrokeWidth(1);

        itemGetMainController = new ItemGetMainController(tableIP, fileOUT, fileIN, hBoxObservableList, threads, inHB, outHB, saveButton, IE);
        itemGetMainController.upDate();
        for (int i = 65; i <= 90; i++) {
            AZList.add(String.valueOf((char) i));
        }
        panelHboxTool.getChildren().add(proces);
        panelHboxTool.getChildren().add(lite);

        discList.setItems(AZList);
        try {
            Statement statmt = bd.getConn().createStatement();
            ;

            discList.setValue(statmt.executeQuery("SELECT * FROM 'settings' WHERE arg  = 'discList'").getString("value"));
            fileIN.setText(statmt.executeQuery("SELECT * FROM 'settings' WHERE arg  = 'fileIN' ").getString("value"));
            fileOUT.setText(statmt.executeQuery("SELECT * FROM 'settings' WHERE arg  = 'fileOUT' ").getString("value"));

            statmt.close();
        } catch (SQLException e) {
            logger.error("get settings:", e);
        }


    }

    public static ArrayList<Thread> threads = new ArrayList<>();

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
                        inHB.setDisable(true);
                        outHB.setDisable(true);
                        IE.setDisable(true);
                        tableIP.setDisable(true);
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

                                    cmdOut(proc, cbTxt + " создание папки:" + fileOUText, IE);

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
                inHB.setDisable(false);
                outHB.setDisable(false);
                IE.setDisable(false);
                tableIP.setDisable(false);
            }
        } catch (Exception e) {
            logger.warn("cmd error", e);
            proces.setText("неизвестная ошибка");
            lite.setStroke(Color.rgb(100, 0, 0));
            inHB.setDisable(false);
            outHB.setDisable(false);
            IE.setDisable(false);
            tableIP.setDisable(false);
        }
    }


    public static void addProcent() {
        sizeEnd++;
        proces.setText("завершено " + sizeEnd + "/" + threads.size());
        if (sizeEnd >= threads.size()) {
            proces.setText("готов к работе");
            lite.setFill(Color.rgb(0, 250, 0));
            itemGetMainController.getInHB().setDisable(false);
            itemGetMainController.getOutHB().setDisable(false);
            itemGetMainController.getIE().setDisable(false);
            itemGetMainController.getTableIP().setDisable(false);

        }
    }

    public void addIP(ActionEvent actionEvent) {

        new StageDialog("add", "IP", true);

    }

    public void saveButton(ActionEvent actionEvent) {
        //UPDATE settings SET FILE_OUT = 'o', FILE_IN = 'i', DISK = 'd' WHERE id = 1

        try {
            Statement statmt = bd.getConn().createStatement();
            statmt.execute("UPDATE settings SET 'value' = '" + discList.getValue() + "' WHERE arg = 'discList' ");
            statmt.execute("UPDATE settings SET 'value' = '" + fileIN.getText() + "' WHERE arg = 'fileIN' ");
            statmt.execute("UPDATE settings SET 'value' = '" + fileOUT.getText() + "' WHERE arg = 'fileOUT' ");
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
        auther.show();
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

    public void settings(ActionEvent actionEvent) {
        settings.show();
    }

    public void inf(ActionEvent actionEvent) {
        inf.show();
    }
}
