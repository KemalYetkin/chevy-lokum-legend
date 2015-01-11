package occupiers;

public abstract class SpecialLokum extends Lokum {

	/**
	 * Constructor
	 * @param type the type of the special Lokum like wrapped, stripped etc.
	 */
	public SpecialLokum(String type) {
		super(type);
	}

	/**
	 * @return true since the objects of this class are always special Lokums
	 */
	public boolean isSpecialLokum(){
		return true;
	}
}
