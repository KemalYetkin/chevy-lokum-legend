package handlers;

import occupiers.Lokum;

public class SwapEvent {

	private Lokum lokum1;
	private Lokum lokum2;

	/**
	 * Constructor
	 * @param lokum1
	 * @param lokum2
	 */
	public SwapEvent(Lokum lokum1, Lokum lokum2) {
		this.setLokum1(lokum1);
		this.setLokum2(lokum2);
	}

	public Lokum getLokum1() {
		return lokum1;
	}

	public void setLokum1(Lokum lokum1) {
		this.lokum1 = lokum1;
	}

	public Lokum getLokum2() {
		return lokum2;
	}

	public void setLokum2(Lokum lokum2) {
		this.lokum2 = lokum2;
	}
}
