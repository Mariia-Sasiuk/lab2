package main.java.server.controller;

import main.java.server.model.MessageModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by masa0715 on 9/4/2015.
 */
public class HistoryStore {
    private static Document doc;
    public static ArrayList<LinkedList<String>> mesStore = new ArrayList<LinkedList<String>>();
    private static final int COUNT_SAVE_MESSAGES = 5;
    final static Logger logger = LogManager.getLogger(HistoryStore.class);

    public static void saveMessage(String user1, String user2, String mes) {
        for (LinkedList<String> dialog : mesStore) {
            if (user1.equals(dialog.get(0)) && user2.equals(dialog.get(1))
                    || user1.equals(dialog.get(1)) && user2.equals(dialog.get(0))) {
                dialog.add(mes);
                if (dialog.size() > COUNT_SAVE_MESSAGES + 2)
                    dialog.remove(2);
                return;
            }
        }

        LinkedList<String> dialog = new LinkedList<>();
        dialog.add(user1);
        dialog.add(user2);
        dialog.add(mes);
        mesStore.add(dialog);
    }

    public static void saveMessageInXML(Element root, Document document) {
        for (LinkedList<String> dialog : mesStore) {
            for (int i = 2; i < dialog.size(); i++) {
                Node userMes = document.createElement("message");
                userMes.setTextContent(dialog.get(i));
                root.appendChild(userMes);
                if (i == dialog.size() - 1) {
                    try {
                        saveXMLFile(dialog.get(0) + dialog.get(1), document);
                    } catch (TransformerException e) {
                        logger.error(e);
                    }
                }
            }
        }
    }

    public static void createXMLFile() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        saveMessageInXML(root, document);
    }

    public static void saveXMLFile(String name, Document doc) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(name + ".xml"));
        transformer.transform(source, result);
    }

    public static void parthHistory(String user1, String user2, String login) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new File(user1 + user2 + ".xml"));

            NodeList nodeList = doc.getElementsByTagName("message");

            StringBuilder histiryMes = new StringBuilder();

            for (int i = 0; i < nodeList.getLength(); i++) {
                saveMessage(user1, user2, nodeList.item(i).getTextContent());
                histiryMes.append(nodeList.item(i).getTextContent() + "/abzc/");
            }
            if (login.equals(user1))
                ServerM.sendTo(MessageModel.createMes("history", histiryMes.toString(), user2, user1), user1);
            else
                ServerM.sendTo(MessageModel.createMes("history", histiryMes.toString(), user1, user2), user2);


        } catch (Exception e) {
            logger.error(e);
        }
    }
}
