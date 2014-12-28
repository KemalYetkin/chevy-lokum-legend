package cas;

import engines.GameEngine;

public class ScoreGoal extends Goal {
	private double scoreGoal;
	
	public ScoreGoal(double goal){
		super();
		setGoal(goal);
	}
	
	@Override
	public boolean goalReached() {
		// TODO Auto-generated method stub
		return GameEngine.getInstance().getScore() >= getGoal();
	}
	
	public double getGoal(){
		return scoreGoal;
	}
	
	private void setGoal(double goal){
		scoreGoal = goal;
	}
	
	public String toString() {
		return (int)getGoal()+"";
	}
	
	

}
