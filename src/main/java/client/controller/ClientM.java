package main.java.client.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

import main.java.client.model.MessageXML;
import main.java.client.view.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ClientM {
    public Socket s;
    public InputStreamReader inStream;
    public OutputStreamWriter outStream;
    public Scanner scanner;
    public PrintWriter out;
    public String message;
    private boolean hishoryRequested = false;
    private String localMesStore = null;

    protected static ArrayList<String> clients = new ArrayList<String>();
    protected static ArrayList<JPanel> clpan = new ArrayList<JPanel>();
    protected static ArrayList<JLabel> ll = new ArrayList<JLabel>();

    protected String title;
    protected String mess;
    private StartWindow startWindow;


    public final static Logger logger = LogManager.getLogger(ClientM.class);

    public static void main(String[] args) {
        ClientM cl = new ClientM();
        cl.startWindow = new StartWindow(cl);
        cl.begin();
    }

    public void begin() {
        try {
            s = new Socket("localhost", 3460);

            try {
                inStream = new InputStreamReader(s.getInputStream(), "UTF8");
                outStream = new OutputStreamWriter(s.getOutputStream(), "UTF8");
            } catch (UnsupportedEncodingException e1) {
                logger.error(e1);
            } catch (IOException e1) {
                logger.error(e1);
            }

            scanner = new Scanner(inStream);
            out = new PrintWriter(outStream, true);

            while (scanner.hasNextLine()) {

                message = scanner.nextLine();
                title = MessageXML.parthSmth(message, "title");
                if ("OK".equals(title)) {
                    startWindow.loginfunc();
                } else if ("Fail".equals(title)) {

                    startWindow.showAvtError();
                }
                if (startWindow.getterMesWind() != null) {
                    if ("your id".equals(title)) {
                        mess = (MessageXML.parthSmth(message, "message"));
                        startWindow.getterClient().setId(Integer.parseInt(mess));
                    } else if (title.equals("List of contacts")) {
                        mess = (MessageXML.parthSmth(message, "message"));
                        String[] a = mess.split("/abzc/");
                        startWindow.getterMesWind().getP0().removeAll();
                        startWindow.getterMesWind().setL(new JLabel("You can contacte to:"));
                        startWindow.getterMesWind().getP0().add(startWindow.getterMesWind().getL());
                        Arrays.sort(a);
                        for (int j = 0; j < a.length; j++) {
                            JLabel label = new JLabel();
                            label.setText(a[j]);
                            label.addMouseListener(new MyClick());
                            ll.add(label);
                            startWindow.getterMesWind().getP0().add(label);
                        }
                        startWindow.getterMesWind().validate();
                        startWindow.getterMesWind().repaint();
                    } else if ("New client".equals(title)) {
                        JLabel label = new JLabel();
                        label.setText(MessageXML.parthSmth(message, "message"));
                        label.addMouseListener(new MyClick());
                        ll.add(label);
                        startWindow.getterMesWind().getP0().add(label);
                    } else if ("nohistory".equals(title)) {
                        addMesWithoutHistory(MessageXML.parthSmth(message, "message"));
                    } else if (title.equals("message") || title.equals("history")) {
                        String to = MessageXML.parthSmth(message, "to");
                        String from = MessageXML.parthSmth(message, "from");
                        mess = (MessageXML.parthSmth(message, "message")).replaceAll("/abzc/", "\n");
                        JTextArea ta1 = new JTextArea(1, 25);
                        if (title.equals("message"))
                            ta1.setText(from + ": " + mess);
                        else
                            ta1.setText(mess);
                        ta1.setEditable(false);
                        ta1.setWrapStyleWord(true);
                        ta1.setLineWrap(true);
                        if ("All".equals(to))
                            startWindow.getterMesWind().getPmes().add(ta1);
                        else {
                            createTab(from);
                            if (!hishoryRequested && "message".equals(title)) {
                                localMesStore = mess;
                            }
                            if ("history".equals(title) || ("message".equals(title) && hishoryRequested)) {
                                returnPan(from).add(ta1);
                                addMesWithoutHistory(from);
                            }
                            startWindow.getterMesWind().setName(from);
                        }
                    } else if (title.equals("ping")) {
                        out.println(MessageXML.sendInfo("", "", "ping"));
                    }
                    startWindow.getterMesWind().validate();
                }
            }
        } catch (UnknownHostException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }

    }


    public JPanel returnPan(String name) {
        for (int i = 0; i < clients.size(); i++) {
            if (name.equals(clients.get(i))) {
                return clpan.get(i);
            }
        }
        clients.add(name);
        JPanel findp = new JPanel(new VerticalFlowLayout());
        clpan.add(findp);
        return findp;
    }

    public void createTab(String fromName) {

        for (int i = 0; i < startWindow.getterMesWind().getTabs().getTabCount(); i++)
            if (startWindow.getterMesWind().getTabs().getTitleAt(i).equals(fromName))
                return;

        addTabPan(fromName, false);
    }

    public void addTabPan(String tabName, boolean activeTab) {
        JPanel pcont = returnPan(tabName);
        JScrollPane scr = new JScrollPane(pcont);

        pcont.addMouseListener(new main.java.client.view.Popup(pcont, scr, startWindow.getterMesWind(), tabName).new MousePopupListener());

        scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        startWindow.getterMesWind().getTabs().addTab(tabName, scr);

        out.println(MessageXML.sendRequestForMesHistory(startWindow.getterClient().getMyname(), tabName)); // request for history

        if (activeTab) {
            startWindow.getterMesWind().getTabs().setSelectedIndex(startWindow.getterMesWind().getTabs().getTabCount() - 1);
        }
    }

    public void addMesWithoutHistory(String from) {
        hishoryRequested = true;
        if (localMesStore != null) {
            JTextArea ta2 = new JTextArea(1, 25);
            ta2.setText(from + ": " + localMesStore);
            ta2.setEditable(false);
            ta2.setWrapStyleWord(true);
            ta2.setLineWrap(true);
            returnPan(from).add(ta2);
            localMesStore = null;

        }
    }

    public class MyClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            if (arg0.getButton() == MouseEvent.BUTTON1 && arg0.getSource() != startWindow.getterMesWind().getB1()) {
                for (int li = 0; li < ll.size(); li++)
                    if (ll.get(li) == (JLabel) arg0.getSource())
                        startWindow.getterMesWind().setName(ll.get(li).getText());


                for (int i = 0; i < startWindow.getterMesWind().getTabs().getTabCount(); i++)
                    if (startWindow.getterMesWind().getTabs().getTitleAt(i).equals(startWindow.getterMesWind().getName())) {
                        startWindow.getterMesWind().getTabs().setSelectedIndex(i);
                        return;
                    }
                addTabPan(startWindow.getterMesWind().getName(), true);
            }
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
        }


    }

}
