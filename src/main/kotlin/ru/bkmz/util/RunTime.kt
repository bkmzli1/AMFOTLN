package ru.bkmz.util

import org.apache.logging.log4j.LogManager
import java.math.BigDecimal


class RunTime {
    var startTime: Double
    var timeSpent = 0.0
    fun stop(name: String) {
        timeSpent = System.currentTimeMillis().toDouble()
        var bigDecimal = BigDecimal(timeSpent)
        bigDecimal = bigDecimal.subtract(BigDecimal.valueOf(startTime))
        bigDecimal = bigDecimal.divide(BigDecimal.valueOf(1000))
        logger.trace(
            Decoder.UTFtoWin1251("время работы $name: ") + bigDecimal.toString() +Decoder.UTFtoWin1251(
                " секунды"
            )
        )
    }

    companion object {
        private val logger = LogManager.getLogger()
    }

    init {
        startTime = System.currentTimeMillis().toDouble()
    }
}
