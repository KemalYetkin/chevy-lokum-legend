package occupiers;

public abstract class Lokum extends SquareOccupier {
	
	/**
	 * Constructor
	 * @param type the type of the Lokum (e.g. time, regular, stripped etc.)
	 */
	public Lokum(String type) {
		super(type);
	}
	
	/**
	 * @return the color of the lokum
	 */
	public String getColor() {
		return ((LokumDescription) description).getColor();
	}
	
	/**
	 * @ensures there must a representation of the Lokum
	 * @param color new color of the Lokum
	 */
	public void setColor(String color){
		if (getDescription() instanceof LokumDescription)
			((LokumDescription) getDescription()).setColor(color);
		else
			setDescription(new LokumDescription(getDescription().getType(), color));
	}
	
	/**
	 * @return true since the objects of this class is always Lokum
	 */
	public boolean isLokum(){
		return true;
	}
	
	/**
	 * @return false since the objects of this class are not Obstacle
	 */
	public boolean isObstacle(){
		return false;
	}
	
	abstract public boolean isSpecialLokum();
}
