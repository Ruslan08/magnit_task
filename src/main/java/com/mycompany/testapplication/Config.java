package com.mycompany.testapplication;

import java.io.IOException;
import org.apache.log4j.Logger;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ruslan
 */
public class Config {

    private static Properties prop;

    private static final Logger log = Logger.getLogger(Config.class);

    private Config() {
        prop = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream(Constant.CONFIG);
        if (is != null) {
            try {
                prop.load(is);
                log.debug("Prop are loaded");
            } catch (IOException ex) {
                log.error("Error for loading properties " + ex.getMessage());
            }
        } else {
            log.error("File not found");
            System.exit(1);
        }
    }
    
    public static String getProp(String property){
        if(prop==null)
            new Config();
        return prop.getProperty(property);
    }
}
