package cas;

import engines.GameEngine;

public class ScoreGoal extends Goal {
	private double scoreGoal;

	/**
	 * Constructor
	 * @param goal the score which must be reached to win the game
	 */
	public ScoreGoal(double goal){
		super();
		setScoreGoal(goal);
	}

	/**
	 * @return true if current score is equal to or greater than the goal score
	 * 		   false vice versa
	 */
	@Override
	public boolean goalReached() {
		return GameEngine.getInstance().getScore() >= getScoreGoal();
	}

	/**
	 * @return the goal score
	 */
	public double getScoreGoal(){
		return scoreGoal;
	}

	/**
	 * @param goal the new score  which must be reached to win the game
	 */
	private void setScoreGoal(double goal){
		scoreGoal = goal;
	}

	/**
	 * @return the goal score as string
	 */
	public String toString() {
		return (int)getScoreGoal()+"";
	}
}
