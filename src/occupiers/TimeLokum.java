package occupiers;

public class TimeLokum extends RegularLokum {

	public static final long defaultTime = 5; // 5s for all
	// The time bonus which will be added to the remaining time when this Lokum is exploded
	private long bonusTime;

	/**
	 * Constructor
	 * Creates a regular Lokum whose type is time
	 * @modifies bonusTime
	 */
	public TimeLokum() {
		super("time");
		setBonusTime(defaultTime);
	}

	/**
	 * @return the time bonus of the Lokum
	 */
	public long getBonusTime() {
		return bonusTime;
	}

	/**
	 * @param new time bonus of the Lokum
	 * @modifies bonusTime
	 */
	public void setBonusTime(long bonusTime) {
		this.bonusTime = bonusTime;
	}

}
