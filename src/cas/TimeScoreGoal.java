package cas;

import engines.GameEngine;

public class TimeScoreGoal extends ScoreGoal {
	private long timeGoal;

	/**
	 * Constructor
	 * @param scoreGoal the score goal which must be reached to win the game
	 * @param timeGoal the initial remaining time
	 */
	public TimeScoreGoal(double scoreGoal, long timeGoal) {
		super(scoreGoal);
		setTimeGoal(timeGoal);
	}

	/**
	 * @return true if current score is equal to or greater than the goal score
	 * 		   and the remaining time is not 0 yet
	 * 		   false vice versa
	 */
	public boolean goalReached() {
		return (super.goalReached() && GameEngine.getInstance().getTimeLeft() !=0);
	}

	/**
	 * @return the initial remaining time
	 */
	public long getTimeGoal(){
		return timeGoal;
	}	

	/**
	 * @param goal the new initial remaining time
	 */
	private void setTimeGoal(long goal){
		timeGoal = goal;
	}

}
