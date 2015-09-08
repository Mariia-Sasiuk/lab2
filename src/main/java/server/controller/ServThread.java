package main.java.server.controller;

import main.java.server.model.MessageModel;
import main.java.server.model.User;

import java.net.Socket;
//import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.transform.TransformerException;

import java.io.*;



public class ServThread implements Runnable {
	private int id;
	private boolean alive = true;
	private Socket cl;
	private OutputStreamWriter outStream;
	private InputStreamReader inStream;
	private Scanner scaner;
	public PrintWriter out;
	public String login;

	public boolean getAlive() {return alive;}
	public void setAlive(boolean alive) {this.alive = alive;}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public ServThread(Socket cl, int id) {
		
		this.cl=cl;
		this.id=id;
		
			try {
				outStream = new OutputStreamWriter(this.cl.getOutputStream(),"UTF-8");
				inStream = new InputStreamReader(this.cl.getInputStream(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				ServerM.logger.error(e);
			} catch (IOException e) {
				ServerM.logger.error(e);
			}
		out = new PrintWriter (outStream, true);
		scaner = new Scanner (inStream);		
	}

	@Override
	public void run() {
		while (scaner.hasNextLine()){
			String messege = scaner.nextLine();
			String title = MessageModel.parthSmth(messege, "title");
			if (title.equals("ping")){
				alive = true;
				ServerM.clList.add(id);
			}

			else if (title.equals("message")){
				String to =  MessageModel.parthSmth(messege,"to");
				String from = MessageModel.parthSmth(messege,"from");

				if (to.equals("All")) ServerM.sendToGroup(messege,id);
				else {
					HistoryStore.saveMessage(to,from,from+": "+ MessageModel.parthSmth(messege,"message")); //save history of messages
					ServerM.sendTo(messege,to);
				}
			}
			else if ("login".equals(title)){
				
				String [] logPas= MessageModel.parthSmth(messege,"message").split(" ");
				for (int p=0;p<ServerM.RegistrUsers.size();p++){
					if (logPas[0].equals(ServerM.RegistrUsers.get(p).getLogin()) && logPas[1].equals(ServerM.RegistrUsers.get(p).getPassword()))
						avtorisation(logPas[0]);
				}				
			}
			else if ("registracia".equals(title)){
				String [] a=MessageModel.parthSmth(messege,"message").split(" ");
				for (int p=0;p<ServerM.RegistrUsers.size();p++){
					if (a[0].equals(ServerM.RegistrUsers.get(p).getLogin()))
						return;
				}
				ServerM.RegistrUsers.add(new User(a[0],a[1]));
				try {
					RegistraciaStore.addUser(a[0],a[1]);
				} catch (TransformerException e) {
					ServerM.logger.error(e);
				}
				avtorisation(a[0]);

			}
			else if ("history".equals(title)){
				String [] fileName= MessageModel.parthSmth(messege,"message").split(" ");
				if ( new File(fileName[0]+fileName[1]+".xml").exists())
					HistoryStore.parthHistory(fileName[0], fileName[1],login);

				else if( new File(fileName[1]+fileName[0]+".xml").exists())
					HistoryStore.parthHistory(fileName[1], fileName[0],login);
				else
					ServerM.sendTo(MessageModel.createMes("nohistory",fileName[1]),login);
			}

		}
	}
	public void Send(String messege) {
		out.println(messege);
	}
	
	public void avtorisation (String login){
		if (!ServerM.ClLogs.containsKey(login)){
			this.login=login;
			ServerM.clList.add(id);
			ServerM.ClLogs.put(id,login);
			ServerM.sendOK(id);
			ServerM.sendAllContacts(id);
			ServerM.addNewContact(id);
			ServerM.sendID(id);
		}
	}
}

