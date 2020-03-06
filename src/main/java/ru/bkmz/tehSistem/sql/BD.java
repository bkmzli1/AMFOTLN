package ru.bkmz.tehSistem.sql;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.tehSistem.util.Decoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


//Этот класс подключает БД
public class BD {
    private Connection conn;

    public static final Logger logger = LogManager.getLogger();

    public BD(String fileConn) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");

        logger.info(Decoder.UTFtoWin1251(fileConn));
        conn = DriverManager.getConnection("jdbc:sqlite:" + fileConn);


    }

    public Connection getConn() {
        return conn;
    }

    public void start() throws SQLException {
        try {
            Statement statmt = conn.createStatement();


            statmt.execute("create table IP ( " +
                    "id INTEGER not null constraint IP_pk primary key autoincrement," +
                    "ip TEXT not null," +
                    "name text not null , " +
                    "boll BOOLEAN default false ); " +
                    "insert into IP(id, ip, boll) select id, ip, boll from IP; drop table IP; alter table IP rename to IP;");


            statmt.execute("create table settings ( " +
                    "id INTEGER default 0 not null constraint settings_pk primary key autoincrement, " +
                    "arg TEXT not null, " +
                    "value TEXT " +
                    ");" +
                    "create unique index settings_arg_uindex on settings (arg); " +
                    "create unique index settings_id_uindex on settings (id);");
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('discList','D');");
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('fileIN','');");
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('fileOUT','');");
            statmt.close();
        } catch (Exception e) {
            logger.error("SQL:", e);
        }
    }
}

