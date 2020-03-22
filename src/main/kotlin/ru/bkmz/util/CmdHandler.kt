package ru.bkmz.util


import javafx.scene.control.Alert
import javafx.scene.control.CheckBox
import org.apache.logging.log4j.LogManager
import ru.bkmz.Main.Companion.bd
import ru.bkmz.controller.ControllerMain
import ru.bkmz.util.Decoder.UTFtoWin1251
import ru.bkmz.util.gui.window.Notification
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.sql.ResultSet


class CmdHandler {

    @Throws(Exception::class)
    fun cmdRun(
        file: String,
        ip: String,
        disk: String,
        fileIP: String,
        IE: CheckBox
    ) {
        val rt = Runtime.getRuntime()
        val command: String
        var args = ""
        val resultSet: ResultSet = bd?.conn?.createStatement()?.executeQuery(
            "SELECT * FROM 'settings' where id >= " + bd!!.conn.createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'settingsId'").getString(
                "value"
            ).toInt() +
                    " AND value >= 1"
        )!!
        while (resultSet.next()) {
            args += " /" + resultSet.getString("arg")
        }

        val f = File(file)
        if (f.isFile) {
            args = args.replace("/s", "")
        } else if (f.isDirectory) {
        }
        command = "cmd.exe /c xcopy $file \\\\$ip\\$disk\\$fileIP  /y $args"
        logger.info("launch:$command")
        val proc = rt.exec(command)
        cmdOut(proc, ip, IE)
        ControllerMain.addProcent()
        logger.info("stop ip:$ip")
    }

    companion object {
        val logger = LogManager.getLogger()
        @Throws(Exception::class)
        fun cmdOut(proc: Process, ip: String, IE: CheckBox) {
            logger.trace("scan start $ip")
            var out = ""
            var outerror = ""
            var line: String
            val stdInput = BufferedReader(InputStreamReader(proc.inputStream, "Cp866"))
            val stdError = BufferedReader(InputStreamReader(proc.errorStream, "Cp866"))
            proc.waitFor()
            proc.destroy()
            while (stdInput.readLine().also { line = it } != null) {
                out += line + "\n"
            }
            if (out != "") {
                if (IE.isSelected) Notification("IP:$ip", out)
                logger.info("cmd out:" + UTFtoWin1251(out))
            }
            while (stdError.readLine().also { line = it } != null) {
                outerror += line + "\n"
            }
            if (outerror != "") {
                if (!outerror.contains("уже существует")) {
                    if (IE.isSelected) Notification("IP:$ip", outerror, Alert.AlertType.ERROR)
                    logger.warn("cmd out error:" + UTFtoWin1251(outerror))
                }
            }
            logger.trace("scan stop $ip")
        }
    }
}
