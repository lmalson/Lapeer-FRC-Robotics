package org.lapeerrobotics.frc.team5460;

//import org.lapeerrobotics.frc.team5460.ArmController;
import org.lapeerrobotics.frc.team5460.AutoTargetController;
import org.lapeerrobotics.frc.team5460.DriveController;
import org.lapeerrobotics.frc.team5460.DriveControls;
import org.lapeerrobotics.frc.team5460.HeadingController;
import org.lapeerrobotics.frc.team5460.RobotHardware;
//import org.lapeerrobotics.frc.team5460.ShooterStateMachine;
import org.lapeerrobotics.frc.team5460.WinchController;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class RobotBase extends SampleRobot {

	public final static String COMPETITION_ROBOT_HOST = "roboRIO-5460"; //host names
	public final static String PRACTICE_ROBOT_HOST = "roboRIO-5461";
	private boolean isCompetitionRobot = true;
	
	private DriveControls driveControls;
	private AutonStateMachineInterface autonStateMachine;
	private AutonControls autonControls;
	private RobotHardware robotHardware;
//	private ShooterStateMachine shooterStateMachine;
	private LiftArmController liftArmController;
	private DriveController driveController;
//	private ArmController armController;
	private CatapultAndIntakeController catapultAndIntakeController;
	private HeadingController headingController;
//	private AutoDriveController autoDriveController;
	private WinchController winchController;
	private AutoTargetController autoTargetController;
	private int loopPeriod = 0;
	private int loopCnt = 0;
	private CameraServer cameraServer;
	private int autonMode = 0;
	
	 public RobotBase() {
		    super();	
		    driveControls = new DriveControls();
		    autonControls = new AutonControls();
		    robotHardware = new RobotHardware();
//		    shooterStateMachine = new ShooterStateMachine();
		    driveController = new DriveController();
		    catapultAndIntakeController = new CatapultAndIntakeController();
//		    armController = new ArmController();
		    headingController = new HeadingController();	
	//	    autoDriveController = new AutoDriveController();
		    winchController = new WinchController();
		    autoTargetController = new AutoTargetController();
		    
//		    shooterStateMachine.setRobotHardware(robotHardware);
		    driveControls.setRobotHardware(robotHardware);
		    driveController.setRobotHardware(robotHardware);
		    driveController.setHeadingController(headingController);
//		    driveController.setAutoDriveController(autoDriveController);
		    driveController.setAutoTargetController(autoTargetController);
		    catapultAndIntakeController.setRobotHardware(robotHardware);
//		    armController.setRobotHardware(robotHardware);
//		    armController.setAutoTargetController(autoTargetController);
		    headingController.setRobotHardware(robotHardware);
		    winchController.setRobotHardware(robotHardware);
		    autoTargetController.setRobotHardware(robotHardware);

//		    catapultAndIntakeController.setControls(driveControls); //??
//		    driveController.setDriveControls(driveControls);
//		    armController.setDriveControls(driveControls);
//		    winchController.setDriveControls(driveControls);
//		    shooterStateMachine.setControls(driveControls);

		    try {
		    	USBCamera usbCamera = new USBCamera("cam1");
//		    	usbCamera.setExposureAuto();
		    	usbCamera.setExposureManual(0);
		    	usbCamera.setBrightness(0);
		    	usbCamera.setFPS(12);
//		    	usbCamera.setWhiteBalanceManual(20);
		    	usbCamera.setSize(640, 480); // 320 240
//		    	usbCamera.setWhiteBalanceAuto();
			    cameraServer = CameraServer.getInstance();
			    cameraServer.setQuality(40); // 50
			    cameraServer.startAutomaticCapture(usbCamera);		    	
		    } catch(Exception ex) {
		    	System.out.println("No USB Camera Connected...");
		    }

		    try {
		    	String host = java.net.InetAddress.getLocalHost().toString();
		    	if (host.startsWith(PRACTICE_ROBOT_HOST)) {
		    		this.isCompetitionRobot = false;
		    	}
		    	System.out.println("Host: " + host + " Competition: "+this.isCompetitionRobot);
		    } catch(Exception ex) {		    
		    }
		    robotHardware.setCompetitionRobot(this.isCompetitionRobot);	
//		    shooterStateMachine.setCompetitionRobot(this.isCompetitionRobot);
		    catapultAndIntakeController.setCompetitionRobot(this.isCompetitionRobot);
//		    armController.setCompetitionRobot(this.isCompetitionRobot);
		    driveController.setCompetitionRobot(this.isCompetitionRobot);
	 }
	 
	    @Override
	    public void robotInit() {   	
	    }
	    
	    @Override
	    public void disabled() {
	    }
	    
	    public void teleopInit() {
	    }

	    @Override
	    public void autonomous(){
	    	try {
	    		autonLocal();
	    	}
	    	catch(Exception ex) {
	    		System.out.println("Caught Auton Ex:"+ex);	
	    	}
	    }
	    
	    public void autonLocal(){
		    driveController.setControls(autonControls);
		    catapultAndIntakeController.setControls(autonControls);
//		    armController.setControls(autonControls);
		    winchController.setControls(autonControls);
//		    shooterStateMachine.setControls(autonControls);

		    int desiredAutonMode = 0;
		    
			try {
				desiredAutonMode = (int)SmartDashboard.getNumber("autonMode");				
			} catch(Exception ex) {}

			System.out.println("Running Auton "+desiredAutonMode);
			
			switch(desiredAutonMode) {
			case 0: 
				autonStateMachine = new AutonStateMachine0();								
				break;
			case 1:
				autonStateMachine = new AutonStateMachine1();				
				break;
			case 2:
				autonStateMachine = new AutonStateMachine2();				
				break;
			case 3:
				autonStateMachine = new AutonStateMachine3();
				break;
			}
		    
			SmartDashboard.putNumber("aMode", desiredAutonMode);

			autonStateMachine.setControls(autonControls);
			autonStateMachine.setRobotHardware(robotHardware);
			autonStateMachine.setAutoTargetController(autoTargetController);
//			autonStateMachine.setArmController(armController);	
			autonStateMachine.setCatapultAndIntakeController(catapultAndIntakeController);

	    	long prevLoopTime = 0;

		       while (this.isAutonomous()) {

		       	long currentTime = System.currentTimeMillis();
		       	
		       	if ((currentTime - prevLoopTime) >= 20) {
		       		loopPeriod = (int)(currentTime - prevLoopTime);
		       		prevLoopTime = currentTime;
			    	loopCnt++;   
			    		       				       		
			       	robotHardware.process();
			       	autonStateMachine.process();
			       	headingController.process();
//			       	autoDriveController.process();
			       	autoTargetController.process();
			       	driveController.process();
			       	catapultAndIntakeController.process();
//			       	armController.process();
			       	winchController.process();
//			       	shooterStateMachine.process();			     
			       	
					if (loopCnt % 4 == 0) { //every other loop send out the netwrork tables to help with comm
						processCommunication(autonControls);
					}					
		       	} 
		       	Timer.delay(0.001);		       	
		      }			
	    }

	    @Override
	    public void operatorControl() {

		       driveControls.reset();
		       robotHardware.reset();
//		       shooterStateMachine.reset();
		       catapultAndIntakeController.reset();
//		       armController.reset();

		    driveController.setControls(driveControls);
		    catapultAndIntakeController.setControls(driveControls);
//		    armController.setControls(driveControls);
		    winchController.setControls(driveControls);
//		    shooterStateMachine.setControls(driveControls);

	    	long prevLoopTime = 0;

		       while (isOperatorControl() && isEnabled()) {
		    	
		       	long currentTime = System.currentTimeMillis();
		       	
		       	if ((currentTime - prevLoopTime) >= 20) {
		       		loopPeriod = (int)(currentTime - prevLoopTime);
		       		prevLoopTime = currentTime;
			    	loopCnt++;   
			    		       				       		
			    	robotHardware.getPimpLightsSolenoid().set(true);
		       		driveControls.process();
			       	robotHardware.process();
			       	headingController.process();
	//		       	autoDriveController.process();
			       	autoTargetController.process();
			       	driveController.process();
			       	catapultAndIntakeController.process();
//			       	armController.process();
			       	winchController.process();
//			       	shooterStateMachine.startLoading(driveControls.isShooterIntake());
//			       	shooterStateMachine.fire(driveControls.isShooterFire());
//			       	shooterStateMachine.eject(driveControls.isShooterEject());
//			       	shooterStateMachine.process();			     
			       	
					if (autoTargetController.getTargetDetected()) {
//						System.out.println(System.currentTimeMillis()+","+String.format("%1d,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f",1,autoTargetController.getErrorX(),autoTargetController.getErrorY(),armController.getShooterAngleTarget(),armController.getShooterAngleCommand(),robotHardware.getShooterAngleFiltered(),robotHardware.getShooterAngleMotor().getOutputVoltage(),headingController.getAutoHeadingTarget(),robotHardware.getHeading(),robotHardware.getDriveLeftMotorMaster().getOutputVoltage(),robotHardware.getDriveRightMotorMaster().getOutputVoltage()));													
//						System.out.println(System.currentTimeMillis()+","+String.format("%1d,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f",1,autoTargetController.getErrorX(),autoTargetController.getErrorY(),headingController.getAutoHeadingTarget(),robotHardware.getHeading(),robotHardware.getDriveLeftMotorMaster().getOutputVoltage(),robotHardware.getDriveRightMotorMaster().getOutputVoltage()));													
					}

					if (loopCnt % 4 == 0) { //every other loop send out the netwrork tables to help with comm
						processCommunication(driveControls);
					}					
		       	} 
		       	Timer.delay(0.001);		       	
		      }
	       System.out.println("disabled...");
	       driveControls.reset();
	       robotHardware.reset();
//	       shooterStateMachine.reset();
	       catapultAndIntakeController.reset();
//	       armController.reset();
	    }

	    
	    public void processCommunication(ControlsInterface controls) {
	       	SmartDashboard.putNumber("R.loopPeriod",loopPeriod);
			SmartDashboard.putNumber("R.loopCnt",loopCnt);
			
			SmartDashboard.putNumber("R.aMode",autonMode);
			
//			SmartDashboard.putNumber("A.aAngTgt", armController.getArmAngleTarget());
//			SmartDashboard.putNumber("A.sAngTgt", armController.getShooterAngleTarget());
										
//			SmartDashboard.putNumber("A.armMtrPwr",this.robotHardware.getArmMotorMaster().getOutputVoltage());
//			SmartDashboard.putNumber("A.shtrMtrPwr",this.robotHardware.getShooterAngleMotor().getOutputVoltage());
			
//			SmartDashboard.putNumber("AT.hAdj",autoTargetController.getHeadingAdjust());
//			SmartDashboard.putNumber("AT.sAngAdj",autoTargetController.getShooterAngleAdjust());

			
			
			SmartDashboard.putBoolean("D.isHGr", controls.isHighGear());
			SmartDashboard.putBoolean("D.isAH", controls.isAutoHeading());
			SmartDashboard.putBoolean("D.isAA", controls.isAutoAngle());
			
			if(controls.isAutoTarget()) {
				SmartDashboard.putString("DC.drMd","T");				
			}
			else if(controls.isAutoPickup()) {
				SmartDashboard.putString("DC.drMd","P");				
			}
/*			else if (controls.isTurtleMode()) {
				SmartDashboard.putString("DC.drMd","L");									
			}*/	
			else { //manual
				SmartDashboard.putString("DC.drMd","M");						
			}
			
			SmartDashboard.putNumber("DC.tH",driveController.getAutoHeadingTarget());						

			SmartDashboard.putNumber("H.p",this.robotHardware.getPressure());
			SmartDashboard.putNumber("H.h",this.robotHardware.getHeading());

//			SmartDashboard.putNumber("H.aAng",this.robotHardware.getArmAngleFiltered());
//			SmartDashboard.putNumber("H.sAng",this.robotHardware.getShooterAngleFiltered());

//			SmartDashboard.putBoolean("H.aAngEncErr",this.robotHardware.isArmAngleEncoderError());
//			SmartDashboard.putBoolean("H.sAngEncErr",this.robotHardware.isShooterAngleEncoderError());

//			SmartDashboard.putBoolean("H.isBallLd",this.robotHardware.isBallLoaded());

//			SmartDashboard.putNumber("H.aAngRaw",this.robotHardware.getArmMotorMaster().getPulseWidthPosition());
//			SmartDashboard.putNumber("H.sAngRaw",this.robotHardware.getShooterAngleMotor().getPulseWidthPosition());
			
			SmartDashboard.putNumber("H.lftDrEncDst", this.robotHardware.getLeftDrivePosition());
			SmartDashboard.putNumber("H.rghtDrEncDst", this.robotHardware.getRightDrivePosition());

//			SmartDashboard.putBoolean("H.inBall",this.robotHardware.isBallLoaded());
			
//			SmartDashboard.putBoolean("S.inOn",(this.shooterStateMachine.getState() == shooterStateMachine.LOADING_STATE));

			//			SmartDashboard.putBoolean("D.isShtrIn", this.isShooterIntake);
//			SmartDashboard.putBoolean("D.isShtrEj", this.isShooterEject);
//			SmartDashboard.putBoolean("D.isShtrFr", this.isShooterFire);
			
//			SmartDashboard.putNumber("H.armMotorMasterCurrent",this.armMotorMaster.getOutputCurrent());
//			SmartDashboard.putNumber("H.armMotorSlaveCurrent",this.armMotorSlave.getOutputCurrent());
			
	    }
	    
}
