package com.mycompany.testapplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author ruslan
 */
public class Test {

    private static final Logger log = Logger.getLogger(Test.class);
    private Connection conn;
    private int n;
    Statement stat;

    DocumentBuilder builder;

    public void insertEntry() {
        if (conn != null) {
            try {
                stat = conn.createStatement();
                stat.getConnection().setAutoCommit(false);
                stat.execute(Config.getProp(Constant.CREATE));
                stat.execute(Config.getProp(Constant.TRUNCATE));
                String query;
                int batchSize = Integer.parseInt(Config.getProp(Constant.BATCHSIZE));
                for (int i = 1; i <= n; i++) {
                    query = Config.getProp(Constant.INSERT).replace("number", Integer.toString(i));
                    stat.addBatch(query);
                    if (i % batchSize == 0) {
                        stat.executeBatch();
                    }
                }
                stat.executeBatch();
                stat.execute("commit");
                log.info("rows inserted");

            } catch (Exception ex) {
                log.error("error for insert " + ex.getMessage());
            }
        } else {
            log.error("no connecton to data base");
        }
    }

    public void writeXML() throws TransformerException, IOException {
        ResultSet rs = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            log.error("newDocumentBuilder error " + ex.getMessage());
        }

        Document doc = builder.newDocument();
        Element RootElement = doc.createElement("entries");
        Element entry, field;

        rs = getValues();
        try {
            if (rs != null) {
                while (rs.next()) {
                    entry = doc.createElement("entry");
                    RootElement.appendChild(entry);
                    field = doc.createElement("field");
                    field.appendChild(doc.createTextNode(rs.getString("field")));
                    entry.appendChild(field);
                }
                stat.execute("shutdown defrag");
                stat.close();
                conn.close();
                
            } else {
                log.error("table TEST is empty");
            }
        } catch (SQLException | DOMException ex) {
            log.error("error for result set " + ex.getMessage());
            ex.printStackTrace();
        }
        doc.appendChild(RootElement);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        //t.setOutputProperty(OutputKeys.METHOD, "xml");
        //t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(Config.getProp(Constant.XML1) + Config.getProp(Constant.DOTXML))));

    }

    public ResultSet getValues() {
        try {
            PreparedStatement ps = conn.prepareStatement(Config.getProp(Constant.SELECT));
            return ps.executeQuery();
        } catch (SQLException ex) {
            log.error("error for select " + ex.getMessage());
        }
        return null;
    }

    public void transform() throws TransformerConfigurationException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File(Config.getProp(Constant.TRANSFORM) + Config.getProp(Constant.DOTXSL)));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new File(Config.getProp(Constant.XML1) + Config.getProp(Constant.DOTXML)));
        transformer.transform(text, new StreamResult(new File(Config.getProp(Constant.XML2) + Config.getProp(Constant.DOTXML))));
    }

    public double parseXML() throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SaxParse saxp = new SaxParse();

        parser.parse(new File(Config.getProp(Constant.XML2) + Config.getProp(Constant.DOTXML)), saxp);
        return saxp.getSumm();
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
