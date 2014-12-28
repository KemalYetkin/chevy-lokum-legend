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
import javax.swing.JTextField;

import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;
import cas.AudioPlayer;
import cas.Player;
import engines.GUIEngine;
import engines.GameEngine;

public class SaveGameGUI extends JFrame{
	private JTextField textField;

	public SaveGameGUI() {
		super();
		init();
		this.getContentPane().add(Box.createVerticalStrut(90));
		this.getContentPane().add(createLabelPanel());
		this.getContentPane().add(Box.createVerticalStrut(120));
		this.getContentPane().add(createSaveButtonsPanel());
		this.getContentPane().add(Box.createVerticalStrut(10));
		this.getContentPane().add(createCancelButtonsPanel());
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

	private JPanel createLabelPanel(){
		JPanel textPanelContainer = new JPanel();

		JPanel headerPanel = new JPanel();
		SLabel name = new SLabel("Enter your SAVE GAME name: ", SLabel.MAIN_MENU_AUTHOR);
		textField = new JTextField();


		headerPanel.setLayout(new GridLayout(0,1,0 ,15));

		headerPanel.add(name);

		headerPanel.add(textField);
		headerPanel.setBackground(SColor.backgroundColor);
		textPanelContainer.add(headerPanel);
		textPanelContainer.setBackground(SColor.backgroundColor);


		return textPanelContainer;
	}
	private JPanel createSaveButtonsPanel() {

		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);
		JPanel buttonPanel = new JPanel();
		ArrayList<SButton> buttons = new ArrayList<SButton>();
		// ONLY CHANGE HERE! ADD SBUTTONS TO THE LIST
		SButton submitButton = new SButton("SAVE!", SButton.SUBMIT_BUTTON);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				GUIEngine.getInstance().saveCurrentGame(textField.getText());
				dispose();
			}
		});
		buttons.add(submitButton);

		buttonPanel.setLayout(new GridLayout(buttons.size(),1,0,10));
		buttonPanel.setBackground(SColor.backgroundColor);
		for (SButton b : buttons) {
			buttonPanel.add(b);
		}

		buttonPanelContainer.add(buttonPanel);

		return buttonPanelContainer;
	}

	private JPanel createCancelButtonsPanel() {

		JPanel footerPanelContainer = new JPanel();
		footerPanelContainer.setBackground(SColor.backgroundColor);

		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(SColor.backgroundColor);

		SButton backButton = new SButton("CANCEL SAVING", SButton.SUB_MENU_BUTTON);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		footerPanel.add(backButton);
		footerPanelContainer.add(footerPanel);

		return footerPanelContainer;
	}
}
