package proj2ser;

import java.net.Socket;
//import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.transform.TransformerException;

import java.io.*;



public class ServThread implements Runnable {
	private int id;
	private Socket cl;
	private OutputStreamWriter outStream;
	private InputStreamReader inStream;
	private Scanner scaner;
	public PrintWriter out;
	
	
	public ServThread(Socket cl, int id) {
		
		this.cl=cl;
		this.id=id;
		
			try {
				outStream = new OutputStreamWriter(this.cl.getOutputStream(),"UTF-8");
				inStream = new InputStreamReader(this.cl.getInputStream(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
			} catch (IOException e) {}
		out = new PrintWriter (outStream, true);
		scaner = new Scanner (inStream);		
	}

	@Override
	public void run() {
		while (scaner.hasNextLine()){
			String messege = scaner.nextLine();
			String title = MessageModel.parthSmth(messege,"title");
			if (title.equals("ping")){
						ServerM.clList.add(id);
				//ServerM.addNewContact(id);
			}
			//System.out.println("From client "+id+" = "+messege);
			else if (title.equals("message")){
				String to =  MessageModel.parthSmth(messege,"to");
				if (to.equals("All")) ServerM.sendToGroup(messege,id);
				else ServerM.sendTo(messege,to);
			}
			else if ("login".equals(title)){
				
				String [] a= MessageModel.parthSmth(messege,"message").split(" ");
				for (int p=0;p<ServerM.RegistrUsers.size();p++){
					if (a[0].equals(ServerM.RegistrUsers.get(p).getLogin()) && a[1].equals(ServerM.RegistrUsers.get(p).getPassword()))
						avtorisation(a[0]);		
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				avtorisation(a[0]);
			
			//else ServerM.sendToGroup(messege,id);
		}
		}
	}
	public void Send(String messege) {
		out.println(messege);	
	}
	
	public void avtorisation (String login){
		if (!ServerM.ClLogs.containsKey(login)){
			ServerM.sendOK(id);
			ServerM.ClLogs.put(id,login);
			ServerM.sendAllContacts(id);
			ServerM.addNewContact(id);
			ServerM.sendID(id);
		}
	}
}

