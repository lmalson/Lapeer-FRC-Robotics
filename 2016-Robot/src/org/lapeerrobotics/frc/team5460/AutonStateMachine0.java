package org.lapeerrobotics.frc.team5460;

/**
 * 
 * Auton 0 - go under bar and shoot
 *
 */
public class AutonStateMachine0 extends AutonStateMachineBase implements AutonStateMachineInterface {

	private final static double DRIVE_RAMP_RATE_UP = 0.04;
	private final static double DRIVE_RAMP_RATE_DOWN = 0.06;
	
	private double drive = 0.0;
	
	private double drivePosReset = 0.0;
	
	@Override
	public void process() {
		
		int nextState = state;

		System.out.println("Heading "+robotHardware.getHeading());
		
		switch(state)
		{
		case 0: // 0 Reset Encoders
			robotHardware.getAnalogGyro().reset();
			// TODO - Capture gyro value
			controls.setIntakePistonLowered(true);
			controls.setLiftArm(-0.6); //-.4
			robotHardware.getDriveLeftMotorMaster().setPosition(0.0);
			robotHardware.getDriveRightMotorMaster().setPosition(0.0);
			controls.setManual(true);
			controls.setAutoHeading(false); 
			nextState = 1;
			break;			

		case 1: //set liftarm down
			if(stateCnt > 20){
				//controls.setLiftArm(0.0);
				nextState = 10;
			}
			break;
		case 10: // 40 Drive Fwd, ramp up power
			drive += DRIVE_RAMP_RATE_UP;
			controls.setDriveFwdRev(drive);
			
			if (drive > 0.7) { // .6 .4
				nextState = 20;
			}
			break;
		case 20: // Drive Fwd, Until Distance	
			drive = 0.7; // .8 .4
			controls.setDriveFwdRev(drive);
			controls.setLiftArm(-0.2);
			if (robotHardware.getRightDrivePosition() > 52.0) { // 50
				System.out.println("A0 st="+state+"  "+String.format("Lft=%4.2f Rgt=%4.2f",robotHardware.getLeftDrivePosition(),robotHardware.getRightDrivePosition()));
				nextState = 30;		
				}
//			if ((robotHardware.getDriveLeftMotorMaster().getPosition() > 400.0) || 
//					(robotHardware.getDriveRightMotorMaster().getPosition() > 400.0) || (stateCnt > 500))
//				nextState = 30;
			break;
		case 30: // 60 ramp down	
			drive -= DRIVE_RAMP_RATE_DOWN;
			controls.setDriveFwdRev(drive);
			controls.setLiftArm(0.0);
			if (drive < 0.0) {
				controls.setDriveFwdRev(0.0);				
				nextState = 100;
			}
			break;
		case 40: // 70 rotate right
			//controls.setDriveHeadingTarget(30.0); //22.5 45
			//double absHeadingError = Math.abs(30.0 - robotHardware.getHeading());
			//if ((absHeadingError < 5.0) || (stateCnt > 100)) {
			//	nextState = 50;
			//}
			controls.setDriveRotation(0.4);
			if(stateCnt > 20){
				nextState = 50;
			}
			break;
		case 50:
			if (stateCnt > 10) {
				controls.setDriveFwdRev(0.3);
				drivePosReset = robotHardware.getRightDrivePosition();
//				robotHardware.getDriveRightMotorMaster().setPosition(0);				
				nextState = 60;
			}						
			break;
		case 60:
			if (robotHardware.getRightDrivePosition() > (drivePosReset+6.0)) { // 5
				controls.setDriveFwdRev(0.0);
				nextState = 70;
			}
			break;
			
			
			
			
/*			
		case 50: // auto target mode
			controls.setAutoTarget(true);
			
			if (this.autoTargetController.getTargetDetected()) {
			nextState = 60;
			} 
			else if (stateCnt > 250) {
				controls.setAutoTarget(false);
				nextState = 60;
			}
			break;
		case 60: // fire on small error
			if (autoTargetController.inTarget()) {
				nextState = 100;
			}
			else if (stateCnt > 200) {
				controls.setAutoTarget(false);
				nextState = 70;				
			}
			break;
*/
		
		
		case 70: // fire
			if (stateCnt > 10) {
				controls.setShooterFireLong(true);
				nextState = 80;				
			}
			
			break;
		case 80: // wait
			controls.setShooterFireLong(false);
			if (stateCnt > 100) {
				System.out.println("Auton 0 Done...");
				nextState = 100;
			}
			break;
		case 90: // pickup mode
			//controls.setAutoPickup(true);
			nextState = 100;
			break;
		case 100: // end
			break;
 		}	
		
		if (nextState != state){
			state = nextState;
			stateCnt = 0;
		}
		else {
			stateCnt++;
		}

	}

}
