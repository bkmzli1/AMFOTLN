package ru.bkmz.controller

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.apache.logging.log4j.LogManager
import ru.bkmz.Main.Companion.bd
import ru.bkmz.util.gui.elements.textPropertyTime
import ru.bkmz.util.gui.window.Notification
import java.sql.SQLException
import java.sql.Statement
import java.util.*


class ControllerAdd {
    lateinit var listIpGet: ListView<HBox>
    lateinit var ip4: TextField
    lateinit var ip3: TextField
    lateinit var ip2: TextField
    lateinit var ip1: TextField
    lateinit var argsVBox: VBox
    lateinit var name: TextField
    lateinit var vbox: VBox

    var hBoxObservableList = FXCollections.observableArrayList<HBox>()
    var checkBoxes = ArrayList<CheckBox>()
    fun initialize() {
        logger.info("start initialize FXML")
        ip4.text = ""
        ip3.text = ""
        ip2.text = "168"
        ip1.text = "192"
        textPropertyTime(ip4, 3, 255)
        textPropertyTime(ip3, 3, 255)
        textPropertyTime(ip2, 3, 255)
        textPropertyTime(ip1, 3, 255)
        update()
        logger.info("stop initialize FXML ")
    }

    fun add() {
        val IP1S = ip1.text
        val IP2S = ip2.text
        val IP3S = ip3.text
        val IP4S = ip4.text
        if ((ip1.text != "") and (ip2.text != "") and (ip3.text != "") and
            (ip4.text != "") and (name.text != "")
        ) {
            val ip = "$IP1S.$IP2S.$IP3S.$IP4S"
            var two = false
            for (cb in checkBoxes) {
                if (cb.text == ip) {
                    two = true
                    break
                }
            }
            if (!two) {
                try {
                    val statmt: Statement = bd!!.conn.createStatement()
                    statmt.execute("INSERT INTO IP ('ip','boll','name') VALUES ('" + ip + "','false','" + name.text + "');")
                    statmt.close()
                } catch (e: SQLException) {
                }
                ControllerMain.itemGetMainController?.addList(ip)
                addIP(ip)
            } else {
                Notification("БД", "Данный IP уже есть")
            }
        } else {
            Notification("БД", "Заполните все поля")
        }
        ControllerMain.itemGetMainController?.upDate()
        update()
    }

    fun deleteIP() {
        for (cb in checkBoxes) {
            if (cb.isSelected) {
                try {
                    val statmt: Statement = bd!!.conn.createStatement()
                    statmt.execute("DELETE FROM IP WHERE ip = '" + cb.text + "'")
                } catch (e: Exception) {
                    logger.error("deleteIP:", e)
                }
            }
        }
        ControllerMain.itemGetMainController?.upDate()
        update()
    }

    fun argSave() {
        update()
    }

    fun addIP(ip: String?) {
        val hBox = HBox(10.0)
        val checkBox = CheckBox(ip)
        checkBoxes.add(checkBox)
        HBox.setHgrow(checkBox,Priority.ALWAYS)
        VBox.setVgrow(checkBox,Priority.ALWAYS)
        checkBox.maxWidth = Double.MAX_VALUE
        hBox.children.addAll(checkBox)
        hBoxObservableList.addAll(hBox)
        listIpGet.setItems(hBoxObservableList)
    }

    fun update() {
        hBoxObservableList.clear()
        checkBoxes.clear()
        try {
            val statmt: Statement = bd!!.conn.createStatement()
            val resultSet = statmt.executeQuery("SELECT * FROM 'IP'")
            while (resultSet.next()) {
                addIP(resultSet.getString("ip"))
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}

