package proj2cl;


import proj2ser.HistoryStore;

import java.awt.event.*;
import javax.swing.*;

public class Popup extends JPanel {
	public JPopupMenu popup = new JPopupMenu();
	JPanel panel;
	JScrollPane scr;
	MessengerWindow mesWind;
	
	public Popup(JPanel p,JScrollPane scr2, MessengerWindow mw2,String tblName){
		JMenuItem menu = new JMenuItem("Close tab");
		popup.add(menu);
		this.panel=p;
		this.scr=scr2;
		this.mesWind=mw2;
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mesWind.getTabs().remove(scr);
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
				popup.show(panel, e.getX(), e.getY());
			}
		}
	}
	
}
