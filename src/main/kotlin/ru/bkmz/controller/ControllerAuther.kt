package ru.bkmz.controller

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import org.apache.logging.log4j.LogManager
import ru.bkmz.util.gui.elements.WrappedImageView


class ControllerAuther {
    lateinit var vBoxAether: VBox
    lateinit  var vBoxGit: VBox
    lateinit   var vBoxVK: VBox
    lateinit  var vBoxGoogle: VBox
    lateinit  var title: Label
    private val inputStreamAether =
        ClassLoader::class.java.getResourceAsStream("/img/fon1.png")
    private val inputStreamGit =
        ClassLoader::class.java.getResourceAsStream("/img/github_PNG20.png")
    private val inputStreamVK =
        ClassLoader::class.java.getResourceAsStream("/img/vk.png")
    private val inputStreamGoogle =
        ClassLoader::class.java.getResourceAsStream("/img/gmail.png")
    private val imageAether = Image(inputStreamAether)
    private val imageGit = Image(inputStreamGit)
    private val imageVk = Image(inputStreamVK)
    private val imageGoogle = Image(inputStreamGoogle)
    fun initialize() {
        logger.info("start initialize FXML ")
        title.id = "title"
        imageBuilder(imageGit, "https://github.com/bkmzli1", vBoxGit)
        imageBuilder(imageAether, "https://github.com/bkmzli1", vBoxAether)
        imageBuilder(imageVk, "https://vk.com/id409602224", vBoxVK)
        imageBuilder(imageGoogle, "https://myaccount.google.com/?utm_source=OGB&tab=mk&utm_medium=act", vBoxGoogle)
        logger.info("stop initialize FXML ")
    }

    private fun imageBuilder(image: Image, url: String, vBox: VBox?) {
        val imageV: WrappedImageView
        imageV = WrappedImageView(image, url)
        vBox!!.children[0] = imageV
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}
