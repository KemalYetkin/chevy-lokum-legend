package cas;

public class Position {
	private int x;
	private int y;

	/**
	 * Constructor
	 * @param x Position's x value
	 * @param y Position's y value
	 */
	public Position(int x, int y) {
		setX(x);
		setY(y);
	}

	/**
	 * @return the x value of the position
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x new x value of the position
	 */
	private void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y value of the position
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y new y value of the position
	 */
	private void setY(int y) {
		this.y = y;
	}

	/**
	 * @param pos the position whose equality will be checked
	 * @return true if the position is equal to the given position
	 * 		   false vice versa
	 */
	public boolean equals(Position pos) {
		return (this.x == pos.x && this.y == pos.y);
	}

	/**
	 * @ensures the x value is between 0 and Board.COLUMN_NUMBER
	 * 			the y value is between 0 and Board.ROW_NUMBER 
	 * @param p the position whose validity will be checked
	 * @return true if the position is valied; false vice versa
	 */
	public static boolean isValid(Position p) {
		return ((p.x >= 0) && (p.x < Board.COLUMN_NUMBER)
				&& ((p.y >= 0) && (p.y < Board.ROW_NUMBER)));
	}

	public String toString() {
		return "("+x+","+y+")";

	}
}
