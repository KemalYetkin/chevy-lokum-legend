package scomponents;

import java.awt.*;
import java.io.FileInputStream;

import javax.swing.*;

/**
 * The class generates modified labels for GUI
 * @author atilberk
 */
public class SLabel extends JLabel {

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
	 * Creates an empty label.
	 */
	public SLabel(){
		super();
	}
	
	/**
	 * Constructor
	 * Creates a label with given type.
	 */
	public SLabel(int type){
		this("", type);
	}
	
	/**
	 * Constructor
	 * Creates a label of given type with empty string and horizontal alignment
	 */
	public SLabel(int type, int horizontalAlignment){
		this("", type, horizontalAlignment);
	}
	
	/**
	 * Constructor
	 * Creates a label with given text of given type.
	 */
	public SLabel(String text, int type) {
		super(text);
		this.loadFont();
		this.setFont(font);
		this.setType(type);
	}
	
	/**
	 * Constructor
	 * Creates a label with given text of given type with horizontal alignment.
	 */
	public SLabel(String text, int type, int horizontalAlignment) {
		super(text, horizontalAlignment);
		this.loadFont();
		this.setFont(font);
		this.setType(type);
	}
	
	/**
	 * Loads the font, if fails loads Helvetica as default
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
	 * Sets the type of the label
	 * @param type
	 */
	private void setType(int t) {
		this.type = t;
		float f;
		switch (t) {
			case MAIN_MENU_TITLE:
				f = 42F;
				this.setForeground(Color.white);
				break;
			case MAIN_MENU_SUBTITLE:
				f = 18F;
				this.setForeground(Color.white);
				break;
			case MAIN_MENU_AUTHOR:
				f = 18F;
				this.setForeground(Color.white);
				break;		
			case PLAY_LABEL:
				f = 18F;
				this.setForeground(Color.white);
				break;
			case PLAY_SIGN:
				f = 24F;
				this.setForeground(Color.white);
				break;
			case GAME_NAME:
				f = 20F;
				this.setForeground(Color.white);
				break;
			case GAME_OTHERS:
				f = 14F;
				this.setForeground(Color.white);
				break;
			case GLOW:
				f = 14F;
				this.setForeground(Color.white);
				ImageIcon icon = new ImageIcon("assets/images/glow.png");
				setIcon(icon);
				break;
			default:
				f = 48F;
				break;
		}
		font = font.deriveFont(f);
		this.setFont(font);
	}
	
	/**
	 * Type values of SLabel
	 */	
	public static final int MAIN_MENU_TITLE = 10;
	public static final int MAIN_MENU_SUBTITLE = 11;
	public static final int MAIN_MENU_AUTHOR = 12;
	public static final int GAME_NAME = 13;
	public static final int GAME_OTHERS = 14;
	public static final int PLAY_LABEL = 20;
	public static final int PLAY_SIGN = 21;
	public static final int GLOW = 30;

}
