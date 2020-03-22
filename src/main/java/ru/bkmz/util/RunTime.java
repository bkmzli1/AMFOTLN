package ru.bkmz.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class RunTime {
    double startTime;
    double timeSpent;
    private static final Logger logger = LogManager.getLogger();

    public RunTime() {
        this.startTime = System.currentTimeMillis();
        ;
    }

    public void stop(String name) {
        timeSpent = System.currentTimeMillis();
        BigDecimal bigDecimal = new BigDecimal(timeSpent);
        bigDecimal = bigDecimal.subtract(BigDecimal.valueOf(startTime));
        bigDecimal = bigDecimal.divide(BigDecimal.valueOf(1000));
        logger.trace(Decoder.UTFtoWin1251("время работы "+name+": ") + bigDecimal.toString() + Decoder.UTFtoWin1251(" секунды"));
    }
}
