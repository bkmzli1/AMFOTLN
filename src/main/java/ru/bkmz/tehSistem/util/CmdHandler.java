package ru.bkmz.tehSistem.util;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.util.gui.window.Notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static ru.bkmz.tehSistem.Main.logger;
import static ru.bkmz.tehSistem.controller.ControllerMain.addProcent;


public class CmdHandler {
    public static final Logger logger = LogManager.getLogger();

    public void cmdRun(String file, String ip, String disk, String fileIP, CheckBox IE) throws Exception {


        Runtime rt = Runtime.getRuntime();
        String command;

        command = "cmd.exe /c xcopy \"" + file + "\" \"\\\\" + ip + "\\" + disk + "\\" + fileIP + "\" /s /e /h /y /i";
        logger.info("launch:" + command);

        Process proc = rt.exec(command);
        cmdOut(proc, ip, IE);
        addProcent();
        logger.info("stop ip:" + ip);
    }

    public static void cmdOut(Process proc, String ip, CheckBox IE) throws Exception {
        String out = "", outerror = "", line;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream(), "Cp866"));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "Cp866"));
        proc.waitFor();
        proc.destroy();
        if (IE.isSelected()) {
            while ((line = stdInput.readLine()) != null) {
                out += line + "\n";
            }
            if (!out.equals("")) {
                new Notification("IP:" + ip, out);
                logger.info("cmd out:", out);
            }

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
