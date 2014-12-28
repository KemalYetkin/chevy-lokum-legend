package scomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;

public class SLokum extends SOccupier {

	public static enum whatWillBeTriggeredAfterThisLokum {
		FALL, GENERATION, FINDMATCHES, IDLE
	};

	private int distance;
	private boolean isThisLastLokumToBeGenerated;

	private String color;
	private whatWillBeTriggeredAfterThisLokum triggerType;
	

	public SLokum(String type, String color) {
		super(type);
		setTotalTravelDistance(1);
		setLastGenerationStatus(false);
		setColor(color);
		setIcon();
		addLokumListeners();
		setTriggerType(whatWillBeTriggeredAfterThisLokum.IDLE);
	}

	
	public String getColor() {
		return this.color;
	}

	private void setColor(String color) {
		this.color = color;
	}

	private void setIcon() {
		ImageIcon icon = new ImageIcon(getImageFileName());
		setWidth(icon.getIconWidth());
		setHeight(icon.getIconHeight());
		super.setIcon(icon);
	}

	protected String getImageFileName() {
		String fn = "assets/images/" + getType() + "_" + getColor() + ".png";
		//	System.out.println(fn);
		return fn;
	}

	public void setTriggerType(whatWillBeTriggeredAfterThisLokum triggerType) {
		this.triggerType = triggerType;
	}

	public void setTotalTravelDistance(int distance){
		this.distance = distance;
	}

	public void setLastGenerationStatus(boolean bool){
		isThisLastLokumToBeGenerated = bool;
	}

	private void addLokumListeners() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				SLokum l = ((SLokum) e.getSource());
				Position p = new Position((l.getX() - (width()/2)) / width(),
						(l.getY() - (height()/2)) / height());
				System.out.println("("+p.getX()+","+p.getY()+")");
				GUIEngine.getInstance().buttonClicked(p);
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

		});
	}

	public void moveToPosition(Position pos) {
		Thread t = new Thread(new MoveLoop(pos));
		t.start();
	}

	class MoveLoop implements Runnable {

		private Position target;
		private Timer timer;
		private int initialY;
		private int initialX;
		private int targetX;
		private int targetY;
		private ActionListener signalEnoughAnimation;
		private int maximumLoop = 6000;
		private int loopCounter = 0;

		public MoveLoop(Position pos) {
			this.target = pos;
			targetX = (target.getX() * width()) + (width()/2);
			targetY = (target.getY() * height()) + (height()/2);
			initialY = getY();
			initialX = getX();
			
			signalEnoughAnimation = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loopCounter++;
					if (loopCounter >= maximumLoop){
						loopCounter = 0;
						timer.stop();
					}
					if (triggerType == whatWillBeTriggeredAfterThisLokum.FALL
							&& Math.abs(getY() - initialY) >= height()
							|| Math.abs(getX() - initialX) >= width()) {
						GameEngine.getInstance().completeSwap();
						triggerType = whatWillBeTriggeredAfterThisLokum.IDLE;
						timer.stop();
					} else if (triggerType == whatWillBeTriggeredAfterThisLokum.GENERATION
							&& Math.abs(getY() - initialY) >= distance *height()) {
						Board.getInstance().generateLokumsToEmptySquares();
						triggerType = whatWillBeTriggeredAfterThisLokum.IDLE;
						timer.stop();
					} else if (triggerType == whatWillBeTriggeredAfterThisLokum.FINDMATCHES){
						//System.out.println(isThisLastLokumToBeGenerated);
						if (Math.abs(getY() - initialY) >= height() || Math.abs(getX() - initialX) >= width()) {
							Board.getInstance().generateLokumsToEmptySquares();
							setLastGenerationStatus(false);
							triggerType = whatWillBeTriggeredAfterThisLokum.IDLE;
							timer.stop();
						} else if (isThisLastLokumToBeGenerated){
							setLastGenerationStatus(false);
							triggerType = whatWillBeTriggeredAfterThisLokum.IDLE;
							timer.stop();
							Board.getInstance().findAllMatches();
						}
					} else if (triggerType == whatWillBeTriggeredAfterThisLokum.IDLE){
						timer.stop();
					}
				}
			};
			timer = new Timer(1, signalEnoughAnimation);
		}

		@Override
		public void run() {
			timer.start();

			int x = getX();
			int counter = 0;
			int y = getY();
			int c = Math.max(Math.abs(targetX - x), Math.abs(targetY - y));
			while ( x != targetX || y != targetY && counter < (targetY-y)*12) {
			//for (int f = 0; f < c; f++) {
				try {
					setLocation(x + ((int) Math.signum(targetX - x)),
							y + ((int) Math.signum(targetY - y)));
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				x = getX();
				y = getY();
				counter++;
			}
			setLocation(targetX,targetY);
		}

	}

}
