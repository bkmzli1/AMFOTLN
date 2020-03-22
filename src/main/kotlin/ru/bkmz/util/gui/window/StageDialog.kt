package ru.bkmz.util.gui.window


import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.stage.Modality
import javafx.stage.Stage
import ru.bkmz.Main
import java.io.IOException
import java.util.*


class StageDialog(nameFile: String, nameWindows: String?, show: Boolean) {
    var newWindow = Stage()

    fun show() {
        newWindow.show()
    }

    init {
        val loader = FXMLLoader()
        loader.location = Objects.requireNonNull(javaClass.classLoader.getResource("fxml/$nameFile.fxml"))
        val secondaryLayout = StackPane()
        val secondScene = Scene(secondaryLayout)
        newWindow.scene = secondScene
        newWindow.initModality(Modality.APPLICATION_MODAL)
        // Specifies the modality for new window.
// Specifies the owner Window (parent) for new window
        newWindow.initOwner(Main.stage)
        // Set position of second window, related to primary window.
        try {
            loader.load<Any>()
        } catch (e: IOException) {
        }
        val root = loader.getRoot<Parent>()
        val inputStream = ClassLoader::class.java.getResourceAsStream("/img/icon.png")
        try {
            val image = Image(inputStream)
            newWindow.icons.add(image)
        } catch (e: NullPointerException) {
        }
        val scene = Scene(root)
        scene.stylesheets
            .add(Objects.requireNonNull(javaClass.classLoader.getResource("css/main.css")).toExternalForm())
        newWindow.scene = scene
        newWindow.isResizable = false
        newWindow.onCloseRequest = EventHandler { }
        newWindow.title = nameWindows
        if (show) show()
    }
}
