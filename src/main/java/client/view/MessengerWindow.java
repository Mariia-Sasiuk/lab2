package main.java.client.view;

import main.java.client.model.Client;
import main.java.client.controller.ClientM;
import main.java.client.model.MessageXML;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

//import proj1cl.ClientM.MyClick;

public class MessengerWindow extends JFrame{
	
	private JButton b1;
	private JTextArea t1;
	private JTextArea login;
	private JPanel p0 = new JPanel(new VerticalFlowLayout());
	private JPanel p1 = new JPanel(new BorderLayout());
	private JPanel pmes = new JPanel(new VerticalFlowLayout());
	private JPanel p;
	private final int w = 500;
	private final int h = 600;
	private JLabel l;
	private String name="";
	
	private ClientM controller;
	private Client cl;
	private JTabbedPane tabs = new JTabbedPane();
	

	public MessengerWindow(ClientM controller,Client cl){
		this.controller = controller;
		this.cl = cl;
	}
	
	public void CreateCleintWindow (String name){
		setTitle(name);
		setLayout(new BorderLayout());
		setSize(w,h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
				
		l=new JLabel("You can contacte to:");
		p = new JPanel(new FlowLayout());
		
		t1 = new JTextArea(3,24);
		b1 = new JButton("send");
		b1.addMouseListener(new MyClick());
		t1.setWrapStyleWord(true);
		t1.setLineWrap(true);
		
		add(p);
		
		JScrollPane scroll = new JScrollPane (t1);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		p.add(scroll);
		
		JScrollPane scr = new JScrollPane (pmes);
		scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		if (tabs.getTabCount()==0)
		tabs.addTab("Chat", scr);
		
		p.add(b1);
		p0.add(l);
		p1.add(p,BorderLayout.SOUTH);
		p1.add(tabs);
		
		add(p0,BorderLayout.WEST);
		add(p1);
		validate();
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
			if (e.getSource()==b1){
					if ("Chat".equals(tabs.getTitleAt(tabs.getSelectedIndex()))){
 					controller.out.println(MessageXML.packMes(t1.getText(), "All", cl.getMyname()));
 					JTextArea ta1=new JTextArea(1,25);
 					ta1.setText(cl.getMyname()+": "+t1.getText());
 					ta1.setEditable(false);
 					ta1.setWrapStyleWord(true);
 					ta1.setLineWrap(true);
 		            pmes.add(ta1);
 		            t1.setText("");
		            }
					else {
						name=tabs.getTitleAt(tabs.getSelectedIndex());
						controller.out.println(MessageXML.packMes(t1.getText(),name,cl.getMyname()));
 					JTextArea ta1=new JTextArea(1,25);
 					ta1.setText(cl.getMyname()+": "+t1.getText());
 					ta1.setEditable(false);
 					ta1.setWrapStyleWord(true);
 					ta1.setLineWrap(true);
 					controller.returnPan(tabs.getTitleAt(tabs.getSelectedIndex())).add(ta1);
 		            t1.setText("");
					}
		            validate();
				}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	public JButton getB1() {
		return b1;
	}

	public void setB1(JButton b1) {
		this.b1 = b1;
	}

	public JTextArea getT1() {
		return t1;
	}

	public void setT1(JTextArea t1) {
		this.t1 = t1;
	}

	public JTextArea getLogin() {
		return login;
	}

	public void setLogin(JTextArea login) {
		this.login = login;
	}

	public JPanel getP0() {
		return p0;
	}

	public void setP0(JPanel p0) {
		this.p0 = p0;
	}

	public JPanel getP1() {
		return p1;
	}

	public void setP1(JPanel p1) {
		this.p1 = p1;
	}

	public JPanel getPmes() {
		return pmes;
	}

	public void setPmes(JPanel pmes) {
		this.pmes = pmes;
	}

	public JPanel getP() {
		return p;
	}

	public void setP(JPanel p) {
		this.p = p;
	}

	public JLabel getL() {
		return l;
	}

	public void setL(JLabel l) {
		this.l = l;
	}

	public JTabbedPane getTabs() {
		return tabs;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
