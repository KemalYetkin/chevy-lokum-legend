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

import cas.AudioPlayers;

import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;
import engines.GUIEngine;

@SuppressWarnings("serial")
public class GameOverGUI extends JFrame {
	private String situation;

	public GameOverGUI(String situation) {
		super();
		init();
		this.situation = situation;
		this.getContentPane().add(createHeaderPanel());
		this.getContentPane().add(createButtonPanel());
		this.setVisible(true);
	}

	private void init() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(360,300);
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

		String currentSituation = null;
		AudioPlayers.getInstance().disableOrEnableBackgroundSounds(false);
		if (situation.equals("win")){
			currentSituation = "Game is over! You win!";
			AudioPlayers.getInstance().disableOrEnableGameOverWinSound(GUIEngine.getInstance().getMusicStatus());
		}
		else if (situation.equals("lose")){
			currentSituation = "Game is over! You lose!";
			AudioPlayers.getInstance().disableOrEnableGameOverLooseSound(GUIEngine.getInstance().getMusicStatus());
		}
		SLabel situiaton = new SLabel(currentSituation, SLabel.PLAY_SIGN);
		headerPanel.add(situiaton);

		headerPanel.setBackground(SColor.backgroundColor);

		headerPanelContainer.add(Box.createVerticalStrut(30));
		headerPanelContainer.add(headerPanel);
		headerPanelContainer.add(Box.createVerticalStrut(10));

		return headerPanelContainer;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);

		JPanel buttonPanel = new JPanel();

		ArrayList<SButton> buttons = new ArrayList<SButton>();

		// ONLY CHANGE HERE! ADD SBUTTONS TO THE LIST
		if (situation.equals("win")){
			SButton nextLevel = new SButton("NEXT LEVEL", SButton.GAME_OVER_BUTTON);
			nextLevel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AudioPlayers.getInstance().disableOrEnableGameOverWinSound(false);
					GUIEngine.getInstance().loadNextLevel();	
				}
			});
			buttons.add(nextLevel);
		}

		SButton mainMenu = new SButton("MAIN MENU", SButton.GAME_OVER_BUTTON);
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AudioPlayers.getInstance().disableOrEnableGameOverWinSound(false);;
				AudioPlayers.getInstance().disableOrEnableGameOverLooseSound(false);
				GUIEngine.getInstance().returnMainMenuFromGameOverScreen();	

			}
		});
		buttons.add(mainMenu);

		if (situation.equals("lose")){
			SButton exit = new SButton("EXIT", SButton.GAME_OVER_BUTTON);
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AudioPlayers.getInstance().disableOrEnableGameOverLooseSound(false);
					GUIEngine.getInstance().exitGame();
				}
			});
			buttons.add(exit);
		}
		// END OF EDIT ZONE

		buttonPanel.setLayout(new GridLayout(buttons.size(),1,0,25));
		buttonPanel.setBackground(SColor.backgroundColor);
		for (SButton b : buttons) {
			buttonPanel.add(b);
		}

		buttonPanelContainer.add(buttonPanel);
		return buttonPanelContainer;
	}
}
