package org.lapeerrobotics.frc.team5460;

public class AutonStateMachineBase implements AutonStateMachineInterface {

	protected AutonControls controls;
	protected RobotHardware robotHardware;
	protected AutoTargetController autoTargetController;
//	protected ArmController armController;
	protected CatapultAndIntakeController catapultAndIntakeController;
	
	protected int state;
	protected int stateCnt;
	
	
	@Override
	public void process() {				
	}

	@Override
	public void setControls(ControlsInterface c) {
		this.controls = (AutonControls)c;
	}

	@Override
	public void setRobotHardware(RobotHardware robotHardware) {
		this.robotHardware = robotHardware;		
	}

	@Override
	public void setAutoTargetController(AutoTargetController autoTargetController) {
		this.autoTargetController = autoTargetController;
	}
	
/*	public void setArmController(ArmController armController) {
		this.armController = armController;
	}*/
	
	public void setCatapultAndIntakeController(CatapultAndIntakeController catapultAndIntakeController) {
		this.catapultAndIntakeController = catapultAndIntakeController;
	}


}
