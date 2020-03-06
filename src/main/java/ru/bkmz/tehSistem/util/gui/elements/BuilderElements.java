package ru.bkmz.tehSistem.util.gui.elements;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuilderElements {
    public static final Logger logger = LogManager.getLogger();

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


    public static void textPropertyTime(TextField textField, int size, int maxNumber) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[\\d]")) {
                    newValue = newValue.replaceAll("[^\\d]", "");
                    if (newValue.length() > size) {
                        newValue = newValue.substring(0, size);
                    }
                    try {
                        if (Integer.parseInt(newValue) > maxNumber) {
                            newValue = newValue.substring(0, newValue.length() - 1);
                        }
                    } catch (NumberFormatException nfe) {

                    }

                    textField.setText(newValue);
                }
            }
        });
    }

}
