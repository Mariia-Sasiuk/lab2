package main.java.server;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RegistraciaStore {
private static Document doc;
public static void addUser(String login, String password) throws TransformerException{
	Node store = doc.getElementsByTagName("store").item(0);
	Node UserModel = doc.createElement("user");
	Node UserLogin = doc.createElement("login");
	Node UserPassword = doc.createElement("password");
	
	UserLogin.setTextContent(login);
	UserPassword.setTextContent(password);
	
	UserModel.appendChild(UserLogin);
	UserModel.appendChild(UserPassword);
	
	store.appendChild(UserModel);
	
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(new File("RegistrStore.xml"));
	transformer.transform(source, result);
	
}

public static void parthStore(){
	
	try{ 
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(new File ("RegistrStore.xml"));
	   
		NodeList nodeList = doc.getElementsByTagName("user");
	  
		Node node;
		for (int i=0; i<nodeList.getLength();i++){
			node=nodeList.item(i);
			NodeList NL = node.getChildNodes();
			ServerM.RegistrUsers.add(new User(NL.item(0).getTextContent(),
					NL.item(1).getTextContent()));
			System.out.println("login = "+NL.item(0).getTextContent()+
					"  password = "+NL.item(1).getTextContent());
		}
	       
	       
	  } catch (Exception e) {
	      e.printStackTrace();
	  }  
}


}
