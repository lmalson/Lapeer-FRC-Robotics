package org.usfirst.frc.team5460.robot;

import org.lapeerrobotics.frc.team5460.RobotBase;

public class Robot extends RobotBase {
	
	public Robot() {
		super();
	}
	
}

/*
public class Robot extends SampleRobot {

	public final static String COMPETITION_ROBOT_HOST = "roboRIO-5460"; //host names
	public final static String PRACTICE_ROBOT_HOST = "roboRIO-5461";
	private boolean isCompetitionRobot = true;
	
	private DriveControls driveControls;
	private RobotHardware robotHardware;
	private ShooterStateMachine shooterStateMachine;
	private DriveController driveController;
	private ArmController armController;
	private HeadingController headingController;
	private WinchController winchController;
	private AutoTargetController autoTargetController;
	private int loopPeriod = 0;
	private int loopCnt = 0;
	private CameraServer cameraServer;
	
	 public Robot() {
		    super();	
		    driveControls = new DriveControls();
		    robotHardware = new RobotHardware();
		    shooterStateMachine = new ShooterStateMachine();
		    driveController = new DriveController();
		    armController = new ArmController();
		    headingController = new HeadingController();		
		    winchController = new WinchController();
		    autoTargetController = new AutoTargetController();
		    
		    shooterStateMachine.setRobotHardware(robotHardware);
		    driveControls.setRobotHardware(robotHardware);
		    driveController.setRobotHardware(robotHardware);
		    driveController.setDriveControls(driveControls);
		    driveController.setHeadingControler(headingController);
		    driveController.setAutoTargetController(autoTargetController);
		    armController.setRobotHardware(robotHardware);
		    armController.setDriveControls(driveControls);
		    armController.setAutoTargetController(autoTargetController);
		    headingController.setRobotHardware(robotHardware);
		    winchController.setRobotHardware(robotHardware);
		    winchController.setDriveControls(driveControls);
		    autoTargetController.setRobotHardware(robotHardware);

		    try {
		    	USBCamera usbCamera = new USBCamera("cam0");
		 //   	usbCamera.setBrightness(50); // 1-100
		    	usbCamera.setExposureAuto();
		    	usbCamera.setFPS(10);
//		    	usbCamera.setSize(640, 480); // 320 240
		    	usbCamera.setSize(320, 240); // 320 240
		    	usbCamera.setWhiteBalanceAuto();
			    cameraServer = CameraServer.getInstance();
			    cameraServer.setQuality(60); // 50
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
		    	ex.printStackTrace();
		    }
		    robotHardware.setCompetitionRobot(this.isCompetitionRobot);	
		    shooterStateMachine.setCompetitionRobot(this.isCompetitionRobot);
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
	    public void autonomous() {
	    	
	    }

	    @Override
	    public void operatorControl() {

	    	long prevLoopTime = 0;

	    	this.robotHardware.getDriveLeftMotorMaster().setPosition(0.0);
	    	this.robotHardware.getDriveRightMotorMaster().setPosition(0.0);

		       while (isOperatorControl() && isEnabled()) {

		       	long currentTime = System.currentTimeMillis();
		       	
		       	if ((currentTime - prevLoopTime) >= 20) {
		       		loopPeriod = (int)(currentTime - prevLoopTime);
		       		prevLoopTime = currentTime;
			    	loopCnt++;   
			    		       				       		
		       		driveControls.process();
			       	robotHardware.process();
			       	headingController.process();
			       	autoTargetController.process();
			       	driveController.process();
			       	armController.process();
			       	winchController.process();
			       	shooterStateMachine.startLoading(driveControls.isShooterIntake());
			       	shooterStateMachine.fire(driveControls.isShooterFire());
			       	shooterStateMachine.eject(driveControls.isShooterEject());
			       	shooterStateMachine.process();			     
			       	
					if (autoTargetController.getTargetDetected()) {
						System.out.println(System.currentTimeMillis()+","+String.format("%1d,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f,%3.2f",1,autoTargetController.getErrorX(),autoTargetController.getErrorY(),armController.getShooterAngleTarget(),armController.getShooterAngleCommand(),robotHardware.getShooterAngleFiltered(),robotHardware.getShooterAngleMotor().getOutputVoltage(),headingController.getAutoHeadingTarget(),robotHardware.getHeading(),robotHardware.getDriveLeftMotorMaster().getOutputVoltage(),robotHardware.getDriveRightMotorMaster().getOutputVoltage()));													
					}

					if (loopCnt % 2 == 0) { //every other loop send out the netwrork tables to help with comm
						processCommunication();
					}					
		       	} 
		       	Timer.delay(0.001);		       	
		      }
	       System.out.println("disabled...");
	       driveControls.reset();
	       robotHardware.reset();
	       shooterStateMachine.reset();
	       armController.reset();
	    }

	    
	    public void processCommunication() {
	       	SmartDashboard.putNumber("R.loopPeriod",loopPeriod);
			SmartDashboard.putNumber("R.loopCnt",loopCnt);
			
			SmartDashboard.putNumber("A.aAngTgt", armController.getArmAngleTarget());
			SmartDashboard.putNumber("A.sAngTgt", armController.getShooterAngleTarget());
										
			SmartDashboard.putNumber("A.armMtrPwr",this.robotHardware.getArmMotorMaster().getOutputVoltage());
			SmartDashboard.putNumber("A.shtrMtrPwr",this.robotHardware.getShooterAngleMotor().getOutputVoltage());
			
			SmartDashboard.putNumber("AT.hAdj",autoTargetController.getHeadingAdjust());
			SmartDashboard.putNumber("AT.sAngAdj",autoTargetController.getShooterAngleAdjust());

			
			
			SmartDashboard.putBoolean("D.isHGr", driveControls.isHighGear());
			SmartDashboard.putBoolean("D.isAH", driveControls.isAutoHeading());
			SmartDashboard.putBoolean("D.isAA", driveControls.isAutoAngle());
			
			if(this.driveControls.isAutoTarget()) {
				SmartDashboard.putString("DC.drMd","T");				
			}
			else if(this.driveControls.isAutoPickup()) {
				SmartDashboard.putString("DC.drMd","P");				
			}
			else if (this.driveControls.isTurtleMode()) {
				SmartDashboard.putString("DC.drMd","L");									
			}		
			else { //manual
				SmartDashboard.putString("DC.drMd","M");						
			}
			
			SmartDashboard.putNumber("DC.tH",driveController.getAutoHeadingTarget());						

			SmartDashboard.putNumber("H.p",this.robotHardware.getPressure());
			SmartDashboard.putNumber("H.h",this.robotHardware.getHeading());

			SmartDashboard.putNumber("H.aAng",this.robotHardware.getArmAngleFiltered());
			SmartDashboard.putNumber("H.sAng",this.robotHardware.getShooterAngleFiltered());

			SmartDashboard.putBoolean("H.aAngEncErr",this.robotHardware.isArmAngleEncoderError());
			SmartDashboard.putBoolean("H.sAngEncErr",this.robotHardware.isShooterAngleEncoderError());

			SmartDashboard.putBoolean("H.isBallLd",this.robotHardware.isBallLoaded());

			SmartDashboard.putNumber("H.aAngRaw",this.robotHardware.getArmMotorMaster().getPulseWidthPosition());
			SmartDashboard.putNumber("H.sAngRaw",this.robotHardware.getShooterAngleMotor().getPulseWidthPosition());
			
			SmartDashboard.putNumber("H.lftDrEncDst", this.robotHardware.getLeftDrivePosition());
			SmartDashboard.putNumber("H.rghtDrEncDst", this.robotHardware.getRightDrivePosition());

			SmartDashboard.putBoolean("H.inBall",this.robotHardware.isBallLoaded());

			//			SmartDashboard.putBoolean("D.isShtrIn", this.isShooterIntake);
//			SmartDashboard.putBoolean("D.isShtrEj", this.isShooterEject);
//			SmartDashboard.putBoolean("D.isShtrFr", this.isShooterFire);
			
//			SmartDashboard.putNumber("H.armMotorMasterCurrent",this.armMotorMaster.getOutputCurrent());
//			SmartDashboard.putNumber("H.armMotorSlaveCurrent",this.armMotorSlave.getOutputCurrent());
			
	    }
	    
}
*/