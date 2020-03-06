package ru.bkmz.tehSistem.util;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Table {
    CheckBox checkBox;
    String name;
    Circle circle;



    public Table(CheckBox checkBox, String name) {
        this.checkBox = checkBox;
        this.name = name;
        this.circle = new Circle(7);
        this.circle.setFill(Color.rgb(250, 250, 0));
        this.circle.setStroke(Color.rgb(0, 0, 0));
        this.circle.setStrokeWidth(1);
        HBox.setHgrow(this.checkBox , Priority.ALWAYS);
        VBox.setVgrow(this.checkBox , Priority.ALWAYS);
        this.checkBox.setMaxWidth(Double.MAX_VALUE);

    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCircleValue(Color color) {
        circle.setFill(color);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
