package frames;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cas.Level;
import engines.GUIEngine;
import engines.GameEngine;
import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;

public class PlayGUI extends JFrame{

	private BoardGUI board;
	
	SLabel levelSign;
	SLabel targetSign;
	SLabel movesSign;
	SLabel scoreSign;
	

	public PlayGUI() {
		super();
		init();

		this.board = new BoardGUI();
		this.getContentPane().add(board);
		this.getContentPane().add(createSidePanel());
		this.setVisible(true);
	}

	private void init() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.setSize(900,600);
		this.getContentPane().setBackground(SColor.panelBackgroundColor);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}	

	private JPanel createSidePanel() {
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		sidePanel.setBackground(SColor.panelBackgroundColor);
		sidePanel.setPreferredSize(new Dimension(300,600));

		JPanel topLabelPanel = new JPanel();
		topLabelPanel.setBackground(SColor.panelBackgroundColor);
		topLabelPanel.setLayout(new GridLayout(2,2,10,10));

		SLabel levelLabel = new SLabel("level:", SLabel.PLAY_LABEL, SLabel.RIGHT);
		topLabelPanel.add(levelLabel);
		
		levelSign = new SLabel(GameEngine.getInstance().getLevel().getLevelNumber()+"", SLabel.PLAY_SIGN, SLabel.LEFT);
		topLabelPanel.add(levelSign);

		SLabel targetLabel = new SLabel("target:", SLabel.PLAY_LABEL, SLabel.RIGHT);
		topLabelPanel.add(targetLabel);
		 targetSign = new SLabel(Level.getInstance().getGoal().toString(), SLabel.PLAY_SIGN, SLabel.LEFT);
		topLabelPanel.add(targetSign);

		JPanel bottomLabelPanel = new JPanel();
		bottomLabelPanel.setBackground(SColor.panelBackgroundColor);
		bottomLabelPanel.setLayout(new GridLayout(2,2,10,10));

		SLabel movesLabel = new SLabel("moves left:", SLabel.PLAY_LABEL, SLabel.RIGHT);
		bottomLabelPanel.add(movesLabel);
		 movesSign = new SLabel(GameEngine.getInstance().getMovesLeft()+"", SLabel.PLAY_SIGN, SLabel.LEFT);
		bottomLabelPanel.add(movesSign);

		SLabel scoreLabel = new SLabel("score:", SLabel.PLAY_LABEL, SLabel.RIGHT);
		bottomLabelPanel.add(scoreLabel);
		 scoreSign = new SLabel(GameEngine.getInstance().getScore()+"", SLabel.PLAY_SIGN, SLabel.LEFT);
		bottomLabelPanel.add(scoreSign);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(SColor.panelBackgroundColor);
		buttonPanel.setLayout(new GridLayout(1,2,10,10));	

		SButton saveButton = new SButton("Save", SButton.SUB_MENU_BUTTON);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SaveGameGUI sgg = new SaveGameGUI();				
			}

		});
		buttonPanel.add(saveButton);
		SButton quitButton = new SButton("Quit", SButton.SUB_MENU_BUTTON);
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				GUIEngine.getInstance().returnMenu();				
			}

		});
		buttonPanel.add(quitButton);

		sidePanel.add(Box.createVerticalGlue());
		sidePanel.add(topLabelPanel);
		sidePanel.add(Box.createVerticalStrut(100));
		sidePanel.add(bottomLabelPanel);
		sidePanel.add(Box.createVerticalStrut(100));
		sidePanel.add(buttonPanel);
		sidePanel.add(Box.createVerticalGlue());

		return sidePanel;
	}

	public BoardGUI getBoardGUI() {
		return board;
	}
	
	public void setLevel(String level) {
		levelSign.setText(level);
	}
	public void setTarget(String target) {
		targetSign.setText(target);
	}
	public void setMovesLeft(String movesLeft) {
		movesSign.setText(movesLeft);
	}
	public void setScore(String score) {
		scoreSign.setText(score);
	}

}
