package main.java.server.controller;


import main.java.server.model.MessageModel;
import main.java.server.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerM {
	public static ArrayList <ServThread> clientList = new ArrayList <ServThread>();
	public static ArrayList <Integer> clList = new ArrayList <Integer>();
	public static ArrayList <Integer> oldclList = new ArrayList <Integer>();
	public static Map ClLogs = new HashMap<Integer, String >();
	public static ArrayList <User> RegistrUsers = new ArrayList <User>();

	public final static Logger logger = LogManager.getLogger(ServerM.class);
	
	public static void main(String[] args) {
		logger.info("Server started working");
		ServerSocket ss = null;

		RegistraciaStore.parthStore();
		
	
		Timer timer= new Timer( 5000 , new ActionListener(){
				public void actionPerformed(ActionEvent ev) {
					pingClients();
				}}
				);
			timer.start();
			
		try {
			ss = new ServerSocket(3460);
			Socket client;
			logger.info(" Server is waiting for connection");
			while (true){
				try {
					client = ss.accept();
				} catch (IOException e) {
					logger.error(e);
					continue;
				}
				logger.info("Connection " + (clientList.size()));
				ServThread clients = new ServThread (client, clientList.size());
				clientList.add(clients);
				//clList.add(clientList.size() - 1);
				Thread t = new Thread (clients);
				t.start();
			}
		} catch (IOException e1) {
			logger.error(e1);
		}
		finally{
			try {
				ss.close();
			} catch (IOException e) {
				logger.error(e);
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
			sendToAll(MessageModel.createMes("List of contacts", str));
			
		
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
		sendToGroup(MessageModel.createMes("New client", ClLogs.get(id).toString()), id);
	}
	
	public static void pingClients(){

		sendAllContacts();
		for (int i = 0; i < clientList.size() ; i++)
			if (oldclList.contains(clientList.get(i).getId())) {
				if (!clientList.get(i).getAlive()) {
					try {
						HistoryStore.createXMLFile();
					} catch (ParserConfigurationException e) {
						logger.error(e);
					}
				}
			}
		oldclList.clear();

		for (int i = 0; i < clientList.size() ; i++) {
			if (clList.contains(clientList.get(i).getId())){
				clientList.get(i).setAlive(false);
				oldclList.add(clientList.get(i).getId());
			}
		}
		clList.clear();
		sendToAll(MessageModel.createMes("ping", ""));
	}
	
	public static void sendID(int id){
		getClient(id).Send(MessageModel.createMes("your id", Integer.toString(id)));
	}
	
	public static void sendOK(int id){
		getClient(id).Send(MessageModel.createMes("OK", Integer.toString(id)));
	}


}