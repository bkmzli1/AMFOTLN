package ru.bkmz.controller

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import org.apache.logging.log4j.LogManager
import ru.bkmz.Main.Companion.bd
import ru.bkmz.Main.Companion.stage
import ru.bkmz.util.CmdHandler
import ru.bkmz.util.CmdHandler.Companion.cmdOut
import ru.bkmz.util.Table
import ru.bkmz.util.gui.elements.CustomTableView
import ru.bkmz.util.gui.elements.ItemGetMainController
import ru.bkmz.util.gui.window.Notification
import ru.bkmz.util.gui.window.StageDialog
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.sql.SQLException
import java.sql.Statement
import java.util.*

class ControllerMain {
    @FXML
    var tableIP = CustomTableView<Table>()
    lateinit var fileOUT: TextField
    lateinit var fileIN: TextField
    lateinit var discList: ComboBox<String>
    lateinit var panelHboxTool: HBox
    lateinit var rootAP: AnchorPane
    lateinit var IE: CheckBox
    lateinit var inHB: HBox
    lateinit var outHB: HBox
    lateinit var saveButton: Button
    lateinit var tabeVB: VBox
    var auther = StageDialog("auther", "Связь", false)
    var settings = StageDialog("settings", "Настройка", false)
    var inf = StageDialog("inf", "Информация", false)
    var hBoxObservableList: ObservableList<Table> = FXCollections.observableArrayList()
    var AZList: ObservableList<String> = FXCollections.observableArrayList<String>()
    fun initialize() {
        VBox.setVgrow(tableIP, Priority.ALWAYS)
        tableIP.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        tabeVB.children.add(tableIP)
        logger.info("start initialize FXML ")
        lite.fill = Color.rgb(0, 250, 0)
        lite.stroke = Color.rgb(0, 0, 0)
        lite.strokeWidth = 1.0
        itemGetMainController =
            ItemGetMainController(
                tableIP,
                fileOUT,
                fileIN, hBoxObservableList, threads,
                inHB,
                outHB,
                saveButton,
                IE
            )
        itemGetMainController!!.upDate()
        for (i in 65..90) {
            AZList.add((i.toChar()).toString())
        }
        panelHboxTool.children.add(proces)
        panelHboxTool.children.add(lite)
        discList.setItems(AZList)
        try {
            val statmt: Statement = bd!!.conn.createStatement()
            discList.value =
                statmt.executeQuery("SELECT * FROM 'settings' WHERE arg  = 'discList'").getString("value")
            fileIN.text = statmt.executeQuery("SELECT * FROM 'settings' WHERE arg  = 'fileIN' ").getString("value")
            fileOUT.text = statmt.executeQuery("SELECT * FROM 'settings' WHERE arg  = 'fileOUT' ").getString("value")
            statmt.close()
        } catch (e: SQLException) {
            logger.error("get settings:", e)
        }
    }

    fun copi() {
        try {
            var threadRuntime = false
            for (t in threads) {
                if (t.isAlive) {
                    threadRuntime = true
                    break
                }
            }
            if (!threadRuntime) {
                lite.fill = Color.rgb(250, 250, 0)
                sizeEnd = 0
                threads.clear()
                val cmdHandler = CmdHandler()
                for (cb in itemGetMainController!!.checkBoxes) {
                    val cbTxt = cb.text
                    val discListTxt = discList.value.toString().toLowerCase()
                    val fileOUText = fileOUT.text
                    if (cb.isSelected) {
                        inHB.isDisable = true
                        outHB.isDisable = true
                        IE.isDisable = true
                        tableIP.isDisable = true
                        var thendlerOpen :Thread? = Thread();
                        val thread = Thread(Runnable {
                            try {
                                logger.info("start ip:$cbTxt")
                                val rt = Runtime.getRuntime()
                                val proc = rt.exec(
                                    "cmd.exe /c mkdir \\\\" + cbTxt + "\\" + discListTxt + "$\\" +
                                            fileOUText
                                )
                                proc.waitFor()
                                cmdOut(proc, "$cbTxt создание папки:$fileOUText", IE)
                                cmdHandler.cmdRun(fileIN.text, cbTxt, "$discListTxt$", fileOUText, IE)
                            } catch (e: Exception) {
                                Notification(
                                    "Ошибка:$cbTxt",
                                    e.message!!,
                                    Alert.AlertType.ERROR
                                )
                                logger.warn("cmd error", e)
                                proces.text = "неизвестная ошибка"
                                lite.stroke = Color.rgb(100, 0, 0)
                                inHB.isDisable = false
                                outHB.isDisable = false
                                IE.isDisable = false
                                tableIP.isDisable = false
                                thendlerOpen!!.stop()
                            }
                        })
                        thendlerOpen = thread
                        threads.add(thread)
                        thread.start()
                        proces.text = "завершено " + sizeEnd + "/" + threads.size
                    }
                }
            } else {
                Notification(
                    "Ошибка",
                    "Ещё есть не завершённые процес(ы)\n" +
                            "Осталось:" + (threads.size - sizeEnd),
                    Alert.AlertType.ERROR
                )
            }
            if (sizeEnd >= threads.size) {
                proces.text = "готов к работе"
                lite.fill = Color.rgb(0, 250, 0)
                inHB.isDisable = false
                outHB.isDisable = false
                IE.isDisable = false
                tableIP.isDisable = false
            }
        } catch (e: Exception) {
            logger.warn("cmd error", e)
            proces.text = "неизвестная ошибка"
            lite.stroke = Color.rgb(100, 0, 0)
            inHB.isDisable = false
            outHB.isDisable = false
            IE.isDisable = false
            tableIP.isDisable = false
        }
    }

    fun addIP() {
        StageDialog("add", "IP", true)
    }

    fun saveButton() { //UPDATE settings SET FILE_OUT = 'o', FILE_IN = 'i', DISK = 'd' WHERE id = 1
        try {
            val statmt: Statement = bd!!.conn.createStatement()
            statmt.execute("UPDATE settings SET 'value' = '" + discList.value + "' WHERE arg = 'discList' ")
            statmt.execute("UPDATE settings SET 'value' = '" + fileIN.text + "' WHERE arg = 'fileIN' ")
            statmt.execute("UPDATE settings SET 'value' = '" + fileOUT.text + "' WHERE arg = 'fileOUT' ")
            for (i in itemGetMainController!!.checkBoxes.indices) {
                statmt.execute(
                    "UPDATE IP SET  boll = '" +
                            itemGetMainController!!.checkBoxes[i].isSelected.toString().replace(
                                "true",
                                "1"
                            )
                                .replace("false", "0") + "' WHERE id = " + (i + 1)
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun auther() {
        auther.show()
    }

    fun fileDesctop() {
        val dialog = FileChooser()
        val file = dialog.showOpenDialog(stage)
        if (file != null) fileIN.text = file.toString()
    }

    fun desctop() {
        val dialog = DirectoryChooser()
        val file = dialog.showDialog(stage)
        if (file != null) fileIN.text = file.toString()
    }

    fun fileDesctopIP() {
        var dialog = FileChooser()
        var onen = false
        try {
            for (cb in itemGetMainController!!.checkBoxes) {
                if (InetAddress.getByName(cb.text).isReachable(1000)) {
                    logger.trace(cb.text)
                    logger.trace("\\\\" + cb.text + "\\" + discList.value.toString().toLowerCase() + "$")
                    dialog.initialDirectory = File(
                        "\\\\" + cb.text + "\\" + discList.value.toString().toLowerCase() + "$"
                    )
                    try {
                        val file = dialog.showOpenDialog(stage)
                        if (file != null) fileOUT.text = file.toString().replace(
                            "\\\\" + cb.text + "\\" + discList.value.toString().toLowerCase() + "$",
                            ""
                        )
                        onen = true
                        break
                    } catch (e: Exception) {
                    }
                }
            }
        } catch (oiE: IOException) {
            logger.error("desctop:", oiE)
        }
        if (!onen) {
            Notification(
                "Ошибка", "Программе не удолось открыть путь по локальной сети.\n" +
                        "Попробуйте сменить диск или добавить IP адреса"
            )
            dialog = FileChooser()
            val file = dialog.showOpenDialog(stage)
            if (file != null) fileOUT.text = file.toString()
        }
    }

    // dialog.setInitialDirectory(new File("\\\\192.168.0.159\\c$"));
    fun desctopIP() {
        var dialog = DirectoryChooser()
        var onen = false
        try {
            for (cb in itemGetMainController!!.checkBoxes) {
                if (InetAddress.getByName(cb.text).isReachable(1000)) {
                    logger.trace(cb.text)
                    logger.trace("\\\\" + cb.text + "\\" + discList.value.toString().toLowerCase() + "$")
                    dialog.initialDirectory = File(
                        "\\\\" + cb.text + "\\" + discList.value.toString().toLowerCase() + "$"
                    )
                    try {
                        val file = dialog.showDialog(stage)
                        if (file != null) fileOUT.text = file.toString().replace(
                            "\\\\" + cb.text + "\\" + discList.value.toString().toLowerCase() + "$",
                            ""
                        )
                        onen = true
                        break
                    } catch (e: Exception) {
                    }
                }
            }
        } catch (oiE: IOException) {
            logger.error("desctop:", oiE)
        }
        if (!onen) {
            Notification(
                "Ошибка", "Программе не удолось открыть путь по локальной сети.\n" +
                        "Попробуйте сменить диск или добавить IP адреса"
            )
            dialog = DirectoryChooser()
            val file = dialog.showDialog(stage)
            if (file != null) fileOUT.text = file.toString()
        }
    }

    fun upDate() {
        itemGetMainController!!.upDate()
    }

    fun settings() {
        settings.show()
    }

    fun inf() {
        inf.show()
    }

    companion object {
        val logger = LogManager.getLogger()
        var lite = Circle(7.0)
        var itemGetMainController: ItemGetMainController? = null
        var proces = Text("готов к работе")
        var sizeEnd = 0
        var threads = ArrayList<Thread>()
        fun addProcent() {
            sizeEnd++
            proces.text = "завершено " + sizeEnd + "/" + threads.size
            if (sizeEnd >= threads.size) {
                proces.text = "готов к работе"
                lite.fill = Color.rgb(0, 250, 0)
                itemGetMainController!!.inHB.isDisable = false
                itemGetMainController!!.outHB.isDisable = false
                itemGetMainController!!.iE.isDisable = false
                itemGetMainController!!.tableIP.isDisable = false
            }
        }
    }
}

