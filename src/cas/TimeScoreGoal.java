package cas;

import engines.GameEngine;

public class TimeScoreGoal extends ScoreGoal {
	private long timeGoal;

	public TimeScoreGoal(double scoreGoal, long timeGoal) {
		super(scoreGoal);
		setTimeGoal(timeGoal);
	}

	public boolean goalReached() {
		return (super.goalReached() && GameEngine.getInstance().getTimeLeft() !=0);
	}
	
	public long getTimeGoal(){
		return timeGoal;
	}
	
	public double getScoreGoal(){
		return getGoal();
	}
	
	private void setTimeGoal(long goal){
		timeGoal = goal;
	}
	
	public String toString() {
		return "s:"+(int)getScoreGoal()+" t:"+getTimeGoal();
	}
	
}
