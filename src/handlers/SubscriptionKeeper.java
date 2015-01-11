package handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import engines.GUIEngine;
import occupiers.Lokum;
import cas.Board;
import cas.XMLParser;

public class SubscriptionKeeper {

	private static SubscriptionKeeper instance;

	private ArrayList<SwapListener> swapListenersList;
	private ArrayList<MatchListener> matchListenersList;
	private ArrayList<ExplodeListener> explodeListenersList;
	private int comboCount;
	private int timeBonusCount;

	private File xmlFile = new File("xml/listeners.xml");

	private SubscriptionKeeper() {
		// Arraylists are gonna be filled from XML

		setComboCount(0);
		setTimeBonusCount(0);

		swapListenersList = new ArrayList<SwapListener>();
		matchListenersList = new ArrayList<MatchListener>();
		explodeListenersList = new ArrayList<ExplodeListener>();
		loadListeners();
	}

	public int getComboCount() {
		return comboCount;
	}

	public void setComboCount(int count) {
		this.comboCount = count;
	}

	public int getTimeBonusCount() {
		return timeBonusCount;
	}

	public void setTimeBonusCount(int count) {
		this.timeBonusCount = count;
	}

	public static SubscriptionKeeper getInstance() {
		if (instance == null)
			instance = new SubscriptionKeeper();
		return instance;
	}

	/**
	 * Calls swapHandlers for two lokums and looks for matches
	 * @param lokum1
	 * @param lokum2
	 * @requires two lokums are swappable*
	 * @ensures swaplisteners are triggered
	 */
	public void swapped(Lokum lokum1, Lokum lokum2) {
		setComboCount(0);
		setTimeBonusCount(0);
		for (SwapListener l : swapListenersList) {
			l.lokumSwapped(new SwapEvent(lokum1, lokum2));
		}
		findMatches(lokum1, lokum2);
	}

	/**
	 * Calls matchListeners for the lokum
	 * @param lokum
	 * @requires lokum is not null
	 * @ensures
	 */
	public void findMatch(Lokum lokum) {

		for (MatchListener l : matchListenersList) {
			if (lokum.getPosition() != null) // if previous match tries didn't succeed, try it.
				l.match(new MatchEvent(lokum));
		}
		//	Board.getInstance().fillDown(GUIEngine.getInstance().getAnimTag());
	}

	/**
	 * Calls matchListeners for the lokum
	 * @param lokum1
	 * @param lokum2
	 * @requires lokum1 lokum2 is not null
	 * @ensures
	 */
	public void findMatches(Lokum lokum1, Lokum lokum2) {
		for (MatchListener l : matchListenersList) {
			if (lokum1.getPosition() != null) // if previous match tries didn't succeed, try it.
				l.match(new MatchEvent(lokum1));

			if (lokum2.getPosition() != null) // if previous match tries didn't succeed, try it.
				l.match(new MatchEvent(lokum2));
		}
		Board.getInstance().fillDown(GUIEngine.getInstance().getNextTag());
		Board.getInstance().findAllMatches();
	}

	/**
	 * Calls all exploders for all lokums in the list
	 * @param lokumList
	 * @ensures all lokums in the list are called to be exploded
	 */
	public void explode(ArrayList<Lokum> lokumList) {	
		for (ExplodeListener l : explodeListenersList) {
			for (Lokum lokum : lokumList) {
				if (lokum != null && lokum.getPosition() != null) // if it wasn't exploded before, then explode.
					l.explode(new ExplodeEvent(lokum,comboCount));
			}

		}
		System.out.println(Board.getInstance().toString());
	}

	private void loadListeners() {
		Document document;
		try {
			document = XMLParser.getDomElement(xmlFile);
			NodeList swapListeners = document
					.getElementsByTagName("swap-listener");
			for (int i = 0; i < swapListeners.getLength(); i++) {
				Node n = swapListeners.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String classname = XMLParser.getValue(e, "classname");
					SwapListener sl = (SwapListener) Class.forName(
							"handlers." + classname).newInstance();
					this.swapListenersList.add(sl);
				}
			}

			NodeList matchListeners = document
					.getElementsByTagName("match-listener");
			for (int i = 0; i < matchListeners.getLength(); i++) {
				Node n = matchListeners.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String classname = XMLParser.getValue(e, "classname");
					MatchListener ml = (MatchListener) Class.forName(
							"handlers." + classname).newInstance();
					this.matchListenersList.add(ml);
				}
			}

			NodeList explodeListeners = document
					.getElementsByTagName("explode-listener");
			for (int i = 0; i < explodeListeners.getLength(); i++) {
				Node n = explodeListeners.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String classname = XMLParser.getValue(e, "classname");
					ExplodeListener el = (ExplodeListener) Class.forName(
							"handlers." + classname).newInstance();
					this.explodeListenersList.add(el);
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException
				| InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean repOK() {
		if (swapListenersList == null) {
			return false;
		} else if (matchListenersList == null) {
			return false;
		} else if (explodeListenersList == null) {
			return false;
		}	
		return true;
	}
}
