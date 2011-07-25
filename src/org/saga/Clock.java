package org.saga;


public class Clock implements Runnable{

	/**
	 * Plugin.
	 */
	private final Saga plugin= Saga.plugin();
	
	/**
	 * Tick.
	 */
	private int tick=0;
	
	public Clock() {

	}
	
	@Override
	public void run() {
		
		plugin.clockTickEvent(tick);
		tick++;
		if(tick>=59){
			tick=0;
		}
	}

}
