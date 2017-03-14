package org.lapeerrobotics.frc.team5460;

public class WinchController {
	
	private RobotHardware robotHardware;
//	private DriveControls driveControls;
	private ControlsInterface controls;

	public WinchController() {		
	}
	
	public void setRobotHardware(RobotHardware r) {
		this.robotHardware = r;
	}
	
//	public void setDriveControls(DriveControls d) {
//		this.driveControls = d;
//	}
	
	public void setControls(ControlsInterface c) {
		this.controls = c;
	}	
	
	public void process() {
       	if(controls.isWinchPull()) {
       		robotHardware.getWinchMotor().set(12.0);
       	}
       	else if(controls.isWinchRetract()) {
       		robotHardware.getWinchMotor().set(-12.0);
       	}
       	else {
       		robotHardware.getWinchMotor().set(0.0);
       	}
       	
       	
       	if (controls.isScissorRelease()) {
       		robotHardware.getScissorLiftReleaseSolenoid().setReverse();
       	} 
       	else {
       		robotHardware.getScissorLiftReleaseSolenoid().setForward();       		
       	}       	
	}
		

}
