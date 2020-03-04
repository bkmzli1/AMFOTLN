package ru.bkmz.tehSistem.util.gui.elements;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.bkmz.tehSistem.Main.bd;

public class ItemGetMainController {
    public static final Logger logger = LogManager.getLogger();
    private ListView<HBox> listIpGet;
    private TextField fileOUT;
    private TextField fileIN;
    ObservableList<HBox> hBoxObservableList = FXCollections.observableArrayList();
    ArrayList<Thread> threads;
    ArrayList<String> ips = new ArrayList<>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    ArrayList<Text> textsConn = new ArrayList<>();


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

    public void upDate() {
        checkBoxes.clear();
        hBoxObservableList.clear();
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
                        HBox hBox = new HBox(10);
                        CheckBox checkBox = new CheckBox(ip);
                        boolean b =resultSet.getBoolean("boll");
                        checkBox.setSelected(b);

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
                        checkBoxes.add(checkBox);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                textsConn.add(text);
                                hBoxObservableList.addAll(hBox);
                            }
                        });
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                listIpGet.setItems(hBoxObservableList);
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
        hBoxObservableList.addAll(hBox);
        listIpGet.setItems(hBoxObservableList);

    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }
}
