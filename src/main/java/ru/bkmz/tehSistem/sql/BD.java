package ru.bkmz.tehSistem.sql;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


//Этот класс подключает БД
public class BD {
    private Connection conn;

    public static final Logger logger = LogManager.getLogger();

    public BD(String fileConn) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        logger.info(fileConn);
        conn = DriverManager.getConnection("jdbc:sqlite:" + fileConn);


    }

    public void setBD(String inquiry) throws SQLException {

        Statement statmt = conn.createStatement();

        statmt.execute(inquiry);

        statmt.close();

    }


    public Connection getConn() {
        return conn;
    }

    public void start() throws SQLException {
        try {
            Statement statmt = conn.createStatement();


            statmt.execute("create table IP ( " +
                    "id INTEGER not null constraint IP_pk primary key autoincrement," +
                    " ip TEXT not null, " +
                    "boll BOOLEAN default false ); " +
                    "insert into IP(id, ip, boll) select id, ip, boll from IP; drop table IP; alter table IP rename to IP;");


            statmt.execute("create table settings ( " +
                    "id INTEGER default 0 not null constraint settings_pk primary key autoincrement, " +
                    "FILE_OUT TEXT," +
                    " FILE_IN TEXT," +
                    "DISK default 'D');" +
                    " create unique index settings_id_uindex on settings (id);");

            statmt.execute("INSERT INTO settings ('FILE_OUT','FILE_IN','DISK') VALUES ('','','D');");

            statmt.execute("create table cmd (" +
                    " Id INTEGER default 0 not null constraint cmd_pk primary key autoincrement " +
                    "); " +
                    "create unique index cmd_Id_uindex on cmd (Id);");

            statmt.execute("alter table cmd add '++' BOOLEAN default false;");

            statmt.close();
        } catch (Exception e) {
            logger.error("SQL:", e);
        }
    }
}
