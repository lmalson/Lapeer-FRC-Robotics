package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public @interface ShooterStateMachineReference {
/*
	
	//TODO delete this class after done using it as a reference
	
	
	
	
	public final static int INIT_STATE = 0;
	public final static int UNLOADED_STATE = 10;
	public final static int LOADING_STATE = 20;
	public final static int PRE_LOADED_STATE = 21;
	public final static int LOADED_STATE = 30;
	public final static int EJECT_STATE = 31;
	public final static int PRE_SHOOTING_STATE = 40;
	public final static int SHOOTING_STATE = 50;
	public final static int POST_SHOOTING_STATE = 60;
	public final static String stateNames[] = {"INIT","UNLOADED","LOADING","LOADED","PRE_SHOOTING","SHOOTING","POST_SHOOTING"};
	private int state = INIT_STATE;
	private int stateCnt;
	private boolean startLoading = false;
	private boolean fire = false;
	private boolean eject = false;
	
	public int startLoadingCnt = 0;
	public int fireCnt = 0;
	private RobotHardware robotHardware;
	private boolean isCompetitionRobot;
	
	private ControlsInterface controls;
	
	public ShooterStateMachine() 
	{		
	}
	
	public void setControls(ControlsInterface c) {
		this.controls = c;
	}
	
	public int getState() {
		return this.state;
	}
	
	public String getStateName() {
		return stateNames[(int)(this.state/10)];
	}
	
//	public void startLoading(boolean l)
//	{
//		startLoading = l;
//	}
//	public void eject(boolean e)
//	{
//		eject = e;
//	}
//	public void fire(boolean f)
//	{
//		fire = f;
//	}


	public boolean getStartLoading() {
		return this.startLoading;
	}
	
	public boolean getFire()
	{
		return this.fire;
	}
		
	public void reset() {
		this.state = INIT_STATE;
	}

	public void process() //actual state machine part
	{
		this.startLoading = controls.isShooterIntake();
		this.fire = controls.isShooterFire();
		this.eject = controls.isShooterEject();		
		
		int nextState = state;
		
		if (this.eject) {
			this.eject = false;
			this.robotHardware.pushShooterPistonOut();
			if (this.isCompetitionRobot)
				this.robotHardware.getShooterMotorMaster().set(9.0); 
			else
				this.robotHardware.getShooterMotorMaster().set(-9.0);
			nextState = this.EJECT_STATE;
		}
		if (this.fire) {
			this.fire = false;
			if (this.isCompetitionRobot)
				this.robotHardware.getShooterMotorMaster().set(11.0);  
			else
				this.robotHardware.getShooterMotorMaster().set(-11.0);  				
			nextState = this.PRE_SHOOTING_STATE;				
		}	

		switch(state)
		{
		case INIT_STATE: // 0
			//reset encoders
			this.robotHardware.getShooterMotorMaster().set(0.0);
			if (stateCnt > 100) {
				nextState = this.UNLOADED_STATE;				
			}
			break;			
		case UNLOADED_STATE: // 10
			if(startLoading && stateCnt > 45) // 90
			{
				nextState = this.LOADING_STATE;
//				startLoading = false;
			}
			break;
		case LOADING_STATE: //20
			if (this.isCompetitionRobot)
				this.robotHardware.getShooterMotorMaster().set(-3.0); 
			else
				this.robotHardware.getShooterMotorMaster().set(3.0); 
				
			if(startLoading && stateCnt > 40 ) // 100
			{
				nextState = this.UNLOADED_STATE;
				this.robotHardware.getShooterMotorMaster().set(0.0);
//				startLoading = false;
			}

//			if(this.robotHardware.isBallLoaded())
//			{
//				this.robotHardware.getShooterMotorMaster().set(0.0);
//				nextState = this.PRE_LOADED_STATE;
//			}
			break;
		case PRE_LOADED_STATE:
			if (stateCnt > 20) {
//				this.fire = false;
				nextState = LOADED_STATE;				
			}
			break;
		case LOADED_STATE:
			if (this.fire) {
				this.fire = false;

				if (this.isCompetitionRobot)
					this.robotHardware.getShooterMotorMaster().set(11.0); 
				else
					this.robotHardware.getShooterMotorMaster().set(-11.0); 
					
				nextState = this.PRE_SHOOTING_STATE;				
			}	
			
			break;
		case EJECT_STATE:
			if(stateCnt > 50)
			{
				this.robotHardware.pullShooterPistonIn();
				this.robotHardware.getShooterMotorMaster().set(0.0);
				nextState = this.UNLOADED_STATE;
			}
			break;
		case PRE_SHOOTING_STATE:
			if(stateCnt > 50)
			{
				nextState = this.SHOOTING_STATE;
			}
			break;
		case SHOOTING_STATE:
			this.robotHardware.pushShooterPistonOut();
			if(stateCnt > 25) // 30
			{
				nextState = this.POST_SHOOTING_STATE;
			}
			break;
		case POST_SHOOTING_STATE:
			this.robotHardware.pullShooterPistonIn();
			if(stateCnt > 20)
			{
				this.robotHardware.getShooterMotorMaster().set(0.0);
				nextState = this.INIT_STATE;
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

	public void setCompetitionRobot(boolean isCompetitionRobot) {
		this.isCompetitionRobot = isCompetitionRobot;	
	}*/
	
}

