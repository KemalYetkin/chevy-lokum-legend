package occupiers;

public class RegularLokum extends Lokum {

	/**
	 * Constructor
	 * Creates a regular Lokum whose type is regular
	 */
	public RegularLokum() {
		super("regular");
	}

	/**
	 * Constructor
	 * @param type the special type of the regular Lokum like time
	 */
	public RegularLokum(String type){
		super(type);
	}

	/**
	 * @return false since the objects of this class are not a special Lokum
	 */
	public boolean isSpecialLokum(){
		return false;
	}
}
