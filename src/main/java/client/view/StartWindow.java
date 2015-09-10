package main.java.client.view;

import main.java.client.model.Client;
import main.java.client.controller.ClientM;
import main.java.client.model.MessageXML;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;


public class StartWindow {
	private JTextArea login;
	private JFrame startWind = new JFrame("MESSENGER");
	private JButton log = new JButton("ENTER");
	private JButton reg = new JButton("REGISTR");
	private JPasswordField pass = new JPasswordField(10);
	private Client cl = new Client();
	private MessengerWindow mesWind;
	private JLabel avtorisationErrorLabel = new JLabel("Not unique login.");
	private JLabel emptyFieldsLabel = new JLabel("Specify all fields");
	private ClientM controller;
	
	public Client getterClient(){
		return cl;
	}
	public MessengerWindow getterMesWind(){
		return mesWind;
	}
	public StartWindow(ClientM controller){
		this.controller=controller;
		startWind.setLayout(new FlowLayout());
		startWind.setSize(500, 200);
		startWind.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startWind.setVisible(true);
		startWind.setLocationRelativeTo(null);
		startWind.setResizable(false);
		
		JLabel welc=new JLabel("Welcome to messenger!");
		login = new JTextArea(1,20);
		startWind.add(welc);
		startWind.add(login);
		startWind.add(pass);
		startWind.add(log);
		startWind.add(reg);

		log.addMouseListener(new MyClick());
		reg.addMouseListener(new MyClick());
		startWind.validate();
	}
	public void showAvtError(){
		if(startWind.isShowing()){
			startWind.add(avtorisationErrorLabel);
			startWind.validate();
		}
	}
	
	public void loginfunc (){
		startWind.dispose();
		mesWind = new MessengerWindow(controller,cl);
		mesWind.CreateCleintWindow(cl.getMyname());
	}
	
	public class MyClick implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			String name = login.getText();
			String password = new String(pass.getPassword());
			if (!"".equals(name) && !"".equals(password)){
				if (e.getSource()==log){
					cl.setMyname(name);
					controller.out.println(MessageXML.sendInfo(name, password, "login"));
				}
				else if(e.getSource()==reg){
					System.out.println("name = "+login.getText());
					cl.setMyname(name);
					controller.out.println(MessageXML.sendInfo(name, password,"registracia"));
					}
			}
			else if(startWind.isShowing()){
				startWind.add(emptyFieldsLabel);
				startWind.validate();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}

}
