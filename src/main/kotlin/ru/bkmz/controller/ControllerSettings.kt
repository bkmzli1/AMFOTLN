package ru.bkmz.controller

import javafx.beans.InvalidationListener
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.apache.logging.log4j.LogManager
import ru.bkmz.Main.Companion.bd
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*

class ControllerSettings {
    lateinit var vbox: VBox
    fun initialize() {
        argCopi()
        timeIP()
    }

    private fun timeIP() {
        vbox.children.add(Text("IP"))
        try {
            val timeArg = arrayOf("timeIP", "timeIpConnect")
            for (s in timeArg) {
                val hBox = HBox(10.0)
                hBox.alignment = Pos.CENTER
                VBox.setVgrow(hBox, Priority.ALWAYS)
                val spinner = Spinner<Int>()
                val valueFactory: SpinnerValueFactory<Int> =  //
                    IntegerSpinnerValueFactory(
                        5,
                        60,
                        bd!!.conn.createStatement().executeQuery("SELECT * FROM 'settings' where arg = '$s'").getString(
                            "value"
                        ).toInt()
                    )
                spinner.setValueFactory(valueFactory)
                spinner.valueProperty().addListener(InvalidationListener {
                    try {
                        bd!!.conn.createStatement()
                            .execute("UPDATE settings SET 'value' = '" + spinner.value + "' WHERE arg = '" + s + "' ")
                        ControllerMain.itemGetMainController!!.upDate()
                    } catch (e: SQLException) {
                        logger.trace("UPDATE settings ")
                    }
                })
                hBox.children.addAll(spinner, Text("секунд"))
                vbox.children.add(hBox)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun argCopi() {
        vbox.children.add(Text("Аргументы копирования"))
        try {
            val resultSet: ResultSet = bd!!.conn.createStatement().executeQuery("SELECT * FROM 'cmdArg' ")
            var score = 0
            val hBoxes = ArrayList<HBox>()
            var hBox = HBox(10.0)
            hBox.alignment = Pos.CENTER
            hBoxes.add(hBox)
            VBox.setVgrow(hBox, Priority.ALWAYS)
            while (resultSet.next()) {
                if (score >= 3) {
                    hBox = HBox(10.0)
                    hBox.alignment = Pos.CENTER
                    VBox.setVgrow(hBox, Priority.ALWAYS)
                    hBoxes.add(hBox)
                    score = 0
                }
                val checkBox = CheckBox(resultSet.getString("arg"))
                checkBox.isSelected = resultSet.getBoolean("value")
                checkBox.maxWidth = Double.MAX_VALUE
                HBox.setHgrow(checkBox, Priority.ALWAYS)
                checkBox.onMouseClicked = EventHandler {
                    var statmtCB: Statement?
                    try {
                        statmtCB = bd!!.conn.createStatement()
                        if (checkBox.isSelected) {
                            statmtCB.execute("UPDATE cmdArg SET 'value' = '" + 1 + "' WHERE arg = '" + checkBox.text + "' ")
                        } else {
                            statmtCB.execute("UPDATE cmdArg SET 'value' = '" + 0 + "' WHERE arg = '" + checkBox.text + "' ")
                        }
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                }
                hBox.children.add(checkBox)
                score++
                // VBox.setVgrow(hBox, Priority.);
            }
            for (hb in hBoxes) {
                vbox.children.add(hb)
            }
        } catch (sqlE: SQLException) {
            logger.trace("sett", sqlE)
        }
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}