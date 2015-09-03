package proj2ser;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.Timer;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.*;
//import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ServerM {
	public static ArrayList <ServThread> clientList = new ArrayList <ServThread>();
	public static ArrayList <Integer> clList = new ArrayList <Integer>();
	public static Map ClLogs = new HashMap<Integer, String >();
	public static ArrayList <User> RegistrUsers = new ArrayList <User>();
	public static ArrayList<LinkedList<String>> mesStore = new ArrayList<LinkedList<String>>();
	private static final int COUNT_SAVE_MESSAGES = 10;
	
	public static void main(String[] args) {
		ServerSocket ss = null;
		
		RegistraciaStore.parthStore();
		
	
		Timer timer= new Timer( 10000 , new ActionListener(){
				public void actionPerformed(ActionEvent ev) {
					pingClients();
				}}
				);
			timer.start();
			
		try {
			ss = new ServerSocket(3460);
			Socket client;
			System.out.println("Waiting...");
			while (true){
				client = ss.accept();
				
				System.out.println("Connection... "+(clientList.size()));
				ServThread clients = new ServThread (client, clientList.size());
				clientList.add(clients);
				clList.add(clientList.size()-1);
				Thread t = new Thread (clients);
				t.start();
				System.out.println("toString "+client.toString());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static ServThread getClient(int index) {
		return ((ServThread) clientList.get(index));
	}

	public static void sendToAll(String messege) {
		for (int i=0;i<clientList.size();i++)
			getClient(i).Send(messege);
	}

	public static void sendTo(String mes,String name){
		for (int i=0;i<clientList.size();i++)
			if (name.equals(ClLogs.get(i)))
				getClient(i).Send(mes);
	}

	public static void sendToGroup(String mes,int id){
		for (int i=0;i<clientList.size();i++)
			if (id!=i)
				getClient(i).Send(mes);
	}
	
	
	public static void sendAllContacts(){
		String str="";
		
		for (int j=0;j<clList.size();j++)
			if (ClLogs.get(clList.get(j))!=null)
				str+=ClLogs.get(clList.get(j))+"/abzc/";
		
		if (!"".equals(str))
			sendToAll(MessageModel.createMes("List of contacts",str));
			
		
	}
	public static void sendAllContacts(int id){
		String str="";
			
		if (clList.size()!=0){
			for (int j=0;j<clList.size();j++)
				if (ClLogs.get(clList.get(j))!=null)
					str+=ClLogs.get(clList.get(j))+"/abzc/";
		if (!"".equals(str))	
			getClient(id).Send(MessageModel.createMes("List of contacts",str));
		}	
		else 
			getClient(id).Send(MessageModel.createMes("List of contacts", "Wait for contacts"));
	}
	
	public static void addNewContact(int id){
		sendToGroup(MessageModel.createMes("New client",ClLogs.get(id).toString()),id);	
	}
	
	public static void pingClients(){
		sendAllContacts();
		clList.clear();
		sendToAll(MessageModel.createMes("ping", ""));
	}
	
	public static void sendID(int id){
		getClient(id).Send(MessageModel.createMes("your id", Integer.toString(id)));
	}
	
	public static void sendOK(int id){
		getClient(id).Send(MessageModel.createMes("OK", Integer.toString(id)));
	}

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
}