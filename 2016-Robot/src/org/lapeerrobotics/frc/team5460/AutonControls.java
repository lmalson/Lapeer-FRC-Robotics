package org.lapeerrobotics.frc.team5460;

public class AutonControls implements ControlsInterface {

//	public static double JOYSTICK_ROTATE_GAIN = 0.75; 
//	public static double JOYSTICK_HEADING_GAIN = 1.2; // 1.5 
//	public static double JOYSTICK_ARM_ANGLE_GAIN = 0.5;
//	public static double JOYSTICK_SHOOTER_ANGLE_GAIN = 0.75; // 0.5
	
	private boolean isManual = false;
	private boolean isAutoTarget = false;
	private boolean isAutoPickup = false;
//	private boolean isTurtleMode = false;
	private boolean isHighGear = false;
	private boolean isAutoHeading = false;
	private double headingTarget = 0.0;
	private double driveHeadingAdjust = 0.0;
//	private double armAngleAdjust = 0.0;
//	private double shooterAngleAdjust = 0.0;
	
	private Boolean isShooterReload = false;
	private boolean isShooterEject = false;
	private boolean isShooterFireLong = false;
	private boolean isShooterFireShort = false;
	private boolean isShooterIntake = false;
	private boolean isWinchPull = false;
	private boolean isWinchRetract = false;
	private boolean isScissorRelease = false;
	private boolean isAutoAngle = false;
	private boolean isIntakePistonLowered = false;
	
	private double driveFwdRev = 0.0;
	private double driveRotation = 0.0;
	private double liftArm = 0.0;
	
	public AutonControls() {
		
	}
		
	@Override
	public double getDriveFwdRev() {
		return driveFwdRev;
	}

	public void setDriveFwdRev(double v) {
		this.driveFwdRev = v;
	}
	
	@Override
	public double getDriveRotation() {
		return driveRotation;
	}

	public void setDriveRotation(double v) {
		this.driveRotation = v;
	}
	
	@Override
	public double getDriveHeadingTarget() {
		return this.headingTarget;
	}

	public void setDriveHeadingTarget(double v) {
		this.headingTarget = v;
	}
	
	@Override
	public double getDriveHeadingAdjust() {
		return this.driveHeadingAdjust;
	}

	public void setDriveHeadingAdjust(double v) {
		this.driveHeadingAdjust = v;
	}
	
	@Override
	public boolean isAutoHeading() {
		return this.isAutoHeading;
	}

	public void setAutoHeading(Boolean b) {
		this.isAutoHeading = b;
	}
	
	@Override
	public boolean isAutoAngle() {
		return this.isAutoAngle;
	}

	public void setAutoAngle(Boolean b) {
		this.isAutoAngle = b;
	}

	@Override
	public boolean isManual() {
		return this.isManual;
	}

	public void setManual(boolean b) {
		this.isManual = b;
	}	
	
	@Override
	public boolean isAutoTarget() {
		return this.isAutoTarget;
	}

	public void setAutoTarget(Boolean b) {
		this.isAutoTarget = b;
	}

	@Override
	public boolean isAutoPickup() {
		return this.isAutoPickup;
	}
	
	public void setAutoPickup(Boolean b) {
		this.isAutoPickup = b;
	}
	
	@Override
	public boolean isHighGear() {
		return this.isHighGear;
	}

	public void setHighGear(Boolean b) {
		this.isHighGear = b;
	}
	
	@Override
	public boolean isIntakeMotor() {
		return this.isShooterIntake;
	}

	public void setShooterIntake(Boolean b) {
		this.isShooterIntake = b;
	}
	
	@Override
	public boolean isShooterEject() {
		return this.isShooterEject;
	}

	public void setShooterEject(Boolean b) {
		this.isShooterEject = b;
	}

	@Override
	public boolean isShooterFireLong() {
		return this.isShooterFireLong;
	}

	public void setShooterFireLong(Boolean b) {
		this.isShooterFireLong = b;
	}
	
	@Override
	public boolean isShooterFireShort() {
		return this.isShooterFireShort;
	}

	public void setShooterFireShort(Boolean b) {
		this.isShooterFireShort = b;
	}
	
	@Override
	public boolean isShooterReload() {
		return this.isShooterReload;
	}
	
	public void setShooterReload(Boolean b) {
		this.isShooterReload = b;
	}
	
	@Override
	public boolean isWinchPull() {
		return this.isWinchPull;
	}

	@Override
	public boolean isWinchRetract() {
		return this.isWinchRetract;
	}

	@Override
	public boolean isScissorRelease() {
		return this.isScissorRelease;
	}

	@Override
	public boolean isIntakePistonLowered() {
		return this.isIntakePistonLowered;
	}
	public void setIntakePistonLowered(boolean b){
		this.isIntakePistonLowered = b;
	}

	@Override
	public double getLiftArm() {
		return this.liftArm;
	}
	
	public void setLiftArm(double v){
		this.liftArm = v;
	}


}
