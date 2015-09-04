package proj2ser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    private static final int COUNT_SAVE_MESSAGES = 10;

    public static void saveMessage(String user1, String user2, String mes){
        for (LinkedList<String> dialog : mesStore){
            if (user1.equals(dialog.get(0)) && user2.equals(dialog.get(1))
                    || user1.equals(dialog.get(1)) && user2.equals(dialog.get(0)))
            {
                dialog.add(mes);
                if (dialog.size()>COUNT_SAVE_MESSAGES+2)
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
    public static void saveMessageInXML(){

    }
    public static void createXMLFile() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);


    }
    public static void saveXMLFile(String name, Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(name+".xml"));
        transformer.transform(source, result);
    }

    public static void parthHistory(String fileName){
        try{
            DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new File (fileName+".xml"));

            NodeList nodeList = doc.getElementsByTagName("root");

            for (int i=0; i<nodeList.getLength();i++){
               // node=nodeList.item(i).getTextContent(); // addition messages from store to list; instead of node should be LIST

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
