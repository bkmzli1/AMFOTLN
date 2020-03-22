package ru.bkmz.util.gui.elements;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.Main;
import ru.bkmz.util.Decoder;
import ru.bkmz.util.RunTime;
import ru.bkmz.util.Table;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.Main.bd;

public class ItemGetMainController {
    private static final Logger logger = LogManager.getLogger();
    private TableView<Table> tableIP;
    private TextField fileOUT;
    private TextField fileIN;
    private HBox inHB;
    private HBox outHB;
    private Button saveButton;
    private CheckBox IE;
    ObservableList<Table> tableOList;
    ArrayList<Thread> threads;
    ArrayList<String> ips = new ArrayList<>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    ArrayList<Text> textsConn = new ArrayList<>();
    public static ArrayList<Thread> threadsIP = new ArrayList<>();
    boolean thenB = true;
    int allRuThread;

    public ItemGetMainController(TableView<Table> tableIP, TextField fileOUT, TextField fileIN, ObservableList<Table> hBoxObservableList, ArrayList<Thread> threads, HBox inHB, HBox outHB,
                                 Button saveButton, CheckBox IE) {
        this.tableIP = tableIP;
        this.fileOUT = fileOUT;
        this.fileIN = fileIN;
        this.tableOList = hBoxObservableList;
        this.threads = threads;
        this.inHB = inHB;
        this.outHB = outHB;
        this.saveButton = saveButton;
        this.IE = IE;
        TableColumn<Table, String> colIP = new TableColumn<Table, String>("IP");
        TableColumn<Table, String> colName = new TableColumn<Table, String>("Имя");
        TableColumn<Table, String> colStat = new TableColumn<Table, String>("Статус");

        colIP.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStat.setCellValueFactory(new PropertyValueFactory<>("circle"));

        colIP.setMinWidth(150);
        colIP.setMaxWidth(150);
        colStat.setMinWidth(100);
        colStat.setMaxWidth(100);

        colName.setStyle(" -fx-alignment: CENTER");
        colStat.setStyle(" -fx-alignment: CENTER");

        colIP.setEditable(false);
        colName.setEditable(false);
        colStat.setEditable(false);

        tableIP.getColumns().addAll(colIP, colName, colStat);
    }

    public void upDate() {
        RunTime runTime = new RunTime();
        logger.trace("upDate start");
        checkBoxes.clear();
        tableOList.clear();
        textsConn.clear();
        thenB = false;
        threadIPStop();

        thenB = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statmt = Main.bd.getConn().createStatement();
                    ResultSet resultSet = statmt.executeQuery("SELECT * FROM 'IP'");
                    while (resultSet.next()) {
                        String ip = resultSet.getString("ip");
                        ips.add(ip);

                        CheckBox checkBox = new CheckBox(ip);
                        boolean b = resultSet.getBoolean("boll");
                        checkBox.setSelected(b);

                        Table table = new Table(checkBox, resultSet.getString("name"));
                        tableOList.add(table);
                        Thread tIp = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (thenB) {
                                    table.setCircleValue(Color.color(1, 1, 0));
                                    try {
                                        boolean con = InetAddress.getByName(ip).isReachable(
                                                Integer.parseInt(bd.getConn().createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'timeIpConnect'").getString("value")) * 1000);
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {

                                                if (con) {
                                                    table.setCircleValue(Color.color(0, 1, 0));
                                                } else {
                                                    table.setCircleValue(Color.color(1, 0, 0));
                                                }
                                            }
                                        });
                                        allRuThread++;
                                        while (allRuThread < threadsIP.size()) {
                                            Thread.sleep(500);
                                        }
                                        Thread.sleep(Integer.parseInt(bd.getConn().createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'timeIP'").getString("value")) * 1000);
                                    } catch (Exception e) {
                                        logger.warn("IP update", e);
                                    }

                                }
                            }
                        }, "TIP :" + ip);
                        tIp.start();
                        threadsIP.add(tIp);
                        checkBoxes.add(checkBox);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                tableIP.setItems(tableOList);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, "upDate").start();
        logger.trace("upDate stop");
        runTime.stop("обновление таблицы");
    }

    private void threadIPStop() {
        for (Thread t :
                threadsIP) {
            try {
                t.stop();
                logger.trace("stop " + t.getName());
            } catch (Exception e) {
                try {
                    t.join();
                    logger.trace("join " + t.getName());
                } catch (InterruptedException ex) {
                    logger.warn("stop Thread Ip", ex);
                }

            }
        }
    }

    public void addList(String ip) {

        HBox hBox = new HBox(10);
        CheckBox checkBox = new CheckBox(ip);

        Text text = new Text(": " + "Обрабтка");

        hBox.getChildren().addAll(checkBox, text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = ": " + InetAddress.getByName(ip).isReachable(1000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(s);
                        }
                    });
                } catch (Exception e) {
                }
            }
        }).start();
        textsConn.add(text);
        checkBoxes.add(checkBox);

        tableIP.setItems(tableOList);

    }

    public TableView<Table> getTableIP() {
        return tableIP;
    }

    public void setTableIP(TableView<Table> tableIP) {
        this.tableIP = tableIP;
    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public static Logger getLogger() {
        return logger;
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

    public HBox getInHB() {
        return inHB;
    }

    public void setInHB(HBox inHB) {
        this.inHB = inHB;
    }

    public HBox getOutHB() {
        return outHB;
    }

    public void setOutHB(HBox outHB) {
        this.outHB = outHB;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public CheckBox getIE() {
        return IE;
    }

    public void setIE(CheckBox IE) {
        this.IE = IE;
    }
}
