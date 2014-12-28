package frames;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;
import engines.GUIEngine;

public class MenuGUI extends JFrame {

	public MenuGUI() {
		super();
		init();

		this.getContentPane().add(createHeaderPanel());
		this.getContentPane().add(createMenuButtonsPanel());
		//this.getContentPane().add(createFooterPanel());
		
		this.setVisible(true);
	}
	
	private void init() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(400,500);
		this.getContentPane().setBackground(SColor.backgroundColor);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}	
	
	private JPanel createHeaderPanel() {
		JPanel headerPanelContainer = new JPanel();
		headerPanelContainer.setLayout(new BoxLayout(headerPanelContainer,
				BoxLayout.Y_AXIS));	
		headerPanelContainer.setBackground(SColor.backgroundColor);
		
		// PANEL
		JPanel headerPanel = new JPanel();
		SLabel openit = new SLabel("openIT proudly presents", SLabel.MAIN_MENU_AUTHOR);
		SLabel title1 = new SLabel("CHEVY LOKUM", SLabel.MAIN_MENU_TITLE);
		SLabel title2 = new SLabel("LEGEND", SLabel.MAIN_MENU_TITLE);
		SLabel subtitle = new SLabel("another COMP 302 Project",
				SLabel.MAIN_MENU_SUBTITLE);
		headerPanel.add(openit);
		headerPanel.add(title1);
		headerPanel.add(title2);
		headerPanel.add(subtitle);

		headerPanel.setBackground(SColor.backgroundColor);
		
		headerPanelContainer.add(Box.createVerticalStrut(30));
		headerPanelContainer.add(headerPanel);
		headerPanelContainer.add(Box.createVerticalStrut(10));
		
		return headerPanelContainer;
	}
	
	private JPanel createMenuButtonsPanel() {
		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);
		
		JPanel buttonPanel = new JPanel();
		
		ArrayList<SButton> buttons = new ArrayList<SButton>();
		
		// ONLY CHANGE HERE! ADD SBUTTONS TO THE LIST
		SButton playButton = new SButton("PLAY", SButton.MAIN_MENU_BUTTON);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIEngine.getInstance().askPlayer();
			}
		});
		buttons.add(playButton);
		
		SButton quitButton = new SButton("QUIT", SButton.MAIN_MENU_BUTTON);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIEngine.getInstance().exitGame();			
			}
		});
		buttons.add(quitButton);
		// END OF EDIT ZONE
		
		buttonPanel.setLayout(new GridLayout(buttons.size(),1,0,10));
		buttonPanel.setBackground(SColor.backgroundColor);
		for (SButton b : buttons) {
			buttonPanel.add(b);
		}
		
		buttonPanelContainer.add(buttonPanel);
		return buttonPanelContainer;
	}

}
