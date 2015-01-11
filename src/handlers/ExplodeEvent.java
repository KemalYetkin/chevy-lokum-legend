package handlers;

import occupiers.Lokum;

public class ExplodeEvent {

	private Lokum lokum;
	private int comboCount;

	public ExplodeEvent(Lokum l, int comboCount) {
		setLokum(l);
		setComboCount(comboCount);
	}

	public int getComboCount() {
		return comboCount;
	}

	public void setComboCount(int comboCount) {
		this.comboCount = comboCount;
	}

	public Lokum getLokum() {
		return lokum;
	}

	public void setLokum(Lokum lokum) {
		this.lokum = lokum;
	}

}
