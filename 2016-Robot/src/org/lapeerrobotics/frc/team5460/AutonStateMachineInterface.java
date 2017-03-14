package org.lapeerrobotics.frc.team5460;

public interface AutonStateMachineInterface {

	public void process();
	
	public void setControls(ControlsInterface c);

	public void setRobotHardware(RobotHardware robotHardware);

	public void setAutoTargetController(AutoTargetController autoTargetController);
	
	public void setCatapultAndIntakeController(CatapultAndIntakeController catapultAndIntakeController);

//	public void setArmController(ArmController armController);
}
