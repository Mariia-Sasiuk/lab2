package proj2cl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;



//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;



public class ClientM{
	public Socket s;
	public InputStreamReader inStream;
	public OutputStreamWriter outStream;
	public Scanner scanner;
	public PrintWriter out;
	public String message;
	
	protected static ArrayList <String> clients = new ArrayList <String>();
	protected static ArrayList <JPanel> clpan = new ArrayList <JPanel>();
	protected static ArrayList <JLabel> ll = new ArrayList <JLabel>();
	
	protected String title;
	protected String mess;
	private StartWindow sw;

	
	//final Logger logger = LoggerFactory.getLogger(ClientM.class);

	public static void main(String[] args) throws UnknownHostException, IOException{
		ClientM cl = new ClientM();
		cl.sw = new StartWindow(cl);
		cl.run();
	}

	public void run() {
		try {
			s = new Socket("localhost",3460);
		} catch (UnknownHostException e) {
		} catch (IOException e) {}
		
		 try {
			inStream =new InputStreamReader(s.getInputStream(),"UTF8");
	        outStream=new OutputStreamWriter(s.getOutputStream(),"UTF8");
		 } catch (UnsupportedEncodingException e1) {
		 } catch (IOException e1) {}

         scanner=new Scanner(inStream);
         out=new PrintWriter(outStream,true);
         
 		 while(scanner.hasNextLine()){
 			 
             message=scanner.nextLine();
             title = MessageXML.parthSmth(message,"title");
             if ("OK".equals(title)){
            	 sw.loginfunc();
             }
             if (sw.getterMesWind()!=null){
				 if ("your id".equals(title)){
					 mess=(MessageXML.parthSmth(message,"message"));
					 sw.getterClient().setId(Integer.parseInt(mess));
				 }
				 else if (title.equals("List of contacts")){
					 mess=(MessageXML.parthSmth(message,"message"));
					 System.out.println(mess);
					 String [] a=mess.split("/abzc/");
					 sw.getterMesWind().getP0().removeAll();
					 sw.getterMesWind().setL(new JLabel("You can contacte to:"));
					 sw.getterMesWind().getP0().add(sw.getterMesWind().getL());
					 Arrays.sort(a);
					 for(int j=0;j<a.length;j++){
						 JLabel label = new JLabel();
						 label.setText(a[j]);
						 label.addMouseListener(new MyClick());
						 ll.add(label);
						 sw.getterMesWind().getP0().add(label);
					 }
					 sw.getterMesWind().validate();
					 sw.getterMesWind().repaint();
				 }
				 else if("New client".equals(title)){
					 JLabel label = new JLabel();
					 label.setText(MessageXML.parthSmth(message,"message"));
					 label.addMouseListener(new MyClick());
					 ll.add(label);
					 sw.getterMesWind().getP0().add(label);
				 }


				 else if (title.equals("message")){
					 String to = MessageXML.parthSmth(message,"to");
					 String from = MessageXML.parthSmth(message,"from");
					 mess=(MessageXML.parthSmth(message,"message")).replaceAll("/abzc/", "\n");
					 JTextArea ta1=new JTextArea(1,25);
					 ta1.setText(from+": "+mess);
					 ta1.setBackground(Color.LIGHT_GRAY);
					 ta1.setEditable(false);
					 ta1.setWrapStyleWord(true);
					 ta1.setLineWrap(true);
					 if ("All".equals(to))
						 sw.getterMesWind().getPmes().add(ta1);
					 else {
						 createTab(from, to);
						 returnPan(from).add(ta1);
						 sw.getterMesWind().setName(from);
					 }

				  }
				 else if (title.equals("ping")){
					 out.println(MessageXML.sendInfo("", "", "ping"));
				 }

				 sw.getterMesWind().validate();
             }
         }
 		
 		
		try {
			s.close();
		} catch (IOException e) {}		
	}
	
	
	
	public JPanel returnPan(String name){
		for (int i=0;i<clients.size();i++){
			if (name.equals(clients.get(i))){
				return clpan.get(i);
			}
		}
		clients.add(name);
		JPanel findp=new JPanel(new VerticalFlowLayout());
		clpan.add(findp);
		return findp;
	}
	
	public void createTab(String fromName, String toName){
		for (int i=0;i<sw.getterMesWind().getTabs().getTabCount();i++)
			if (sw.getterMesWind().getTabs().getTitleAt(i).equals(fromName))
				return;
			 
		addTabPan(fromName,false);
		out.println(MessageXML.sendRequestForMesHistory(fromName, toName));

	}
	
	public void addTabPan (String tabName, boolean activeTab){
		JPanel pcont = returnPan(tabName);
		JScrollPane scr = new JScrollPane (pcont);

		pcont.addMouseListener(new Popup(pcont,scr,sw.getterMesWind()).new MousePopupListener());
		
		scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sw.getterMesWind().getTabs().addTab(tabName, scr);
		if (activeTab)
			sw.getterMesWind().getTabs().setSelectedIndex(sw.getterMesWind().getTabs().getTabCount()-1);
			
	}
	
	public class MyClick implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getButton()==MouseEvent.BUTTON1 && arg0.getSource()!=sw.getterMesWind().getB1()){
				System.out.println("click1");
				for (int li=0;li<ll.size();li++)
					if( ll.get(li)==(JLabel)arg0.getSource())
						sw.getterMesWind().setName(ll.get(li).getText());

		
				for (int i=0;i<sw.getterMesWind().getTabs().getTabCount();i++)
					if (sw.getterMesWind().getTabs().getTitleAt(i).equals(sw.getterMesWind().getName())){
						sw.getterMesWind().getTabs().setSelectedIndex(i);
						return;
					}
				addTabPan(sw.getterMesWind().getName(), true);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
		

	}

}
