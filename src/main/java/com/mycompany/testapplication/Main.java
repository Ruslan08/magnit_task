package com.mycompany.testapplication;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author ruslan
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class);
    
      public static void main(String[] a) throws TransformerException, IOException, SAXException, ParserConfigurationException  {
       Test test = new Test();
       
       test.setConn(Db_connect.getConn());  //set connection
       test.setN(1);      //set N
       
       test.insertEntry();  //insert entry to data base
       
       test.writeXML();     //get entry from data base and insert to 1.xml
          
       test.transform();    //transform 1.xml to 2.xml
      
       log.info("RESULT = "+test.parseXML());   //get sum
        
    }
}
