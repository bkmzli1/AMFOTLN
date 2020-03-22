package ru.bkmz.util.gui.elements

import javafx.scene.control.TextField
import org.apache.logging.log4j.LogManager
val logger = LogManager.getLogger()


    private fun textProperty(textField: TextField) {
        textField.textProperty().addListener { _, _, newValue ->
            if (!newValue.matches("[\\d,.-]".toRegex())) {
                textField.text = newValue.replace("[^\\d,.-]".toRegex(), "")
            }
        }
    }

    fun textPropertyTime(textField: TextField, size: Int, maxNumber: Int) {
        textField.textProperty().addListener { _, _, newValue ->
            var newValueVar = newValue
            if (!newValueVar.matches("[\\d]".toRegex())) {
                newValueVar = newValueVar.replace("[^\\d]".toRegex(), "")
                if (newValueVar.length > size) {
                    newValueVar = newValueVar.substring(0, size)
                }
                try {
                    if (newValueVar.toInt() > maxNumber) {
                        newValueVar = newValueVar.substring(0, newValueVar.length - 1)
                    }
                } catch (nfe: NumberFormatException) {
                }
                textField.text = newValueVar
            }
        }
    }



