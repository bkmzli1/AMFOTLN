package ru.bkmz.sql;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.bkmz.util.Decoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


//Этот класс подключает БД
public class BD {
    private Connection conn;

    public static final Logger logger = LogManager.getLogger();
    String[] cmdArg = {"a", "m", "s", "e", "v","i","f","l","g","h","r","t","u","k","o","z","b","j"};
    String[] cedArcBoolean = {"s","h","i"};

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

            addIdSettings(statmt, "discList", "D");
            addIdSettings(statmt, "fileIN");
            addIdSettings(statmt, "fileOUT");
            addIdSettings(statmt, "timeIP","10");
            addIdSettings(statmt, "timeIpConnect","10");

            statmt.execute("create table cmdArg ( " +
                    "id INTEGER default 0 not null constraint settings_pk primary key autoincrement, " +
                    "arg TEXT not null, " +
                    "value BOOLEAN " +
                    ");" +
                    "create unique index settings_arg_uindex on cmdArg (arg); " +
                    "create unique index settings_id_uindex on cmdArg (id);");

            for (String arg :
                    cmdArg) {
                boolean b = false;
                for (String argB :
                        cedArcBoolean) {
                    if (arg.equals(argB)) {
                        b = true;
                        break;
                    }
                }

                if (b) {
                    statmt.execute("INSERT INTO cmdArg ('arg','value') VALUES ('" + arg + "','" + 1 + "');");
                } else {
                    statmt.execute("INSERT INTO cmdArg ('arg','value') VALUES ('" + arg + "','" + 0 + "');");
                }
            }

            statmt.close();
        } catch (Exception e) {
            logger.error("SQL:", e);
        }

    }

    void addIdSettings(Statement statmt, String arg, String value) {
        try {
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('" + arg + "','" + value + "');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void addIdSettings(Statement statmt, String arg) {
        try {
            statmt.execute("INSERT INTO settings ('arg','value') VALUES ('" + arg + "','');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

