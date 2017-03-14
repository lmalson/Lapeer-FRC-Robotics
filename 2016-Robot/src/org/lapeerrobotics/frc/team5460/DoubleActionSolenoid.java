package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DoubleActionSolenoid extends DoubleSolenoid {

	private int fwdCounter = 0;
	private int revCounter = 0;
	
	public DoubleActionSolenoid(final int forwardChannel, final int reverseChannel) {
		super(forwardChannel,reverseChannel);		
	}


	public void process() {
}

	public void setForward() {
		if (fwdCounter < 150) {
			super.set(DoubleSolenoid.Value.kForward);
			fwdCounter++;
			revCounter = 0;
		} else {
			super.set(DoubleSolenoid.Value.kOff);			
		}
	}
	
	public void setReverse() {
		if (revCounter < 150) {
			super.set(DoubleSolenoid.Value.kReverse);
			revCounter++;
			fwdCounter = 0;
		} else {
			super.set(DoubleSolenoid.Value.kOff);						
		}
	}
	
}
