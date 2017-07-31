package com.tg.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;

/**
 * Created by twogoods on 2017/7/28.
 */
public class XmlUtils {
    public static Element generateMybatisXmlFrame(String daoName) throws SAXException, DocumentException {
        String frame = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "\n" +
                "<!DOCTYPE mapper\n" +
                "PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "\n" +
                "<mapper namespace=\"" + daoName + "\">\n" +
                "</mapper>\n";
        Document document = parseText(frame);
        Element rootElement = document.getRootElement();
        return rootElement;
    }

    public static void writeFile(String fileName, String relativePath, Document document) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        FileWriter fileWriter = null;
        try {
            File dir = new File(XmlUtils.class.getResource("/").getPath() + relativePath.replace(".", "/"));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName + ".xml");
            fileWriter = new FileWriter(file);
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (IOException e) {
            //TODO 处理
            e.printStackTrace();
        }
    }


    public static Document parseText(String text) throws DocumentException, SAXException {
        SAXReader reader = new SAXReader(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        reader.setFeature("http://apache.org/xml/features/validation/schema", false);
        String encoding = getEncoding(text);

        InputSource source = new InputSource(new StringReader(text));
        source.setEncoding(encoding);
        Document result = reader.read(source);
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding(encoding);
        }
        return result;
    }

    private static String getEncoding(String text) {
        String result = null;
        String xml = text.trim();
        if (xml.startsWith("<?xml")) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"\'");

            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                if ("encoding".equals(token)) {
                    if (tokens.hasMoreTokens()) {
                        result = tokens.nextToken();
                    }
                    break;
                }
            }
        }
        return result;
    }
}
