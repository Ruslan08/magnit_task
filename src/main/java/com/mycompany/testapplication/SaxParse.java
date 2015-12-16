package com.mycompany.testapplication;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author ruslan
 */
public class SaxParse extends DefaultHandler {
    double summ = 0;

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals("entry")) {
            summ += Double.valueOf(atts.getValue(0));
            //summ += Double.valueOf(atts.getValue(0).replace("\r", "").replace("\n", ""));
        }
    }

    public double getSumm() {
        return summ;
    }
}
