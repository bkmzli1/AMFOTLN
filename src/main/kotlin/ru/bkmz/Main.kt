package ru.bkmz

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.apache.logging.log4j.LogManager
import ru.bkmz.controller.ControllerMain
import ru.bkmz.sql.BD
import ru.bkmz.util.gui.window.Notification
import java.awt.*
import java.io.File
import java.io.IOException
import java.net.BindException
import java.net.ServerSocket
import java.util.*
import kotlin.system.exitProcess

class Main : Application() {

    var window = Thread()
    var windowsB = false


    override fun init() {
        var file = File(FileSeve)
        if (!file.exists()) {
            file.mkdir()
        }
        try {
            file = File(SQLFile)
            val f = !file.exists()
            bd = BD(SQLFile)
            if (f) {
                bd!!.start()
            }
        } catch (e: Exception) {
            logger.error("BD:", e)
        }
    }

    override fun start(hideStage: Stage) {
        hideStage.initStyle(StageStyle.UTILITY)
        hideStage.width = 0.0
        hideStage.height = 0.0
        hideStage.maxWidth = 0.0
        hideStage.minWidth = 0.0
        hideStage.maxHeight = 0.0
        hideStage.minHeight = 0.0
        hideStage.opacity = 0.0

        hideStage.show()
        try {
            stage()
        } catch (e: NullPointerException) {
            logger.error("hideStage is not loaded", e)
        }
        tryMenu()
    }

    fun tryMenu() {
        val trayMenu = PopupMenu()
        var item = MenuItem("завершить")
        item.addActionListener { System.exit(0) }
        trayMenu.add(item)
        item = MenuItem("открыть")
        item.addActionListener { openWindow() }
        trayMenu.add(item)
        val inputStream = ClassLoader::class.java.getResource("/img/icon.png")
        val icon = Toolkit.getDefaultToolkit().getImage(inputStream)
        val trayIcon = TrayIcon(icon, APPLICATION_NAME, trayMenu)
        trayIcon.isImageAutoSize = true
        val tray = SystemTray.getSystemTray()
        try {
            tray.add(trayIcon)
        } catch (e: AWTException) {
            e.printStackTrace()
        }
        trayIcon.displayMessage(
            APPLICATION_NAME, "Application started!",
            TrayIcon.MessageType.INFO
        )
    }

    fun stage() {
        logger.info("start loader FXML")
        val loader = FXMLLoader()
        loader.location = Objects.requireNonNull(javaClass.classLoader.getResource("fxml/main.fxml"))
        try {
            loader.load<Any>()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        val root = loader.getRoot<Parent>()
        logger.info("stop loader FXML")
        val scene = Scene(root)
        scene.stylesheets
            .add(Objects.requireNonNull(javaClass.classLoader.getResource("css/main.css")).toExternalForm())
        stage.scene = scene
        stage.title = APPLICATION_NAME
        val inputStream = ClassLoader::class.java.getResourceAsStream("/img/icon.png")
        try {
            val image = Image(inputStream)
            stage.icons.add(image)
        } catch (ne: NullPointerException) {
            println("icon null")
        }
        stage.onCloseRequest = EventHandler {
            if (ControllerMain.sizeEnd >= ControllerMain.threads.size) {
                logger.info("Platform exit")
                Platform.exit()
            } else {
                window.run { stop() }
                windowsB = false
            }
        }
        val sSize = Toolkit.getDefaultToolkit().screenSize
        stage.height = sSize.height * 60.0 / 100.0
        stage.width = sSize.width * 60.0 / 100.0
        stage.minHeight = sSize.height * 20.0 / 100.0
        stage.minWidth = sSize.width * 20.1 / 100.0
        close()

        logger.info("show")
        stage.show()
        windowsB = true
    }


    private fun openWindow() {
        if (!windowsB) {
            window = Thread(Runnable {
                Platform.runLater {
                    windowsB = true
                    stage.show()
                }
            }, "windowOpen")
            window.start()
        } else {
            Notification("", "Окно уже открыто")
        }
    }

    companion object {
        var splashScreen = SplashScreen.getSplashScreen()
        var stage = Stage()
        const val name = "AMFOTLN"
        private val logger = LogManager.getLogger()
        var FileSeve = System.getenv("APPDATA") + "\\AMFOTLN"
        var SQLFile = "$FileSeve\\AMFOTLN.db"
        var bd: BD? = null
        const val APPLICATION_NAME = name
        var start: Boolean = false
        var open: Boolean = false
        val t = Thread(Runnable {
            try {
                var serverSocket = ServerSocket(1921)
                serverSocket.accept()
                logger.info(serverSocket.inetAddress)
                splashScreen.close()
            } catch (e: BindException) {
                open = true
                close()
                Notification("лаунчер", "Программа уже запущена");
                Thread.sleep(10000)
                exitProcess(0)
            }
        })

        fun close() {
            try {
                splashScreen.close()
            } catch (e: NullPointerException) {
                logger.error("splashScreen is not loaded", e)
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            for (arg in args) {
                logger.trace(arg)
            }
            try {

                logger.info("start:open")
                t.start()
                while (true) {
                    if (start) {
                        logger.info("start:launch")
                        launch(Main::class.java)
                    } else {
                        Thread.sleep(500)
                        if (!open)
                            start = true
                    }
                }

            } catch (e: Exception) {
                logger.trace("", e)
            }
            logger.info("exit")
            exitProcess(0)
        }
    }

}