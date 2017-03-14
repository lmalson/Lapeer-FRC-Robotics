package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTargetController {

	private double headingAdjust;
	private double driveAdjust;
//	private double shooterAngleAdjust;
	private boolean targetDetected = false;
	private double errorX;
	private double errorY;
	private int lastImgCnt = 0;

	private RobotHardware robotHardware;

	public AutoTargetController() {
		
	}
	
	public void process() {
		
		targetDetected = false;
		int imgCnt = lastImgCnt;
		try {
			targetDetected = SmartDashboard.getBoolean("targetDetected");				
			imgCnt = (int)SmartDashboard.getNumber("tgtImgCnt");
			errorX = SmartDashboard.getNumber("errorX");	
			errorY = SmartDashboard.getNumber("errorY");	
		} catch(Exception ex) {}
		
		if (targetDetected && (imgCnt != lastImgCnt)) {
//			shooterAngleAdjust = -0.007 * errorY; // 0.005
			driveAdjust = 0.1 * errorY; 
			headingAdjust = -0.1 * errorX; // 0.1

			lastImgCnt = imgCnt;
		}
		else {
//			shooterAngleAdjust = 0.0;
			driveAdjust = 0.0;
			headingAdjust = 0.0;
		}
						
	}
	
	public double getHeadingAdjust() {
		return this.headingAdjust;
	}
	
	public double getDriveAdjust() {
		return this.driveAdjust;
	}
/*	public double getShooterAngleAdjust() {
		return this.shooterAngleAdjust;
	}*/
	
	public void setRobotHardware(RobotHardware r) {
		this.robotHardware = r;
	}
	
	public double getErrorX() {
		return errorX;
	}
	
	public double getErrorY() {
		return errorY;
	}
	
	public boolean getTargetDetected() {
		return this.targetDetected;
	}
	
	public boolean inTarget() {
		if ((Math.abs(errorX) < 40.0) && (Math.abs(errorY) < 40.0)) {
			return true;			
		}
		else {
			return false;
		}
	}
	
}

