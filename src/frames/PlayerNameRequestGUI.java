package frames;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;
import cas.Player;
import engines.GUIEngine;

@SuppressWarnings("serial")
public class PlayerNameRequestGUI extends JFrame{
	private String playerName;
	private JTextField textField;

	public PlayerNameRequestGUI() {
		super();
		init();
		this.getContentPane().add(Box.createVerticalStrut(90));
		this.getContentPane().add(createLabelPanel());
		this.getContentPane().add(Box.createVerticalStrut(80));
		this.getContentPane().add(createStartButtonsPanel());
		this.getContentPane().add(Box.createVerticalStrut(10));
		this.getContentPane().add(createBackButtonsPanel());
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private void init() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(400,500);
		this.getContentPane().setBackground(SColor.backgroundColor);
		this.setLocationRelativeTo(null);
	}	

	private JPanel createLabelPanel(){
		JPanel textPanelContainer = new JPanel();

		JPanel headerPanel = new JPanel();
		SLabel name = new SLabel("Enter your name: ", SLabel.MAIN_MENU_AUTHOR);
		textField = new JTextField();

		headerPanel.setLayout(new GridLayout(0,1,0 ,15));
		headerPanel.add(name);
		headerPanel.add(textField);
		headerPanel.setBackground(SColor.backgroundColor);
		textPanelContainer.add(headerPanel);
		textPanelContainer.setBackground(SColor.backgroundColor);

		return textPanelContainer;
	}

	private JPanel createStartButtonsPanel() {
		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBackground(SColor.backgroundColor);

		JPanel buttonPanel = new JPanel();

		ArrayList<SButton> buttons = new ArrayList<SButton>();

		
		
		
		SButton submitButton = new SButton("SUBMIT", SButton.SUBMIT_BUTTON);
		
		AbstractAction buttonPressed = new AbstractAction() {
			@Override
            public void actionPerformed(ActionEvent e) {
				playerName = textField.getText();				
				if(!playerName.equals("")){
					Player player = new Player(playerName);				
					GUIEngine.getInstance().startGame(player);
				}
		}};	
		
		submitButton.addActionListener(buttonPressed);
		submitButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER,0), "Enter_pressed");
		submitButton.getActionMap().put("Enter_pressed", buttonPressed);
		buttons.add(submitButton);

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
				GUIEngine.getInstance().returnMenuFromPlayerRequester();
			}
		});
		footerPanel.add(backButton);
		footerPanelContainer.add(footerPanel);

		return footerPanelContainer;
	}



}
