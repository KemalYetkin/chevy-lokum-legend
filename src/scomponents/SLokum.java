package scomponents;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import cas.Position;
import engines.GUIEngine;

@SuppressWarnings("serial")
public class SLokum extends SOccupier {

	public static enum whatWillBeTriggeredAfterThisLokum {
		FALL, GENERATION, FINDMATCHES, IDLE
	};

	private String color;

	public SLokum(String type, String color) {
		super(type);
		setColor(color);
		setIcon();
		addLokumListeners();
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

	public void moveFoo(Position target) {
		//	System.out.println("Moved! @ "+ Thread.currentThread().getName());
		setLocation((target.getX() * width()) + (width()/2),
				(target.getY() * height()) + (height()/2));

	}

	class MoverListenerFoo implements Runnable {

		private Position target;
		public MoverListenerFoo(Position target){
			this.target= target;
		}

		@Override
		public void run() {
			setLocation((target.getX() * width()) + (width()/2),
					(target.getY() * height()) + (height()/2));
		}
	}

	class MoveLoop implements Runnable {
		private Timer timer;
		private int targetX;
		private int targetY;

		@Override
		public void run() {
			timer.start();

			int x = getX();
			int counter = 0;
			int y = getY();
			while ( x != targetX || y != targetY && counter < (targetY-y)*12) {
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
