package frames;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;
import engines.GUIEngine;

@SuppressWarnings("serial")
public class LoadGameGUI extends JFrame {
	@SuppressWarnings("rawtypes")
	public LoadGameGUI(HashMap<String, ArrayList> list) {
		super();
		init();
		this.setVisible(true);
		this.getContentPane().add(createHeaderPanel());
		this.getContentPane().add(createSavesPanel(list));		
		this.getContentPane().add(createBackButtonsPanel());
		this.pack();
	}

	public LoadGameGUI(){
		super();
		init();
		this.setVisible(true);
		this.getContentPane().add(createHeaderPanel());
		this.getContentPane().add(createBackButtonsPanel());
		this.pack();
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
		SLabel headerLabel = new SLabel("Choose a game to play",
				SLabel.MAIN_MENU_SUBTITLE);
		headerPanel.add(headerLabel);
		headerPanel.setBackground(SColor.backgroundColor);

		headerPanelContainer.add(Box.createVerticalStrut(20));
		headerPanelContainer.add(headerPanel);
		headerPanelContainer.add(Box.createVerticalStrut(10));

		return headerPanelContainer;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private JPanel createSavesPanel(HashMap<String, ArrayList> list) {
		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);

		JPanel buttonPanel = new JPanel();

		ArrayList<SButton> buttons = new ArrayList<SButton>();

		// ONLY CHANGE HERE! ADD SBUTTONS TO THE LIST
		for (String key : list.keySet()) {
			SButton button;
			String gamePath = key.replace(".xml", "");
			String levelNo = "Level: " + (int) list.get(key).get(0);
			String date = ((Date) list.get(key).get(1)).toGMTString();
			SLabel gameLabel = new SLabel(gamePath,SLabel.GAME_NAME);
			SLabel levelLabel = new SLabel(levelNo,SLabel.GAME_OTHERS);
			SLabel dateLabel = new SLabel(date, SLabel.GAME_OTHERS);				
			button = new SButton(SButton.GAME_LOAD_BUTTON);
			button.setLayout(new BorderLayout());
			button.add(BorderLayout.NORTH,gameLabel);
			button.add(BorderLayout.CENTER,levelLabel);
			button.add(BorderLayout.AFTER_LAST_LINE,dateLabel);				
			button.addActionListener(new GamePickListener(gamePath));		
			buttons.add(button);
		}
		// END OF EDIT ZONE

		buttonPanel.setLayout(new GridLayout(list.size() + 1, 5, 20,20));
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
				GUIEngine.getInstance().returnStartGUIFromLoadGUI();
			}
		});
		footerPanel.add(backButton);
		footerPanelContainer.add(footerPanel);

		return footerPanelContainer;
	}

	class GamePickListener implements ActionListener {

		private String gamePath;
		public GamePickListener(String gamePath) {
			this.gamePath = gamePath;
		}

		public void actionPerformed(ActionEvent e) {
			GUIEngine.getInstance().loadGame(gamePath);
		}

	}
}
