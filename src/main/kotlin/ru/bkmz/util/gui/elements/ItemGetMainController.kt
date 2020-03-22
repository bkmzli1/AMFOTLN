package ru.bkmz.util.gui.elements


import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.apache.logging.log4j.LogManager
import ru.bkmz.Main.Companion.bd
import ru.bkmz.util.RunTime
import ru.bkmz.util.Table
import java.net.InetAddress
import java.sql.Statement
import java.util.*


class ItemGetMainController(
    var tableIP: TableView<Table>,
    var fileOUT: TextField,
    var fileIN: TextField,
    var tableOList: ObservableList<Table>,
    var threads: ArrayList<Thread>,
    var inHB: HBox,
    var outHB: HBox,
    var saveButton: Button,
    var iE: CheckBox
) {
    var ips = ArrayList<String>()
    var checkBoxes = ArrayList<CheckBox>()
    var textsConn = ArrayList<Text>()
    var thenB = true
    var allRuThread = 0
    fun upDate() {
        val runTime = RunTime()
        logger.trace("upDate start")
        checkBoxes.clear()
        tableOList.clear()
        textsConn.clear()
        thenB = false
        threadIPStop()
        thenB = true
        Thread(Runnable {
            try {
                val statmt: Statement = bd!!.conn.createStatement()
                val resultSet = statmt.executeQuery("SELECT * FROM 'IP'")
                while (resultSet.next()) {
                    val ip = resultSet.getString("ip")
                    ips.add(ip)
                    val checkBox = CheckBox(ip)
                    val b = resultSet.getBoolean("boll")
                    checkBox.isSelected = b
                    val table = Table(checkBox, resultSet.getString("name"))
                    tableOList.add(table)
                    val tIp = Thread(Runnable {
                        while (thenB) {
                            table.setCircleValue(Color.color(1.0, 1.0, 0.0))
                            try {
                                val con = InetAddress.getByName(ip).isReachable(
                                    bd!!.conn.createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'timeIpConnect'").getString(
                                        "value"
                                    ).toInt() * 1000
                                )
                                Platform.runLater {
                                    if (con) {
                                        table.setCircleValue(Color.color(0.0, 1.0, 0.0))
                                    } else {
                                        table.setCircleValue(Color.color(1.0, 0.0, 0.0))
                                    }
                                }
                                allRuThread++
                                while (allRuThread < threadsIP.size) {
                                    Thread.sleep(500)
                                }
                                Thread.sleep(
                                    bd!!.conn.createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'timeIP'").getString(
                                        "value"
                                    ).toInt() * 1000.toLong()
                                )
                            } catch (e: Exception) {
                                logger.warn("IP update", e)
                            }
                        }
                    }, "TIP :$ip")
                    tIp.start()
                    threadsIP.add(tIp)
                    checkBoxes.add(checkBox)
                    Platform.runLater { tableIP.setItems(tableOList) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, "upDate").start()
        logger.trace("upDate stop")
        runTime.stop("обновление таблицы")
    }

    private fun threadIPStop() {
        for (t in threadsIP) {
            try {
                t.stop()
                logger.trace("stop " + t.name)
            } catch (e: Exception) {
                try {
                    t.join()
                    logger.trace("join " + t.name)
                } catch (ex: InterruptedException) {
                    logger.warn("stop Thread Ip", ex)
                }
            }
        }
    }

    fun addList(ip: String?) {
        val hBox = HBox(10.0)
        val checkBox = CheckBox(ip)
        val text = Text(": " + "Обрабтка")
        hBox.children.addAll(checkBox, text)
        Thread(Runnable {
            try {
                val s = ": " + InetAddress.getByName(ip).isReachable(1000)
                Platform.runLater { text.text = s }
            } catch (e: Exception) {
            }
        }).start()
        textsConn.add(text)
        checkBoxes.add(checkBox)
        tableIP.setItems(tableOList)
    }


    companion object {
        val logger = LogManager.getLogger()
        var threadsIP = ArrayList<Thread>()
    }

    init {
        val colIP =
            TableColumn<Table, String>("IP")
        val colName =
            TableColumn<Table, String>("Имя")
        val colStat =
            TableColumn<Table, String>("Статус")
        colIP.setCellValueFactory(PropertyValueFactory("checkBox"))
        colName.setCellValueFactory(PropertyValueFactory("name"))
        colStat.setCellValueFactory(PropertyValueFactory("circle"))
        colIP.minWidth = 150.0
        colIP.maxWidth = 150.0
        colStat.minWidth = 100.0
        colStat.maxWidth = 100.0
        colName.style = " -fx-alignment: CENTER"
        colStat.style = " -fx-alignment: CENTER"
        colIP.isEditable = false
        colName.isEditable = false
        colStat.isEditable = false
        tableIP.columns.addAll(colIP, colName, colStat)
    }
}
