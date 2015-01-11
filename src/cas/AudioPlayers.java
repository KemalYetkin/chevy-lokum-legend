package cas;

import java.awt.event.*;
import java.io.*;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * 
 * @author ogunoz
 * 
 */
public class AudioPlayers {
	private Timer timerPlayGUIBackground;
	private Timer timerWin;
	private Timer timerLose;
	private Timer timerMenu;
	private MusicLoopPlayerListener musicPlayerListener;
	private MenuLoopListener menuLoopListener;
	private GameOverWinListener winOverListener;
	private GameOverLoseListener loseOverListener;
	private static AudioPlayers instance = null;
	private AudioPlayer player;
	private AudioStream as;
	private AudioStream colorStream;

	private boolean stateOfEffects = true;

	/**
	 * constructor of the class
	 */
	private AudioPlayers() {
		musicPlayerListener = new MusicLoopPlayerListener();
		winOverListener = new GameOverWinListener();
		loseOverListener = new GameOverLoseListener();
		menuLoopListener = new MenuLoopListener();
		timerPlayGUIBackground = new Timer(138050, musicPlayerListener);
		timerWin = new Timer(21100, winOverListener);
		timerLose = new Timer(32100, loseOverListener);
		timerMenu = new Timer(161500, menuLoopListener);
		player = AudioPlayer.player;
	}

	/**
	 * @ensures if an instance has already been created before the call of this
	 *          method, returns that instance; if not, then create a new
	 *          instance of the class and return it.
	 * @modifies instance
	 * @return the instance of the class
	 */
	public static AudioPlayers getInstance() {
		if (instance == null) {
			instance = new AudioPlayers();
		}
		return instance;
	}

	/**
	 * @ensures play or stop the music stream.
	 * @requires jaudiotagger, mp3plugin and jl1.0.1 jars should be added to
	 *           java build path. FileInputStream file should be in the correct
	 *           path.
	 * @param boolean status - playing status(boolean)
	 * 
	 */
	public void playExplosionEffect(boolean status) {
		if (stateOfEffects) {
			AudioStream as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/explode.wav");

				as = new AudioStream(in);
				if (status)
					AudioPlayer.player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @ensures play or stop the music stream.
	 * @requires jaudiotagger, mp3plugin and jl1.0.1 jars should be added to
	 *           java build path. FileInputStream file should be in the correct
	 *           path.
	 * @param boolean status - playing status(boolean)
	 * 
	 */
	public void playFallingDoneEffect(boolean status) {
		if (stateOfEffects) {
			AudioStream as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/fall.wav");

				as = new AudioStream(in);
				if (status)
					AudioPlayer.player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @ensures play or stop the music stream.
	 * @requires jaudiotagger, mp3plugin and jl1.0.1 jars should be added to
	 *           java build path. FileInputStream file should be in the correct
	 *           path.
	 * @param boolean status - playing status(boolean)
	 * 
	 */
	public void playIncorrectSelectionEffect(boolean status) {
		if (stateOfEffects) {
			AudioStream as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/incorrect.wav");

				as = new AudioStream(in);
				if (status)
					AudioPlayer.player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @ensures play or stop the music stream.
	 * @requires jaudiotagger, mp3plugin and jl1.0.1 jars should be added to
	 *           java build path. FileInputStream file should be in the correct
	 *           path.
	 * @param boolean status - playing status(boolean)
	 * 
	 */
	public void playWrappedExplodeEffect(boolean status) {
		if (stateOfEffects) {
			AudioStream as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/wrapped.mp3");

				as = new AudioStream(in);
				if (status)
					AudioPlayer.player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @ensures play or stop the music stream.
	 * @requires jaudiotagger, mp3plugin and jl1.0.1 jars should be added to
	 *           java build path. FileInputStream file should be in the correct
	 *           path.
	 * @param boolean status - playing status(boolean)
	 * 
	 */
	public void playStripedExplodeEffect(boolean status) {
		if (stateOfEffects) {
			AudioStream as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/striped.mp3");

				as = new AudioStream(in);
				if (status)
					AudioPlayer.player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @ensures play or stop the music stream.
	 * @requires jaudiotagger, mp3plugin and jl1.0.1 jars should be added to
	 *           java build path. FileInputStream file should be in the correct
	 *           path.
	 * @param boolean status - playing status(boolean)
	 * 
	 */
	public void playColorBombExplodeEffect(boolean status) {
		if (stateOfEffects) {
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/colorBomb.wav");

				colorStream = new AudioStream(in);
				if (status)
					AudioPlayer.player.start(colorStream);
				else
					AudioPlayer.player.stop(colorStream);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @ensures start the timer for background music loop or stop the timer if
	 *          it is alive.
	 * @requires timerPlayGUIBackground has already been constructed.
	 * @param boolean status - starting or stoping status (boolean)
	 * 
	 */
	private void playBackgroundMusic(boolean status) {
		timerPlayGUIBackground.setInitialDelay(0);
		timerPlayGUIBackground.start();

	}

	/**
	 * @ensures start the timer for background music loop or stop the timer if
	 *          it is alive.
	 * @requires timerWin has already been constructed.
	 * @param boolean status - starting or stoping status (boolean)
	 * 
	 */
	private void playWinningMusic(boolean status) {
		timerWin.setInitialDelay(0);
		timerWin.start();
	}

	/**
	 * @ensures start the timer for background music loop or stop the timer if
	 *          it is alive.
	 * @requires timerLose has already been constructed.
	 * @param boolean status - starting or stoping status (boolean)
	 * 
	 */
	private void playLosingMusic(boolean status) {
		timerLose.setInitialDelay(0);
		timerLose.start();
	}

	/**
	 * @ensures start the timer for background music loop or stop the timer if
	 *          it is alive.
	 * @requires timerMenu has already been constructed.
	 * @param boolean status - starting or stoping status (boolean)
	 * 
	 */
	private void playMenuMusic(boolean status) {
		timerMenu.setInitialDelay(0);
		timerMenu.start();
	}

	/**
	 * @requires a boolean field stateOfEffects has to be already initialized.
	 * @modifies the boolean field holding state of the music effects.
	 * @param boolean status
	 */
	public void disableOrEnableEffects(boolean status) {
		stateOfEffects = status;
	}

	/**
	 * @ensures start/stop the play GUI background music
	 * @requires a timer for this loop, has to be already initialized.
	 * @param boolean status
	 */
	public void disableOrEnableBackgroundSounds(boolean status) {
		if (!status) {
			if (player != null) {
				player.stop(as);

			}
			if (timerPlayGUIBackground.isRunning())
				timerPlayGUIBackground.stop();
		} else {
			playBackgroundMusic(status);
		}
	}

	/**
	 * @ensures start/stop the game over win scenario music.
	 * @requires a timer for this loop, has to be already initialized.
	 * @param boolean status
	 */
	public void disableOrEnableGameOverWinSound(boolean status) {
		if (!status) {
			if (player != null)
				player.stop(as);
			if (timerWin.isRunning())
				timerWin.stop();
		} else {
			playWinningMusic(status);
		}
	}

	/**
	 * @ensures start/stop the game over loose scenario music.
	 * @requires a timer for this loop, has to be already initialized.
	 * @param boolean status
	 */
	public void disableOrEnableGameOverLooseSound(boolean status) {
		if (!status) {
			if (player != null)
				player.stop(as);
			if (timerLose.isRunning())
				timerLose.stop();
		} else {
			playLosingMusic(status);
		}
	}

	/**
	 * @ensures start/stop the menu music.
	 * @requires a timer for this loop, has to be already initialized.
	 * @param boolean status
	 */
	public void disableOrEnableMenuSound(boolean status) {
		if (!status) {
			if (player != null)
				player.stop(as);
			if (timerMenu.isRunning())
				timerMenu.stop();
		} else {
			playMenuMusic(status);
		}
	}

	/**
	 * @ensures call the other disable/enable methods with its parameter.
	 * @param boolean status
	 */
	public void disableOrEnableAllBackMusic(boolean status) {
		disableOrEnableBackgroundSounds(status);
		disableOrEnableGameOverLooseSound(status);
		disableOrEnableGameOverWinSound(status);
		disableOrEnableMenuSound(status);
	}

	class MusicLoopPlayerListener implements ActionListener {
		/**
		 * Constructor
		 * 
		 */
		public MusicLoopPlayerListener() {
		}

		/**
		 * @ensures start the audio stream.
		 * @requires mp3 file should be in the correct path. Timer must be
		 *           started.
		 * @param ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/background.mp3");

				as = new AudioStream(in);
				player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	class GameOverWinListener implements ActionListener {
		/**
		 * Constructor
		 * 
		 */
		public GameOverWinListener() {
		}

		/**
		 * @ensures start the audio stream.
		 * @requires mp3 file should be in the correct path. Timer must be
		 *           started.
		 * @param ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/lokumGibi.mp3");

				as = new AudioStream(in);
				player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	class GameOverLoseListener implements ActionListener {
		/**
		 * Constructor
		 * 
		 */
		public GameOverLoseListener() {
		}

		/**
		 * @ensures start the audio stream.
		 * @requires mp3 file should be in the correct path. Timer must be
		 *           started.
		 * @param ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			as = null;
			InputStream in = null;
			try {
				in = new FileInputStream(
						"assets/music/kaybedenYineBenOldum.mp3");

				as = new AudioStream(in);
				player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	class MenuLoopListener implements ActionListener {
		/**
		 * Constructor
		 * 
		 */
		public MenuLoopListener() {
		}

		/**
		 * @ensures start the audio stream.
		 * @requires mp3 file should be in the correct path. Timer must be
		 *           started.
		 * @param ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			as = null;
			InputStream in = null;
			try {
				in = new FileInputStream("assets/music/trolololol.mp3");

				as = new AudioStream(in);
				player.start(as);

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
}
