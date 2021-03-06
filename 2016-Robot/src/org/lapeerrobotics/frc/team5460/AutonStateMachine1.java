package org.lapeerrobotics.frc.team5460;

/**
 * 
 * Auton 1 - cross defnence 
 *
 */
public class AutonStateMachine1 extends AutonStateMachineBase implements AutonStateMachineInterface {

	private final static double DRIVE_RAMP_RATE_UP = 0.04; //.02
	private final static double DRIVE_RAMP_RATE_DOWN = 0.06; //.04
	
	private double drive = 0.0;

	@Override
	public void process() {
		
		int nextState = state;

		switch(state)
		{
		case 0: // 0 Reset Encoders0
			robotHardware.getAnalogGyro().reset();
			robotHardware.getDriveLeftMotorMaster().setPosition(0.0);
			robotHardware.getDriveRightMotorMaster().setPosition(0.0);
			controls.setIntakePistonLowered(true);
			controls.setManual(true);
			controls.setAutoHeading(false);
			nextState = 10;
			break;			

			case 10: // 40 Drive Fwd, ramp up power
			drive += DRIVE_RAMP_RATE_UP;
			controls.setDriveFwdRev(drive);
			if (drive > 0.8) { //.4
				nextState = 20;
			}
			break;
		case 20: // Drive Fwd, Until Distance	
			drive = 0.8; //.4
			controls.setDriveFwdRev(drive);
			if (stateCnt > 210) //120
				nextState = 30;
//			if ((robotHardware.getDriveLeftMotorMaster().getPosition() > 400.0) || 
//					(robotHardware.getDriveRightMotorMaster().getPosition() > 400.0) || (stateCnt > 500))
//				nextState = 30;
			break;
		case 30: // 60 ramp down	
			drive -= DRIVE_RAMP_RATE_DOWN;
			controls.setDriveFwdRev(drive);
			
			if (drive < 0.0) {
				controls.setDriveFwdRev(0.0);				
				nextState = 40;
				System.out.println("Auton 1 Done...");
			}
			break;
		case 40: // 60 ramp down	

			
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
