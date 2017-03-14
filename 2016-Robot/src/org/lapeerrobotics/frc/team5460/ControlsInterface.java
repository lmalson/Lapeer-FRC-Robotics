package org.lapeerrobotics.frc.team5460;

public interface ControlsInterface {

	public double getDriveFwdRev();
	
	public double getDriveRotation();
	
	public double getDriveHeadingTarget();
	
	public double getDriveHeadingAdjust();

	public boolean isAutoHeading();
	
	public boolean isAutoAngle();

	public boolean isManual();

//	public boolean isTurtleMode();
	
	public boolean isAutoTarget();
	
	public boolean isAutoPickup();
		
	public boolean isHighGear();
		
//	public double getArmAngleAdjust();
	
//	public double getShooterAngleAdjust();
	
	public boolean isIntakeMotor();
	
	public boolean isIntakePistonLowered();
	
	public boolean isShooterEject();
	
	public boolean isShooterReload();
	
	public boolean isShooterFireLong();
	
	public boolean isShooterFireShort();
	
	public boolean isWinchPull();
	
	public boolean isWinchRetract();
	
	public boolean isScissorRelease();
	
//	public boolean isLiftArmDown();
	
//	public boolean isLiftArmUp();
	
	public double getLiftArm();
}
