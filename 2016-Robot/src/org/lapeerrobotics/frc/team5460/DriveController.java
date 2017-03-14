
package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveController {
		
	private RobotHardware robotHardware;
//	private DriveControls driveControls;
	private ControlsInterface controls;
	private HeadingController headingController;
//	private AutoDriveController autoDriveController;
	private AutoTargetController autoTargetController;		
	private double autoHeadingTarget = 0.0;
	private double autoDriveFwdRevTarget = 0.0;
	private boolean isCompetitionRobot; 
	
	public DriveController() {
		
	}
	
	public void process() {

		boolean manualSteering = true;
		
		if(this.controls.isAutoTarget()) {
			manualSteering = false;		

			double headingAdj = autoTargetController.getHeadingAdjust();
			autoHeadingTarget = this.robotHardware.getHeading() + headingAdj;
			autoHeadingTarget += this.controls.getDriveHeadingAdjust();
			if (autoHeadingTarget > 180.0)
				autoHeadingTarget -= 360.0;
			if (autoHeadingTarget < -180.0)
				autoHeadingTarget += 360.0;					
		
			
			double driveAdj = autoTargetController.getDriveAdjust();
			autoDriveFwdRevTarget = this.robotHardware.getDriveLeftMotorEncoder() + driveAdj;
					
			
			headingController.setAutoHeadingTarget(autoHeadingTarget);		
//			autoDriveController.setAutoDriveTarget(autoDriveFwdRevTarget);
		}
		else if(this.controls.isAutoPickup()) {
			headingController.reset();	
//			autoDriveController.reset();
		} 
/*		else if (this.controls.isTurtleMode()) {
			headingController.reset();
		}*/		
		else { //manual
			if(this.controls.isAutoHeading()) {
				autoHeadingTarget = this.controls.getDriveHeadingTarget();
				manualSteering = false;
			}
			else {
				headingController.reset();				
//				autoDriveController.reset();
			}			
		}

		if (manualSteering) {
			this.robotHardware.getDriveRightMotorMaster().set( this.controls.getDriveFwdRev() + this.controls.getDriveRotation());
			this.robotHardware.getDriveLeftMotorMaster().set( -this.controls.getDriveFwdRev() + this.controls.getDriveRotation());						
//			this.robotHardware.getDriveRightMotorMaster().set( -this.controls.getDriveFwdRev() + this.controls.getDriveRotation());
//			this.robotHardware.getDriveLeftMotorMaster().set( this.controls.getDriveFwdRev() + this.controls.getDriveRotation());						
		} 
		else {
			headingController.setAutoHeadingTarget(autoHeadingTarget);
//			autoDriveController.setAutoDriveTarget(autoDriveFwdRevTarget);
			double rotatePower = headingController.getRotatePower();
//			double drivePower = autoDriveController.getDrivePower();
//			this.robotHardware.getDriveRightMotorMaster().set( -drivePower + rotatePower); //-this.controls.getDriveFwdRev()
//			this.robotHardware.getDriveLeftMotorMaster().set( drivePower + rotatePower); // this.controls.getDriveFwdRev()				
			this.robotHardware.getDriveRightMotorMaster().set( this.controls.getDriveFwdRev() + rotatePower); //-this.controls.getDriveFwdRev()
			this.robotHardware.getDriveLeftMotorMaster().set(  -this.controls.getDriveFwdRev() + rotatePower); // this.controls.getDriveFwdRev()				
		}
		
		if(!this.controls.isHighGear()) {
			if(isCompetitionRobot)
				this.robotHardware.getGearShiftingSolenoid().setReverse();
			else
				this.robotHardware.getGearShiftingSolenoid().setForward();
	
		}
		else {
			if(isCompetitionRobot)
				this.robotHardware.getGearShiftingSolenoid().setForward();
			else
				this.robotHardware.getGearShiftingSolenoid().setReverse();

		}

/*		if(this.controls.isLiftArmUp()) {
			this.robotHardware.getLiftArms().set(4.0);
		}
		else if(this.controls.isLiftArmDown()) {
			this.robotHardware.getLiftArms().set(-4.0);
		}
		else {
			this.robotHardware.getLiftArms().set(0.0);
		}*/
		
		this.robotHardware.getLiftArms().set(0.6 * this.controls.getLiftArm()); //.4
		
	}
	
	public double getAutoHeadingTarget() {
		return this.autoHeadingTarget;
	}
	
	public void setAutoTargetController(AutoTargetController a) {
		this.autoTargetController = a;
	}

	public void setRobotHardware(RobotHardware r) {
		this.robotHardware = r;
	}
	
	public void setControls(ControlsInterface c) {
		this.controls = c;
	}

//	public void setDriveControls(DriveControls d) {
//		this.driveControls = d;
//	}
	
	public void setHeadingController(HeadingController h) {
		this.headingController = h;
	}
	
	public void setAutoDriveController(AutoDriveController d) {
//		this.autoDriveController = d;
	}

	public void setCompetitionRobot(boolean isCompetitionRobot) {
		this.isCompetitionRobot = isCompetitionRobot;
	}
}
