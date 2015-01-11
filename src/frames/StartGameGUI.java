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
import engines.GUIEngine;

@SuppressWarnings("serial")
public class StartGameGUI extends JFrame {

	public StartGameGUI() {
		super();
		init();
		this.getContentPane().add(Box.createVerticalStrut(90));
		this.getContentPane().add(createStartButtonsPanel());
		this.getContentPane().add(Box.createVerticalStrut(10));
		this.getContentPane().add(createBackButtonsPanel());
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

	private JPanel createStartButtonsPanel() {

		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);
		JPanel buttonPanel = new JPanel();
		ArrayList<SButton> buttons = new ArrayList<SButton>();

		// ONLY CHANGE HERE! ADD SBUTTONS TO THE LIST
		SButton newGameButton = new SButton("NEW GAME", SButton.START_MENU_BUTTON);
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIEngine.getInstance().chooseLevel();
			}
		});
		buttons.add(newGameButton);

		SButton loadGameButton = new SButton("LOAD GAME", SButton.START_MENU_BUTTON);
		loadGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIEngine.getInstance().callLoadGame();
			}
		});
		buttons.add(loadGameButton);
		// END OF EDIT ZONE

		buttonPanel.setLayout(new GridLayout(buttons.size(),1,0,10));
		buttonPanel.setBackground(SColor.backgroundColor);
		for (SButton b : buttons) {
			buttonPanel.add(b);
		}

		buttonPanelContainer.add(buttonPanel);

		return buttonPanelContainer;
	}

	private JPanel createBackButtonsPanel() {

		JPanel footerPanelContainer = new JPanel();
		footerPanelContainer.setBackground(SColor.backgroundColor);

		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(SColor.backgroundColor);

		SButton backButton = new SButton("BACK TO MENU", SButton.SUB_MENU_BUTTON);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIEngine.getInstance().returnMenu();
			}
		});
		footerPanel.add(backButton);
		footerPanelContainer.add(footerPanel);
		return footerPanelContainer;
	}
}
