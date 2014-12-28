package cas;

public class Position {
	private int x;
	private int y;

	public Position(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	private void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	private void setY(int y) {
		this.y = y;
	}
	
	public boolean equals(Position pos) {
		return (this.x == pos.x && this.y == pos.y);
	}
	
	public static boolean isValid(Position p) {
		return ((p.x >= 0) && (p.x < Board.COLUMN_NUMBER)
				&& ((p.y >= 0) && (p.y < Board.ROW_NUMBER)));
	}
}
