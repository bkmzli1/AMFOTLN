package ru.bkmz.tehSistem.util.gui.elements;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuilderElements {
    public static final Logger logger = LogManager.getLogger();

    private static TextArea textAreaBuild(String value) {
        TextArea textArea = new TextArea(value);

        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        HBox.setHgrow(textArea, Priority.ALWAYS);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        return textArea;
    }

    public static TextField textFieldBuild(String value, String promptText) {
        TextField textField = new TextField(value);
        textField.setPromptText(promptText);
        textField.setAlignment(Pos.CENTER);
        HBox.setHgrow(textField, Priority.ALWAYS);
        VBox.setVgrow(textField, Priority.ALWAYS);
        //textField.setId("text");
        textProperty(textField);
        return textField;
    }

    public static TextField textFieldBuild(String value, String promptText, String help) {
        TextField textField = new TextField(value);
        textField.setPromptText(promptText);
        textField.setAlignment(Pos.CENTER);
        Tooltip tooltip = new Tooltip(help);
        Tooltip.install(textField, tooltip);
        HBox.setHgrow(textField, Priority.ALWAYS);
        VBox.setVgrow(textField, Priority.ALWAYS);
        //textField.setId("text");
        textProperty(textField);
        return textField;
    }

    public static Text textBuild(String value) {
        Text text = new Text(value.replace("*", "×").replace("/", "÷"));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setId("text");
        HBox.setHgrow(text, Priority.ALWAYS);
        VBox.setVgrow(text, Priority.ALWAYS);
        return text;
    }


    public static Label labelBuild(String value) {

        Label label = new Label(value.replace("*", "×").replace("/", "÷").replace("÷÷", "/"));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setId("text");
        HBox.setHgrow(label, Priority.ALWAYS);
        VBox.setVgrow(label, Priority.ALWAYS);
        return label;
    }


    private static void textProperty(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[\\d,.-]")) {
                    textField.setText(newValue.replaceAll("[^\\d,.-]", ""));
                }
            }
        });
    }

    public static void textProperty(TextField textField, int size) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[\\d]")) {
                    newValue = newValue.replaceAll("[^\\d]", "");
                    if (newValue.length() > size) {
                        newValue = newValue.substring(0, size);
                    }
                    textField.setText(newValue);
                }
            }
        });
    }

    public static Button buttonBuild(String value) {
        Button button = new Button(value);
        button.setAlignment(Pos.CENTER);
        button.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(button, Priority.ALWAYS);
        VBox.setVgrow(button, Priority.ALWAYS);
        return button;
    }

    public static HBox hBoxBulder() {
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(hBox, Priority.SOMETIMES);
        VBox.setVgrow(hBox, Priority.SOMETIMES);
        return hBox;
    }

    public static VBox vBoxBulder() {
        VBox hBox = new VBox(10);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(hBox, Priority.SOMETIMES);
        VBox.setVgrow(hBox, Priority.SOMETIMES);
        return hBox;
    }

    public static void infBilder(VBox vBox, String infs) {
        TextArea inf = textAreaBuild(infs);
        HBox.setHgrow(inf, Priority.ALWAYS);
        VBox.setVgrow(inf, Priority.ALWAYS);
        vBox.getChildren().add(inf);
    }
}
