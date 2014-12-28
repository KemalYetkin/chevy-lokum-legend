package scomponents;

import javax.swing.JLabel;


public abstract class SOccupier extends JLabel {
	
	private String type;

	private int iconWidth;
	private int iconHeight;


	public SOccupier(String type) {
		super();
		setType(type);
		
	}

	public String getType() {
		return this.type;
	}
	
	private void setType(String type) {
		this.type = type;
	}
	
	public int width() {
		return iconWidth;
	}
	public int height() {
		return iconHeight;
	}
	
	protected void setWidth(int w) {
		iconWidth = w;
	}
	protected void setHeight(int h) {
		iconHeight = h;
	}
	
	protected abstract String getImageFileName();
	

	public void appear() {
		Thread t = new Thread(new AppearLoop());
		t.start();
	}
	
	class AppearLoop implements Runnable {
		
		@Override
		public void run() {
			
			while (getWidth() < iconWidth ) {
				try {
					setBounds(getX()-1, getY()-1, getWidth()+2, getHeight()+2);
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		
	}
}
