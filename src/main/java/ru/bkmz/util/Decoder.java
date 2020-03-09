package ru.bkmz.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Decoder {
    public static final Logger logger = LogManager.getLogger();

    public static String UTFtoWin1251(String s) {
        String line, out = "";
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)), "windows-1251"));

            while ((line = b.readLine()) != null) {
                out += line;
            }

        } catch (Exception e) {
            logger.error("Decoder:", e);
        }
        return out;
    }
}
