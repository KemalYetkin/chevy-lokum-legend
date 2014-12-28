package occupiers;

import cas.Position;

public abstract class SquareOccupier {
	// the position of the square occupier
	protected Position position;
	// the description of the square occupier
	protected SquareOccupierDescription description;
	
	/**
	 * Constructor
	 * @param type the type of the square occupier
	 * @modifies description, position
	 */
	public SquareOccupier(String type) {
		setPosition(null);
		description = new SquareOccupierDescription(type);
	}
	
	/**
	 * @return the position of the square occupier
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * @modifies position
	 * @param position new position of the square occupier
	 */
	public void setPosition(Position position){
		this.position = position;
	}
	
	/**
	 * @return the description of the square occupier
	 */
	public SquareOccupierDescription getDescription() {
		return description;
	}
	
	/**
	 * @modifies description
	 * @param desc new description of the square occupier
	 */
	protected void setDescription(SquareOccupierDescription desc) {
		this.description = desc;
	}
	
	abstract public boolean isLokum();
	abstract public boolean isObstacle();
}
