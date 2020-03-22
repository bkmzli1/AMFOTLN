package ru.bkmz.util.gui.elements

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.effect.Glow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import org.apache.logging.log4j.LogManager
import java.awt.Desktop
import java.net.URL


class WrappedImageView(image: Image?, url: String) :
    ImageView(image) {
    private var off: Thread? = null
    private var on: Thread? = null
    private val time = 50
    override fun minWidth(height: Double): Double {
        return 40.0
    }

    override fun prefWidth(height: Double): Double {
        val I = image ?: return minWidth(height)
        return I.width
    }

    override fun maxWidth(height: Double): Double {
        return 16384.0
    }

    override fun minHeight(width: Double): Double {
        return 40.0
    }

    override fun prefHeight(width: Double): Double {
        val I = image ?: return minHeight(width)
        return I.height
    }

    override fun maxHeight(width: Double): Double {
        return 16384.0
    }

    override fun isResizable(): Boolean {
        return true
    }

    override fun resize(width: Double, height: Double) {
        fitWidth = width
        fitHeight = height
    }

    companion object {
        val logger = LogManager.getLogger()
        private fun openWebpage(urlString: String) {
            try {
                Desktop.getDesktop().browse(URL(urlString).toURI())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        onMouseEntered = EventHandler {
            try {
                Thread(Runnable {
                    try {
                        try {
                            on!!.join()
                            off!!.join()
                        } catch (ignored: Exception) {
                        }
                        on = Thread(Runnable {
                            var i = 0.0
                            while (i < 1.0) {
                                val finalI = i
                                try {
                                    Thread.sleep(time.toLong())
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                Platform.runLater { effect = Glow(finalI) }
                                i += 0.2
                            }
                        })
                        on!!.start()
                    } catch (ignored: Exception) {
                    }
                }).start()
            } catch (ignored: Exception) {
            }
        }
        onMouseExited = EventHandler {
            try {
                Thread(Runnable {
                    try {
                        try {
                            on!!.join()
                            off!!.join()
                        } catch (ignored: Exception) {
                        }
                        off = Thread(Runnable {
                            var i = 1.0
                            while (i > 0.0) {
                                try {
                                    Thread.sleep(time.toLong())
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                val finalI = i
                                Platform.runLater { effect = Glow(finalI) }
                                i -= 0.2
                            }
                        })
                        off!!.start()
                    } catch (ignored: Exception) {
                    }
                }).start()
            } catch (ignored: Exception) {
            }
        }
        onMouseClicked = EventHandler {
            openWebpage(
                url
            )
        }
        isPreserveRatio = true
    }
}