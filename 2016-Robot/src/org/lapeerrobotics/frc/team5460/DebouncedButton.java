package org.lapeerrobotics.frc.team5460;

public class DebouncedButton {

	private boolean toggle = false;
	private boolean state = false;
	private int bounceCount = 0;
	
	public DebouncedButton(boolean toggle) {
		this.toggle = toggle;
	}
	
	public void processButton(boolean v) {

		if(v){ 
			bounceCount++;
			if(bounceCount == 3) {	
				if (toggle) { 
					if(state){
						state = false;
					}
					else {
						state = true;
					}					
				} else
					state = true;
			}
		} else {
			bounceCount = 0;
			if (!toggle)
				state = false;
		}
				
	}
	
	public boolean getState() {
		return this.state;
	}

	public void setState(boolean b) {
		this.state = b;		
	}
}
