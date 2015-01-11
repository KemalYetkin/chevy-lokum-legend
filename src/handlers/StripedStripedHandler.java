package handlers;

import java.util.ArrayList;
import occupiers.Lokum;
import occupiers.StripedLokum;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of two striped lokum
 */
public class StripedStripedHandler implements SwapListener {

	public StripedStripedHandler() {}

	/**
	 * Adds all lokums on one row and one column on the on board to the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case, all lokums on one row and one column on the on board are added to the explosion queue and and explosion triggered
	 */
	@Override
	public void lokumSwapped(SwapEvent e) {
		if((e.getLokum1() instanceof StripedLokum && e.getLokum2() instanceof StripedLokum)){
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();
			StripedLokum lok1 = (StripedLokum) e.getLokum1();
			StripedLokum lok2 = (StripedLokum) e.getLokum2();
			lok1.setDirection("horizontal");
			lok2.setDirection("vertical");
			lokumsToBeExploded.add(lok1);
			lokumsToBeExploded.add(lok2);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
		}
	}
}
