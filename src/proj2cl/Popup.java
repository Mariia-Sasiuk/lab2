package proj2cl;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
//import javax.swing.JPopupMenu;

public class Popup extends JPanel {
	public JPopupMenu popup = new JPopupMenu();
	JPanel p;
	JScrollPane scr;
	MessengerWindow mw;
	
	public Popup(JPanel p,JScrollPane scr, MessengerWindow mw){
		JMenuItem menu = new JMenuItem("Close tab");
		popup.add(menu);
		this.p=p;
		this.scr=scr;
		this.mw=mw;
		ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	mw.getTabs().remove(scr);
            }
        };
        menu.addActionListener(menuListener);
	}	
	
	class MousePopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) { checkPopup(e); }
		public void mouseClicked(MouseEvent e) { checkPopup(e); }
		public void mouseReleased(MouseEvent e) { 
			if (e.getButton()==MouseEvent.BUTTON3)
					checkPopup(e); 
		}

		private void checkPopup(MouseEvent e) {			
			if (e.isPopupTrigger()) {System.out.println("ku");
				popup.show(p, e.getX(), e.getY());
			}
		}
	}
	
}
