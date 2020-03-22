package ru.bkmz.util

import javafx.scene.control.CheckBox
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle


class Table(var checkBox: CheckBox, var name: String) {
    var circle: Circle

    fun setCircleValue(color: Color?) {
        circle.fill = color
    }

    init {
        circle = Circle(7.0)
        circle.fill = Color.rgb(250, 250, 0)
        circle.stroke = Color.rgb(0, 0, 0)
        circle.strokeWidth = 1.0
        HBox.setHgrow(checkBox, Priority.ALWAYS)
        VBox.setVgrow(checkBox, Priority.ALWAYS)
        checkBox.maxWidth = Double.MAX_VALUE
    }
}
