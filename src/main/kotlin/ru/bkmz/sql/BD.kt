package ru.bkmz.sql

import org.apache.logging.log4j.LogManager
import ru.bkmz.util.Decoder.UTFtoWin1251
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

//Этот класс подключает БД
class BD(fileConn: String) {
    val conn: Connection

    var cmdArg =
        arrayOf("a", "m", "s", "e", "v", "i", "f", "l", "g", "h", "r", "t", "u", "k", "o", "z", "b", "j")
    var cedArcBoolean = arrayOf("s", "h", "i")

    @Throws(SQLException::class)
    fun start() {
        try {
            val statmt = conn.createStatement()
            statmt.execute(
                "create table IP ( " +
                        "id INTEGER not null constraint IP_pk primary key autoincrement," +
                        "ip TEXT not null," +
                        "name text not null , " +
                        "boll BOOLEAN default false ); " +
                        "insert into IP(id, ip, boll) select id, ip, boll from IP; drop table IP; alter table IP rename to IP;"
            )
            statmt.execute(
                "create table settings ( " +
                        "id INTEGER default 0 not null constraint settings_pk primary key autoincrement, " +
                        "arg TEXT not null, " +
                        "value TEXT " +
                        ");" +
                        "create unique index settings_arg_uindex on settings (arg); " +
                        "create unique index settings_id_uindex on settings (id);"
            )
            addIdSettings(statmt, "discList", "D")
            addIdSettings(statmt, "fileIN")
            addIdSettings(statmt, "fileOUT")
            addIdSettings(statmt, "timeIP", "10")
            addIdSettings(statmt, "timeIpConnect", "10")
            statmt.execute(
                "create table cmdArg ( " +
                        "id INTEGER default 0 not null constraint settings_pk primary key autoincrement, " +
                        "arg TEXT not null, " +
                        "value BOOLEAN " +
                        ");" +
                        "create unique index settings_arg_uindex on cmdArg (arg); " +
                        "create unique index settings_id_uindex on cmdArg (id);"
            )
            for (arg in cmdArg) {
                var b = false
                for (argB in cedArcBoolean) {
                    if (arg == argB) {
                        b = true
                        break
                    }
                }
                if (b) {
                    statmt.execute("INSERT INTO cmdArg ('arg','value') VALUES ('$arg','1');")
                } else {
                    statmt.execute("INSERT INTO cmdArg ('arg','value') VALUES ('$arg','0');")
                }
            }
            statmt.close()
        } catch (e: Exception) {
            logger.error("SQL:", e)
        }
    }

    fun addIdSettings(statmt: Statement, arg: String, value: String) {
        try {
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('$arg','$value');")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun addIdSettings(statmt: Statement, arg: String) {
        try {
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('$arg','');")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    companion object {
        val logger = LogManager.getLogger()
    }

    init {
        Class.forName("org.sqlite.JDBC")
        logger.info(UTFtoWin1251(fileConn))
        conn = DriverManager.getConnection("jdbc:sqlite:$fileConn")
    }
}