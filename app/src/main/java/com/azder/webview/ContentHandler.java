package com.azder.webview;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by zhou6 on 2017/2/13.
 */
public class ContentHandler extends DefaultHandler{
    private String nodeName;
    private StringBuilder name;
    private StringBuilder age;
    private StringBuilder sex;
    private static final String TAG = "CONTENT";


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        name = new StringBuilder();
        age = new StringBuilder();
        sex = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("student".equals(localName)){
            Log.d(TAG, "name is" + name.toString().trim());
            Log.d(TAG, "age is" + age.toString().trim());
            Log.d(TAG, "sex is" + sex.toString().trim());
            name.setLength(0);
            age.setLength(0);
            sex.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if ("name".equals(nodeName)){
            name.append(ch, start, length);
        } else if ("age".equals(nodeName)){
            age.append(ch, start, length);
        } else if ("sex".equals(nodeName)){
            sex.append(ch, start, length);
        }
    }
}
