package handlers;

import occupiers.Lokum;

/**
 * @author atilberk
 */
public class MatchEvent {

	private Lokum lokum;

	public MatchEvent(Lokum l) {
		this.lokum = l;
	}

	public Lokum getLokum() {
		return lokum;
	}
}
