package org.lapeerrobotics.frc.team5460;
//Auton 2 portculous

public class AutonStateMachine4 extends AutonStateMachineBase implements AutonStateMachineInterface {

	private final static double DRIVE_RAMP_RATE_UP = 0.04;
	private final static double DRIVE_RAMP_RATE_DOWN = 0.06;
	
	private double drive = 0.0;

	@Override
	public void process() {
		int nextState = state;

	/*	if (state < 40)
		System.out.println("A  state="+state+" cnt: "+stateCnt+" lPos: "+robotHardware.getDriveLeftMotorMaster().getPosition()+"rPos: "+robotHardware.getDriveRightMotorMaster().getPosition());
	*/	
		switch(state)
		{
		case 0: // 0 Reset Encoders0
			robotHardware.getAnalogGyro().reset();
			robotHardware.getDriveLeftMotorMaster().setPosition(0.0);
			robotHardware.getDriveRightMotorMaster().setPosition(0.0);
			controls.setIntakePistonLowered(true);
			controls.setLiftArm(-0.6);//-.4
			controls.setManual(true);
			controls.setAutoHeading(false);
			nextState = 1;
			break;		
		case 1: 
			if(stateCnt > 25){
				controls.setLiftArm(0.0);
				nextState = 10;
			}
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
			
			if (stateCnt > 100) //100
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
				controls.setDriveHeadingTarget(8.0);
			//.setAutoHeading(true);
				nextState = 40;
			}
			break;
		case 40: // 60 ramp down	
			if(stateCnt > 25){
				nextState = 50;
			}
			break;
		case 50:
			controls.setShooterFireLong(true);
			if(stateCnt > 100){
					System.out.println("Auton 0 Done...");
					nextState = 60;
			}
			nextState = 60;
			break;
		case 60:
			
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
