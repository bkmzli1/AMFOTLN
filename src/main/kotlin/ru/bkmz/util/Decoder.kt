package ru.bkmz.util

import org.apache.logging.log4j.LogManager
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


object Decoder {
    val logger = LogManager.getLogger()
    fun UTFtoWin1251(s: String): String? {
        var line: String?
        var out: String? = ""
        try {
            val b = BufferedReader(
                InputStreamReader(
                    ByteArrayInputStream(
                        s.toByteArray(
                            StandardCharsets.UTF_8
                        )
                    ), "windows-1251"
                )
            )
            while (b.readLine().also { line = it } != null) {
                out += line
            }
        } catch (e: Exception) {
            logger.error("Decoder:", e)
        }
        return out
    }
}
