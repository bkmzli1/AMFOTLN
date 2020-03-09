package ru.bkmz.util;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.controller.ControllerMain;
import ru.bkmz.util.gui.window.Notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;

import static ru.bkmz.Main.bd;


public class CmdHandler {
    public static final Logger logger = LogManager.getLogger();

    public void cmdRun(String file, String ip, String disk, String fileIP, CheckBox IE) throws Exception {


        Runtime rt = Runtime.getRuntime();
        String command;
        String args = "";

        ResultSet resultSet =  bd.getConn().createStatement().executeQuery("SELECT * FROM 'settings' where id >= " + Integer.parseInt(bd.getConn().createStatement().executeQuery("SELECT * FROM 'settings' where arg = 'settingsId'").getString("value")) + " AND value >= 1");

        while (resultSet.next()){
            args += " /"+resultSet.getString("arg");
        }
        command = "cmd.exe /c xcopy \"" + file + "\" \"\\\\" + ip + "\\" + disk + "\\" + fileIP + "\" /y "+args;
        logger.info("launch:" + command);

        Process proc = rt.exec(command);
        if (IE.isSelected()) {
            cmdOut(proc, ip);
        }
        ControllerMain.addProcent();
        logger.info("stop ip:" + ip);
    }

    public static void cmdOut(Process proc, String ip) throws Exception {
        String out = "", outerror = "", line;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream(), "Cp866"));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "Cp866"));
        proc.waitFor();
        proc.destroy();

        while ((line = stdInput.readLine()) != null) {
            out += line + "\n";
        }
        if (!out.equals("")) {
            new Notification("IP:" + ip, out);
            logger.info("cmd out:", out);


        }

        while ((line = stdError.readLine()) != null) {
            outerror += line + "\n";
        }
        if (!outerror.equals("")) {
            new Notification("IP:" + ip, outerror, Alert.AlertType.ERROR);
            logger.warn("cmd out error:", outerror);
        }

    }
}
