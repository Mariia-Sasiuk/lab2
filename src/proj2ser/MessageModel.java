package proj2ser;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MessageModel {
	
	public static String createMes(String title,String message){
		String st ="<sendmes>" +
				"<title>"+title+"</title>" +
				"<message>"+message+"</message>"+
				"</sendmes>";
		return st;
	}
	
	public static String parthSmth(String mes,String what){
		String mesTitle = null;
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
		        mesTitle = ((Node) fstNm.item(0)).getNodeValue();
		       }
		  } catch (Exception e) {
		      e.printStackTrace();
		  }  
		return mesTitle;
	}
}
