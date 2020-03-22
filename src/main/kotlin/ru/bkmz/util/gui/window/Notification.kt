package ru.bkmz.util.gui.window

import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import org.apache.logging.log4j.LogManager
import ru.bkmz.util.Decoder.UTFtoWin1251


class Notification {
    constructor(name: String, info: String) {
        Platform.runLater {
            logger.info(UTFtoWin1251("$name:$info"))
            val alert = Alert(AlertType.INFORMATION)
            alert.title = name
            alert.headerText = null
            alert.contentText = info
            alert.showAndWait()
        }
    }

    constructor(name: String, info: String, alert: AlertType) {
        Platform.runLater {
            logger.info(UTFtoWin1251("$name:$info:$alert"))
            val nAlert = Alert(alert)
            nAlert.title = name
            nAlert.headerText = null
            nAlert.contentText = info
            nAlert.showAndWait()
            nAlert.buttonTypes.removeAll()
        }
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}
