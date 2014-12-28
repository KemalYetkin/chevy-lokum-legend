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

public class LevelChooserGUI extends JFrame {
	
	public LevelChooserGUI(int maxLevelNo, int totalLevelNo) {
		super();
		init();
		this.setVisible(true);
		
		this.getContentPane().add(createHeaderPanel());
		this.getContentPane().add(createLevelsPanel(maxLevelNo, totalLevelNo));
		this.getContentPane().add(createBackButtonsPanel());
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
		SLabel headerLabel = new SLabel("Choose a level to play",
				SLabel.MAIN_MENU_SUBTITLE);
		headerPanel.add(headerLabel);
		headerPanel.setBackground(SColor.backgroundColor);
		
		headerPanelContainer.add(Box.createVerticalStrut(20));
		headerPanelContainer.add(headerPanel);
		headerPanelContainer.add(Box.createVerticalStrut(10));
		
		return headerPanelContainer;
	}
	
	private JPanel createLevelsPanel(int maxLevelNo, int totalLevelNo) {
		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);
		
		JPanel buttonPanel = new JPanel();
		
		ArrayList<SButton> buttons = new ArrayList<SButton>();
		
		// ONLY CHANGE HERE! ADD SBUTTONS TO THE LIST
		for (int i = 1; i <= totalLevelNo; i++) {
			SButton button;
			if (i <= maxLevelNo) {
				button = new SButton(i+"", SButton.LEVEL_CHOOSE_BUTTON_ON);
				button.addActionListener(new LevelPickListener(i));
			} else {
				button = new SButton(i+"", SButton.LEVEL_CHOOSE_BUTTON_OFF);
			}
			buttons.add(button);
		}
		// END OF EDIT ZONE
		
		buttonPanel.setLayout(new GridLayout(((totalLevelNo-1) / 5) + 1, 5, 10,10));
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
				GUIEngine.getInstance().returnStartGUIFromLevelChooser();
			}
		});
		footerPanel.add(backButton);
		footerPanelContainer.add(footerPanel);
		
		return footerPanelContainer;
	}
	
	class LevelPickListener implements ActionListener {
		
		private int levelNo;
		public LevelPickListener(int levelNo) {
			this.levelNo = levelNo;
		}
		
		public void actionPerformed(ActionEvent e) {
			GUIEngine.getInstance().newGame(levelNo);
		}
		
	}
}
