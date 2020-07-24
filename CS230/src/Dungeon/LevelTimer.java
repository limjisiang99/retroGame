package Dungeon;

import java.lang.Math;
import java.text.DecimalFormat;
/**
 * This is the class in charge of managing the game timer.
 * @author 983146,975359
 * @version 1.0
 * @since 08-12-2019
 */
public class LevelTimer {
	
	private static long startTime;// The time that the timer was started at
	private static double savedTime;// Any extra amounts of time that need to be added
	private static DecimalFormat df = new DecimalFormat("#.###");// The format that the time is output in
	/**
	 * Starts or resets the timer.
	 */
	public static void startTimer() {
		startTime = getCurrentTimer();
	}
	
	/**
	 * Gets the current time of the system in milliseconds
	 * @return the current time of the system in milliseconds
	 */
	private static long getCurrentTimer() {
		 return System.currentTimeMillis();
	}
	
	/**
	 * Calulates the current time elapsed in a level in seconds.
	 * @return the current time elapsed on this level in seconds.
	 */
	public static double getCurrentTime() {	
		//subtracts the start time from the current time and then adds the saved time to get the
		//the current time with any additional time added on
		return Double.parseDouble(df.format(savedTime+ (Math.round((int) (  (getCurrentTimer() - startTime)))/1000.0)));
	}
	
	/**
	 * Adds a set number of seconds to the timer, useful when loading a save.
	 * @param addedTime The number of seconds to add to the timer.
	 */
	public static void addSavedTime(double addedTime) {
		savedTime += addedTime;
	}
}
