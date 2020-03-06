package ru.bkmz.tehSistem.util.gui.elements;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.util.Table;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.tehSistem.Main.bd;

public class ItemGetMainController {
    public static final Logger logger = LogManager.getLogger();
    private TableView<Table> tableIP;
    private TextField fileOUT;
    private TextField fileIN;
    ObservableList<Table> tableOList;
    ArrayList<Thread> threads;
    ArrayList<String> ips = new ArrayList<>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    ArrayList<Text> textsConn = new ArrayList<>();


    public ItemGetMainController(TableView<Table> tableIP, TextField fileOUT, TextField fileIN, ObservableList<Table> hBoxObservableList, ArrayList<Thread> threads) {
        this.tableIP = tableIP;
        this.fileOUT = fileOUT;
        this.fileIN = fileIN;
        this.tableOList = hBoxObservableList;
        this.threads = threads;
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
        checkBoxes.clear();
        tableOList.clear();
        textsConn.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statmt = bd.getConn().createStatement();
                    ResultSet resultSet = statmt.executeQuery("SELECT * FROM 'IP'");
                    while (resultSet.next()) {
                        String ip = resultSet.getString("ip");
                        ips.add(ip);

                        CheckBox checkBox = new CheckBox(ip);
                        boolean b = resultSet.getBoolean("boll");
                        checkBox.setSelected(b);

                        Table table = new Table(checkBox, resultSet.getString("name"));
                        tableOList.add(table);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean con = InetAddress.getByName(ip).isReachable(1000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (con) {
                                                table.setCircleValue(Color.color(0,1,0));
                                            } else {
                                                table.setCircleValue(Color.color(1,0,0));
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                }
                            }
                        }).start();
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

}
