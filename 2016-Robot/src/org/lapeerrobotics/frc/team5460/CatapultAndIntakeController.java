package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.Timer;

public class CatapultAndIntakeController {
	private boolean isCompetitionRobot;
	
	
	private RobotHardware robotHardware;
	private ControlsInterface controls;



	public void setCompetitionRobot(boolean isCompetitionRobot) {
		this.isCompetitionRobot = isCompetitionRobot;	
	}


	private int state;
	private int stateCnt;
	
	public CatapultAndIntakeController() {	
	}
	
	
	public void process() {

		if(controls.isShooterEject()){
			robotHardware.getIntakeMotor().set(-12.0); //-4
		} else if(controls.isIntakeMotor()) {
			robotHardware.getIntakeMotor().set(12.0);//8
		} else {
			robotHardware.getIntakeMotor().set(0.0);
		}
	
		if(controls.isIntakePistonLowered()) {
			robotHardware.getIntakeSolenoid().setForward();
		} else {
			robotHardware.getIntakeSolenoid().setReverse();
		}
		
		
		
	int nextState = state;	
		switch(state)
		{
			case 0://catapult down
				robotHardware.getShooterSolenoid().set(false);
//				robotHardware.catapultSolenoidReload();

				if (stateCnt > 100) {
					if(controls.isShooterFireLong()){
						nextState = 1;
					}
					else if(controls.isShooterFireShort()){
						nextState = 2;
					}					
				}
				break;
			case 1://long fire
				robotHardware.getShooterSolenoid().set(true);
//				robotHardware.catapultSolenoidFire();
				if(stateCnt > 20)
				{
					nextState = 0;
				}
				break;
			case 2://short fire
				robotHardware.getShooterSolenoid().set(true);
//				robotHardware.catapultSolenoidFire();
				if(stateCnt > 6)
				{
					nextState = 0;
				}
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
	
	
	
	public void setRobotHardware(RobotHardware r) {
		this.robotHardware = r;		
	}

	public void setControls(ControlsInterface c) {
		this.controls = c;
	}


	public void reset() {
		// TODO make this do something
	}
}
