package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveControls implements ControlsInterface {
	
	private Joystick driverJoystick;
	private Joystick operatorJoystick;
	private RobotHardware robotHardware;
	private boolean isManual = true;
	private boolean isAutoTarget;
	private boolean isAutoPickup;
	private boolean isHighGear;
	private boolean isAutoHeading;
	private boolean isIntakePistonLower;
	private double headingTarget;
	public static double JOYSTICK_ROTATE_GAIN = 0.75; 
	public static double JOYSTICK_HEADING_GAIN = 1.2; // 1.5 
	
	private DebouncedButton debouncedDriveY;
	private DebouncedButton debouncedDriveX;
	private DebouncedButton debouncedDriveA;
	private DebouncedButton debouncedDriveB;
	private DebouncedButton debouncedDriveLB;
	private DebouncedButton debouncedDriveRB;
	private DebouncedButton debouncedOperatorY;
	private DebouncedButton debouncedOperatorX;
	private DebouncedButton debouncedOperatorA;
	private DebouncedButton debouncedOperatorB;
	private DebouncedButton debouncedOperatorLB;
	private DebouncedButton debouncedOperatorRB;
	private boolean isShooterEject;
	private boolean isShooterReload;
//	private boolean isShooterIntake;
	private boolean isIntakeMotor; 
	private boolean isWinchPull = false;
	private boolean isWinchRetract = false;
	private boolean isScissorRelease = false;
	private boolean isAutoAngle = false;
	private boolean isShooterFireLong = true;
	private boolean isShooterFireShort = false;
//	private boolean isLiftArmUp;
//	private boolean isLiftArmDown;
	private double liftArm;
	
	public DriveControls() {
		driverJoystick = new Joystick(0);
		operatorJoystick = new Joystick(1);
		
		debouncedDriveY = new DebouncedButton(false);
		debouncedDriveX = new DebouncedButton(false);
		debouncedDriveA = new DebouncedButton(false);
		debouncedDriveB = new DebouncedButton(false);
		debouncedDriveLB = new DebouncedButton(true);
		debouncedDriveRB = new DebouncedButton(true);
		debouncedOperatorY = new DebouncedButton(true);
		debouncedOperatorX = new DebouncedButton(false);
		debouncedOperatorA = new DebouncedButton(true);
		debouncedOperatorB = new DebouncedButton(false);
		debouncedOperatorLB = new DebouncedButton(true);
		debouncedOperatorRB = new DebouncedButton(true); //this toggles
	}
	
	public void reset() {
		isManual = true;
		isAutoTarget = false;
		isAutoPickup = false;
		isHighGear = false;
		isAutoHeading = false;
		headingTarget = 0.0;
		isWinchPull = false;
		isWinchRetract = false;
	}
	
	public void process() {
		
		debouncedDriveRB.processButton(driverJoystick.getRawButton(6));
		debouncedDriveLB.processButton(driverJoystick.getRawButton(5));
		debouncedDriveY.processButton(driverJoystick.getRawButton(4));
		debouncedDriveX.processButton(driverJoystick.getRawButton(3));
		debouncedDriveB.processButton(driverJoystick.getRawButton(2));
		debouncedDriveA.processButton(driverJoystick.getRawButton(1));
		debouncedOperatorRB.processButton(operatorJoystick.getRawButton(6));
		debouncedOperatorLB.processButton(operatorJoystick.getRawButton(5));
		debouncedOperatorY.processButton(operatorJoystick.getRawButton(4));
		debouncedOperatorX.processButton(operatorJoystick.getRawButton(3));
		debouncedOperatorB.processButton(operatorJoystick.getRawButton(2));
		debouncedOperatorA.processButton(operatorJoystick.getRawButton(1));		
				
		isHighGear = debouncedDriveRB.getState();
//		isAutoHeading = debouncedDriveLB.getState();
		isScissorRelease = debouncedDriveLB.getState();
		
		if (debouncedDriveX.getState()) { //manual
			isManual = true;
//			isTurtleMode = false;
			isAutoTarget = false;
			isAutoPickup = false;
		}
/*		else if (debouncedDriveY.getState()) { //turtlemode
			isManual = false;
			isTurtleMode = true;
			isAutoTarget = false;
			isAutoPickup = false;
		}*/
		else if (debouncedDriveA.getState()) { //autotarget //DriveB
			isManual = false;
			isAutoTarget = true;
			isAutoPickup = false;
		}
/*		else if (debouncedDriveA.getState()) { //autopickup
			isManual = false;
			isAutoTarget = false;
			isAutoPickup = true;
		} */
			    	    		
		isAutoAngle = debouncedOperatorY.getState();
		isShooterReload = false;
		isShooterEject = debouncedOperatorX.getState();
		isIntakeMotor = debouncedOperatorLB.getState();
		isIntakePistonLower = debouncedOperatorRB.getState();
	//	isLiftArmUp = debouncedDriveA.getState();
	//	isLiftArmDown = debouncedDriveB.getState();
		liftArm = -operatorJoystick.getRawAxis(5);
		
		
/*		if(operatorJoystick.getRawButton(5)){ lb
			isIntakeMotor = true;
		}
		else
			isIntakeMotor = false; */
		
		if(operatorJoystick.getRawAxis(3) > 0.75)
			isShooterFireLong = true;
		else
			isShooterFireLong = false;
		
		if(operatorJoystick.getRawAxis(2) > 0.75)
			isShooterFireShort = true;
		else 
			isShooterFireShort = false;
		
		
		if(isAutoHeading){
			headingTarget += getDriveHeadingAdjust();
			if(headingTarget > 180.0) {
				headingTarget -= 360.0;
			}
			else if(headingTarget < -180.0) {
				headingTarget += 360.0;
			}
		}
		else{
			headingTarget = robotHardware.getHeading();
		}

		if(driverJoystick.getRawAxis(2) >= 0.75) { //driverJoystick.getPOV() >= 0.0
			isWinchPull = true;
		}
		else{
			isWinchPull = false;
		}
		
		if(driverJoystick.getRawAxis(3) >= 0.75) { //driverJoystick.getPOV() == 180.0
			isWinchRetract = true;
		}
		else{
			isWinchRetract = false;
		}
				

	}
	
	public double getDriveFwdRev() {
		return -driverJoystick.getRawAxis(1);
	}
	
	public double getDriveRotation() {
		return JOYSTICK_ROTATE_GAIN * driverJoystick.getRawAxis(4);
	}
	
	public double getDriveHeadingTarget() {
		return this.headingTarget;
	}
	
	public double getDriveHeadingAdjust() {
		return JOYSTICK_HEADING_GAIN * deadband(driverJoystick.getRawAxis(4));
	}

	public boolean isAutoHeading() {
		return this.isAutoHeading;
	}
	
	public boolean isAutoAngle() {
		return this.isAutoAngle;
	}

	public boolean isManual() {
		return this.isManual;
	}

	public boolean isAutoTarget() {
		return this.isAutoTarget;
	}
	
	public boolean isAutoPickup() {
		return this.isAutoPickup;
	}
		
	public boolean isHighGear() {
		return this.isHighGear;
	}
	

	public double getLiftArm() {
		return this.liftArm;
	}
	
	public void setRobotHardware(RobotHardware robotHardware) {
		this.robotHardware = robotHardware;
	}
	
	public boolean isIntakeMotor() {
		return this.isIntakeMotor;
	}
	
	public boolean isShooterEject() {
		return this.isShooterEject;
	}
	
	public boolean isIntakePistonLowered() {
		return this.isIntakePistonLower;
	}
	
	public boolean isShooterFireLong() {
		return this.isShooterFireLong;
	}
	
	public boolean isShooterFireShort() {
		return this.isShooterFireShort;
	}
	
	public boolean isShooterReload() {
		return this.isShooterReload;
	}
	
	public boolean isWinchPull() {
		return this.isWinchPull;
	}
	
	public boolean isWinchRetract(){
		return this.isWinchRetract;
	}
	
	public boolean isScissorRelease(){
		return this.isScissorRelease;
	}

	private double deadband(double value) {
		if (value > -0.09 && value < 0.09) 
			return 0.0;
		else
			return value;
	}	
}
