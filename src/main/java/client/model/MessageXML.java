package main.java.client.model;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import main.java.client.controller.ClientM;
import main.java.server.model.MessageModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import static main.java.client.controller.ClientM.logger;


public class MessageXML {
	final static Logger logger = LogManager.getLogger(MessageModel.class);

	public static String packMes(String mes,String to,String from){
		String newMes = mes.replaceAll("\n", "/abzc/");
		String st ="<sendmes>" +
				 "<title>message</title>" +
				"<to>"+to+"</to>"+
				 "<from>"+from+"</from>"+
				 "<message>"+newMes+"</message>"+
				 "</sendmes>";
		return st;
	}

	public static String sendInfo(String name,String passw, String title){
		String st ="<sendmes>" +
				 "<title>"+title+"</title>" +
				 "<message>"+name+" "+passw+"</message>"+
				 "</sendmes>";
		return st;
	}

	public static String sendRequestForMesHistory(String user1Name,String user2Name ){
		String st ="<sendmes>" +
				"<title>history</title>" +
				"<message>"+user1Name+" "+user2Name+"</message>"+
				"</sendmes>";
		return st;
	}
	
	public static String parthSmth(String mes,String what){
		String mesSmth = null;
		try{ 
			DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(mes));

			Document doc = db.parse(is);
		   
			NodeList nodeLst = doc.getElementsByTagName("sendmes");
		  
		       Node fstNode = nodeLst.item(0);

		       if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		        Element fstElmnt = (Element) fstNode;
		        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(what);
		        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		        NodeList fstNm = fstNmElmnt.getChildNodes();
		        mesSmth = ((Node) fstNm.item(0)).getNodeValue();
		       }
		  } catch (Exception e) {
			logger.error(e);
		  }  
		return mesSmth;
	}
}
