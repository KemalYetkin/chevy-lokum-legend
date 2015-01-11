package scomponents;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * The class generates modified buttons for GUI
 * @author atilberk
 *
 */
@SuppressWarnings("serial")
public class SButton extends JButton {

	/**
	 * Font field for the label
	 */
	private Font font;
	/**
	 * Type field of the label
	 */
	private int type;

	/**
	 * Constructor
	 * Creates a button with given string of given type
	 * @param text
	 * @param type
	 */
	public SButton(String text, int type) {
		super(text);
		loadFont();
		this.setFont(font);
		this.setType(type);
	}

	/**
	 * Constructor
	 * Creates a button of given type
	 * @param type
	 */
	public SButton(int type) {
		super();
		loadFont();
		setType(type);
	}

	/**
	 * Constructor
	 * Creates a button with given icon image of given type
	 * @param icon image
	 * @param type
	 */
	public SButton(Icon icon, int type) {
		super(icon);
		loadFont();
		this.setFont(font);
		this.setType(type);
	}

	/**
	 * Sets the font field from the source, sets font to Helvetica if file reading process fails.
	 */
	private void loadFont() {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("assets/fonts/baveuse.ttf"));
		} catch (Exception e) {
			font = new Font("Helvetica", Font.PLAIN, 22);
			e.printStackTrace();
		}
	}

	/**
	 * Changes the state of iconic, dual state buttons.
	 * Does nothing on other types of buttons.
	 */
	public void changeState() {
		if (Math.abs(type) > 10) {
			setType(type * -1);
		}
	}

	/**
	 * Sets the icon of the button to the corresponding icon for the given type.
	 * @param type
	 */
	private void setIcon(int type) {
		super.setIcon(getIcon(type));
	}

	/**
	 * Returns the icon of the button corresponding to the given type
	 * @param type
	 * @return icon image
	 */
	private static Icon getIcon(int type) {
		String filename = "assets/images/";
		switch (type) {
		case SOUND_BUTTON_MUTE:
			filename += "music_off.png" ;
			break;
		case SOUND_BUTTON_UNMUTE:
			filename += "music_on.png" ;
			break;
		case SOUND_BUTTON_EFFECT_ON:
			filename += "effects_on.png" ;
			break;
		case SOUND_BUTTON_EFFECT_OFF:
			filename += "effects_off.png" ;
			break;
		default:
			filename = "";
		}
		return new ImageIcon(filename);
	}

	/**
	 * Sets the type of the label
	 * @param type
	 */
	private void setType(int t) {
		this.type = t;
		float f = 24F;
		Color color = SColor.buttonColor;
		switch (type) {
		case MAIN_MENU_BUTTON:
			f = 52F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 5));
			this.setForeground(SColor.white);
			this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
			this.setPreferredSize(new Dimension(240,60));
			break;
		case START_MENU_BUTTON:
			f = 18F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 5));
			this.setForeground(SColor.white);
			this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
			this.setPreferredSize(new Dimension(240,60));
			break;
		case SUB_MENU_BUTTON:
			f = 14F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setForeground(SColor.white);
			this.setMinimumSize(new Dimension(150,80));
			break;
		case LEVEL_CHOOSE_BUTTON_ON:
			f = 36F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setForeground(SColor.white);
			this.setMinimumSize(new Dimension(80,80));
			break;
		case GAME_LOAD_BUTTON:
			f = 18F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setForeground(SColor.white);
			this.setMinimumSize(new Dimension(160,160));
			break;
		case LEVEL_CHOOSE_BUTTON_OFF:
			f = 36F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.backgroundColor, 3));
			this.setForeground(SColor.buttonDisabledColor);
			this.setMinimumSize(new Dimension(80,80));
			break;
		case SOUND_BUTTON_MUTE:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(color);
			this.setPreferredSize(new Dimension(40,40));
			this.setIcon(type);
			break;
		case SOUND_BUTTON_UNMUTE:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(color);
			this.setPreferredSize(new Dimension(40,40));
			this.setIcon(type);
			break;

		case SOUND_BUTTON_EFFECT_ON:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(color);
			this.setPreferredSize(new Dimension(40,40));
			this.setIcon(type);
			break;
		case SOUND_BUTTON_EFFECT_OFF:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(color);
			this.setPreferredSize(new Dimension(40,40));
			this.setIcon(type);
			break;

		case SPECIAL_SWAP_ON:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(color);
			this.setForeground(SColor.white);
			this.setMinimumSize(new Dimension(60,40));
			this.setPreferredSize(new Dimension(60,40));
			this.setIcon(type);
			break;
		case SPECIAL_SWAP_OFF:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(SColor.PINK);
			this.setForeground(SColor.white);
			this.setMinimumSize(new Dimension(60,40));
			this.setPreferredSize(new Dimension(60,40));
			break;

		case SUBMIT_BUTTON:
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setBackground(color);
			this.setForeground(SColor.white);
			this.setPreferredSize(new Dimension(160,40));
			this.setIcon(type);

		case GAME_OVER_BUTTON:
			f = 34F;
			this.setBackground(color);
			this.setBorder(BorderFactory.createLineBorder(SColor.white, 3));
			this.setForeground(SColor.white);
			this.setPreferredSize(new Dimension(240,70));

			break;
		default:
			this.setSize(140, 40);
			break;
		}
		font = font.deriveFont(f);
		this.setFont(font);
	}

	/**
	 * Type values of SLabel
	 */	
	public static final int MAIN_MENU_BUTTON = 1;
	public static final int START_MENU_BUTTON = 2;
	public static final int SUB_MENU_BUTTON = 3;
	public static final int SUBMIT_BUTTON = 4;
	public static final int GAME_LOAD_BUTTON = 5;
	public static final int LEVEL_CHOOSE_BUTTON_ON = 10;
	public static final int LEVEL_CHOOSE_BUTTON_OFF = -10;
	public static final int SOUND_BUTTON_UNMUTE = 11;
	public static final int SOUND_BUTTON_MUTE = -11;
	public static final int SOUND_BUTTON_EFFECT_ON = 111;
	public static final int SOUND_BUTTON_EFFECT_OFF = -111;
	public static final int SPECIAL_SWAP_ON = 12;
	public static final int SPECIAL_SWAP_OFF = -12;
	public static final int GAME_OVER_BUTTON = 6;
}
