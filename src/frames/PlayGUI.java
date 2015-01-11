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
import cas.AudioPlayers;
import cas.Level;
import cas.TimeScoreGoal;
import engines.GUIEngine;
import engines.GameEngine;
import scomponents.SButton;
import scomponents.SColor;
import scomponents.SLabel;

@SuppressWarnings("serial")
public class PlayGUI extends JFrame{

	private BoardGUI board;
	private boolean mute = GUIEngine.getInstance().getMusicStatus();
	private boolean effectMute = true;
	SLabel levelSign;
	SLabel targetSign;
	SLabel movesSign;
	SLabel scoreSign;
	SLabel timeSign;
	SButton specialSwapButton;

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
		this.setSize(980,630);
		this.getContentPane().setBackground(SColor.panelBackgroundColor);
		this.setLocationRelativeTo(null);
		AudioPlayers.getInstance().disableOrEnableMenuSound(false);
		AudioPlayers.getInstance().disableOrEnableBackgroundSounds(mute);
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
		sidePanel.setPreferredSize(new Dimension(350,600));

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

		JPanel midLabelPanel = new JPanel();
		midLabelPanel.setBackground(SColor.panelBackgroundColor);
		midLabelPanel.setPreferredSize(new Dimension(350,100));
		JPanel midLabelInnerPanel = new JPanel();
		midLabelInnerPanel.setPreferredSize(new Dimension(350,100));
		midLabelInnerPanel.setBackground(SColor.panelBackgroundColor);
		SLabel specialSwapLabel = new SLabel("special swap:", SLabel.PLAY_LABEL, SLabel.RIGHT);
		midLabelInnerPanel.add(specialSwapLabel);
		specialSwapButton = new SButton(GameEngine.getInstance().getSpecialSwapsLeft()+"", SButton.SPECIAL_SWAP_ON);
		specialSwapButton.setEnabled(GameEngine.getInstance().getSpecialSwapsLeft() > 0);
		specialSwapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIEngine.getInstance().specialSwapButtonClicked();
			}
		});
		midLabelInnerPanel.add(specialSwapButton);
		midLabelPanel.add(midLabelInnerPanel);


		JPanel bottomLabelPanel = new JPanel();
		bottomLabelPanel.setBackground(SColor.panelBackgroundColor);
		bottomLabelPanel.setLayout(new GridLayout(3,2,10,10));

		SLabel timeLabel = new SLabel("time left:", SLabel.PLAY_LABEL, SLabel.RIGHT);
		bottomLabelPanel.add(timeLabel);
		timeSign = new SLabel((Level.getInstance().getGoal() instanceof TimeScoreGoal) ? GameEngine.getInstance().getTimeLeft()+"" : "\u221E", SLabel.PLAY_SIGN, SLabel.LEFT);
		bottomLabelPanel.add(timeSign);

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

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				GameEngine.getInstance().stopTimer();
				SaveGameGUI sgg = new SaveGameGUI();	
				sgg.show();
			}
		});
		buttonPanel.add(saveButton);
		SButton quitButton = new SButton("Quit", SButton.SUB_MENU_BUTTON);
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameEngine.getInstance().stopTimer();
				AudioPlayers.getInstance().disableOrEnableAllBackMusic(false);
				dispose();
				GUIEngine.getInstance().returnMenu();				
			}

		});
		buttonPanel.add(quitButton);

		int buttonType = !mute ? SButton.SOUND_BUTTON_MUTE
				: SButton.SOUND_BUTTON_UNMUTE;
		final SButton musicButton = new SButton(buttonType);
		musicButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mute = !mute;
				GUIEngine.getInstance().setMusicStatus(mute);
				AudioPlayers.getInstance().disableOrEnableBackgroundSounds(mute);
				musicButton.changeState();
			}
		});
		buttonPanel.add(musicButton);

		int effectType = !effectMute ? SButton.SOUND_BUTTON_EFFECT_OFF
				: SButton.SOUND_BUTTON_EFFECT_ON;
		final SButton effectButton = new SButton(effectType);
		//add listeners to button
		effectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				effectMute = !effectMute;
				AudioPlayers.getInstance().disableOrEnableEffects(effectMute);
				effectButton.changeState();
			}
		});
		buttonPanel.add(effectButton);

		sidePanel.add(Box.createVerticalGlue());
		sidePanel.add(topLabelPanel);
		sidePanel.add(Box.createVerticalStrut(60));
		sidePanel.add(midLabelPanel);
		sidePanel.add(Box.createVerticalStrut(60));
		sidePanel.add(bottomLabelPanel);
		sidePanel.add(Box.createVerticalStrut(60));
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

	public void setTimeLeft(String time) {	
		String hex = "221E";
		int intValue = Integer.parseInt(hex, 16);
		System.out.println((char)intValue);
		timeSign.setText((Level.getInstance().getGoal() instanceof TimeScoreGoal) ? time : hex);
	}

	public void setSpecialSwapLeft(int swapCount){
		specialSwapButton.setText(swapCount+"");
		if (swapCount == 0)
			specialSwapButton.setEnabled(false);
	}

	public void toggleSpecialSwapButton() {
		specialSwapButton.changeState();
	}
}
