package org.saga;

import java.util.ArrayList;


public class Clock implements Runnable{

	
	/**
	 * Instance.
	 */
	private static Clock instance; 
	
	/**
	 * Gets the clock.
	 * 
	 * @return clock.
	 */
	public static Clock getClock() {
		return instance;
	}
	
	/**
	 * Instances that will receive a tick every second.
	 */
	private ArrayList<Ticker> eachSecond = new ArrayList<Clock.Ticker>();

	
	public Clock() {

	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		for (Ticker ticker : eachSecond) {
			ticker.clockTick();
		}

	}


	/**
	 * Registers a ticker.
	 * 
	 * @param ticker ticker
	 */
	public void registerEachSecondTick(Ticker ticker) {

		eachSecond.add(ticker);
		
	}
	
	
	/**
	 * Loads the clock.
	 * 
	 */
	public static void load() {

		Clock clock = new Clock();
		Saga.plugin().getServer().getScheduler().scheduleAsyncRepeatingTask(Saga.plugin(),clock , 200L, 20L);
		instance = clock;
		
	}
	
	/**
	 * Unloads the clock.
	 * 
	 */
	public static void unload() {

		instance.eachSecond = null;
		instance = null;
		
	}
	
	public static interface Ticker{
		
		
		/**
		 * A clock tick.
		 * 
		 */
		void clockTick();
		
		
	}
	
}
