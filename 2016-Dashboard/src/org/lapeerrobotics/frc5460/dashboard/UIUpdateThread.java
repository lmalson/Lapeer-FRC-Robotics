package org.lapeerrobotics.frc5460.dashboard;

import java.awt.Frame;

public class UIUpdateThread {

	private Frame frame;
	private boolean isRunning = false;
	
	public void setIsRunning(boolean i) {
		this.isRunning = i;
	}
	
	public void run() {
		while(isRunning) {
			if (frame.getGraphics() != null) {
				frame.repaint();
			}
			 try {
	                Thread.sleep(80);
	            } catch (InterruptedException e){
	            }
		}
	}

	public void setFrame(Frame f) {
		this.frame = f;		
	}

	
}
