package handlers;

import cas.Position;

public class GenerateEvent {

	private Position position;
	private String color;
	private String detail;


	public GenerateEvent(Position pos, String color) {
		this.position = pos;
		this.color = color;	
	}

	// for stripe lokum direction
	public GenerateEvent(Position pos, String color, String detail) {
		this.position = pos;
		this.color = color;
		this.detail = detail;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}




}
