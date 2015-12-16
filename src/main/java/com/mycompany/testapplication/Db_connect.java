package com.mycompany.testapplication;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ruslan
 */
public class Db_connect {

    private static Connection con = null;
    private static final Logger log = Logger.getLogger(Db_connect.class);
    
    private Db_connect(){
        try{
            log.info("Connect to data base...");
            con = DriverManager.getConnection(
                    Config.getProp(Constant.URL),
                    Config.getProp(Constant.USER),
                    "");
            log.info("Connect success!");
        }catch(SQLException ex){
            log.error("error for connect to data base "+ ex.getMessage());
        }
    }
    
    public static Connection getConn(){
        if(con == null)
            return new Db_connect().con;
        return con;
    }
}
