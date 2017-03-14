package org.lapeerrobotics.frc.team5460;

public class AutoDriveController {
	public final static double DRIVE_KP = 0.035; // 0.025
	private static final double DRIVE_KD = 0.0;
	private static final double MAX_DRIVE_PWR = 4.0;
	private static final double MIN_DRIVE_PWR = -4.0;
	
	private double driveError = 0.0;
	private double prevDriveError = 0.0;
	private double deltaDriveError = 0.0;

	private double autoDriveTarget = 0.0;
	
	private double drivePower = 0.0;
	
	private RobotHardware robotHardware;
	
	public AutoDriveController() {
		robotHardware.getDriveLeftMotorMaster().setPosition(0.0);
		robotHardware.getDriveRightMotorMaster().setPosition(0.0);
		
	}
	
	public double getAutoDriveTarget() {
		return this.autoDriveTarget;
	}
	
	public void process() {
		
		prevDriveError = driveError;
		driveError = autoDriveTarget - this.robotHardware.getDriveLeftMotorEncoder();

		deltaDriveError = driveError - prevDriveError;

		drivePower = DRIVE_KP * driveError + DRIVE_KD * deltaDriveError;
		if (drivePower > MAX_DRIVE_PWR)
			drivePower = MAX_DRIVE_PWR;
		else if (drivePower < MIN_DRIVE_PWR)
			drivePower = MIN_DRIVE_PWR;
		
//		System.out.println("heading process " + rotatePower);
	}
	
	public void setAutoDriveTarget(double t) {
		this.autoDriveTarget = t;
	}
	
	public double getDrivePower() {
		return this.drivePower;
	}
	
	public void setRobotHardware(RobotHardware robotHardware) {
		this.robotHardware = robotHardware;
	}
	
	public void reset() {
		this.driveError = 0;
		this.autoDriveTarget = 0.0;
	}
	
}
