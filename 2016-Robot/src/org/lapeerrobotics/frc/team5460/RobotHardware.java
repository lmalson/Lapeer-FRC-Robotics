package org.lapeerrobotics.frc.team5460;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotHardware {
	
	public final static double VOLTS_PER_DEGREE_PER_SECOND = 0.0128;

	private boolean isCompetitionRobot = true;
	private double intakeBallSensorValue = 0.0;
	private boolean isBallLoaded = false;
	private CANTalon driveRightMotorMaster;
	private CANTalon driveLeftMotorMaster;
	private CANTalon driveRightMotorSlave;
	private CANTalon driveLeftMotorSlave;
//	private CANTalon shooterMotorMaster;
//	private CANTalon shooterMotorSlave;
//	private CANTalon shooterAngleMotor;
//	private CANTalon armMotorMaster;
//	private CANTalon armMotorSlave;
	private CANTalon winchMotor;
	private CANTalon intakeMotor;
	private CANTalon liftArms;
	private DoubleActionSolenoid intakeSolenoid;
	private DoubleActionSolenoid gearShiftingSolenoid;
	private DoubleActionSolenoid scissorLiftReleaseSolenoid;
	private Solenoid shooterSolenoid;
	private Solenoid pimpLightsSolenoid;
	private Compressor compressor;
	private AnalogGyro analogGyroSensor;
	private AnalogInput analogPressureSensor;
//	private double armAngle;
//	private double shooterAngle;
//	private double armAngleFiltered;
//	private double shooterAngleFiltered;
	private double headingFiltered;
	private int cnt = 0;
	
//	private MedianFilter armAngleFilter;
//	private MedianFilter shooterAngleFilter;
	private MedianFilter gyroFilter;
	
	private double pressure = 0.0;
	
//	private boolean armAngleEncoderError = false;
//	private boolean shooterAngleEncoderError = false;
	
	public RobotHardware() {
		driveRightMotorMaster = new CANTalon(1);
		driveRightMotorMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		driveRightMotorMaster.changeControlMode(TalonControlMode.PercentVbus);  //was speed
		driveRightMotorMaster.configEncoderCodesPerRev(128);
		driveRightMotorMaster.setPosition(0.0);

		driveRightMotorSlave = new CANTalon(2);
		driveRightMotorSlave.changeControlMode(TalonControlMode.Follower);
		driveRightMotorSlave.set(driveRightMotorMaster.getDeviceID());

		driveLeftMotorMaster = new CANTalon(3);
		driveLeftMotorMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		driveLeftMotorMaster.changeControlMode(TalonControlMode.PercentVbus);	//was speed
		driveLeftMotorMaster.configEncoderCodesPerRev(256);
		driveLeftMotorMaster.setPosition(0.0);

		driveLeftMotorSlave = new CANTalon(4);
		driveLeftMotorSlave.changeControlMode(TalonControlMode.Follower);
		driveLeftMotorSlave.set(driveLeftMotorMaster.getDeviceID());
		
		liftArms = new CANTalon(5);
		liftArms.changeControlMode(TalonControlMode.PercentVbus);
		intakeMotor = new CANTalon(7);
		
		winchMotor = new CANTalon(10);
		
		

		gearShiftingSolenoid = new DoubleActionSolenoid(0,1);
		shooterSolenoid = new Solenoid(2);
		intakeSolenoid = new DoubleActionSolenoid(3,4);		
		scissorLiftReleaseSolenoid = new DoubleActionSolenoid(5,6);

		pimpLightsSolenoid = new Solenoid(7);
		analogGyroSensor = new AnalogGyro(0);
    	analogGyroSensor.setSensitivity(VOLTS_PER_DEGREE_PER_SECOND);
    	analogGyroSensor.calibrate();
				
    	analogPressureSensor = new AnalogInput(1);
    	
		compressor = new Compressor(0);
    	compressor.setClosedLoopControl(true);    	
    	
    	gyroFilter = new MedianFilter();
	}
	
	public void setCompetitionRobot(boolean b) {
		this.isCompetitionRobot = b;
	}
	
	public boolean isCompetitionRobot() {
		return this.isCompetitionRobot;
	}
	
	public void robotInit() {
		
	}
	
	public void process() {
		 
		cnt++;
		
		intakeSolenoid.process();
		gearShiftingSolenoid.process();
	//	catapultSolenoid.process();
		scissorLiftReleaseSolenoid.process();
		
//		intakeBallSensorValue = shooterMotorMaster.getAnalogInRaw()*5.0/1024.0;			
				
		pressure = this.analogPressureSensor.getAverageVoltage() * 41.5;
		
/*		if(this.intakeBallSensorValue > 2.5) {
			isBallLoaded = true;
		}
		else {
			isBallLoaded = false;
		}*/
		
//		int aAngRaw = this.armMotorMaster.getPulseWidthPosition() & 0x0FFF;
//		int sAngRaw = this.shooterAngleMotor.getPulseWidthPosition() & 0x0FFF;

//		double armAngleRaw = aAngRaw*360.0/4096.0;
//		double shooterAngleRaw = sAngRaw*360.0/4096.0;			

/*		if(isCompetitionRobot) {
			shooterAngle = 210.0 - shooterAngleRaw; // 300
			armAngle = armAngleRaw - 26.0;
			
		} else {
			if(shooterAngleRaw >= 112.0 && shooterAngleRaw <= 360.0) {
				shooterAngle = 292.0 - shooterAngleRaw;
			}
			else {
				shooterAngle = -68.0 - shooterAngleRaw;
			}
			if(armAngleRaw >= 0.0 && armAngleRaw <= 286.0) {
				armAngle = 106.0 - armAngleRaw;
			}
			else {
				armAngle = 466.0 - armAngleRaw;
			}
		}*/
		
//		armAngleFiltered = this.armAngleFilter.filter(armAngle);
//		shooterAngleFiltered = this.shooterAngleFilter.filter(shooterAngle);

		if(isCompetitionRobot) {
			headingFiltered = this.gyroFilter.filter(this.analogGyroSensor.getAngle()); 				
		} 
		else {
			headingFiltered = this.gyroFilter.filter(this.analogGyroSensor.getAngle()); // neg, gyro upside down			
		}
		
		if (headingFiltered > 180.0)
			headingFiltered -= 360.0;
		if (headingFiltered < -180.0)
			headingFiltered += 360.0;					
		
		
	}
	
	public double getHeading(){
		return headingFiltered;
	}
	
	public AnalogGyro getAnalogGyro() {
		return this.analogGyroSensor;
	}
	
	public CANTalon getDriveRightMotorMaster() {
		return this.driveRightMotorMaster;
	}
	
	public CANTalon getDriveLeftMotorMaster() {
		return this.driveLeftMotorMaster;
	}
	
/*	public CANTalon getShooterMotorMaster() {
		return this.shooterMotorMaster;
	}*/
	
/*	public CANTalon getShooterAngleMotor(){
		return this.shooterAngleMotor;
	}*/
	
/*	public CANTalon getArmMotorMaster() {
		return this.armMotorMaster;
	}*/
	
	public CANTalon getWinchMotor() {
		return this.winchMotor;
	}
	
	public CANTalon getIntakeMotor(){
		return this.intakeMotor;
	}
	
	public CANTalon getLiftArms() {
		return this.liftArms;
	}
	
	public DoubleActionSolenoid getIntakeSolenoid(){
		return this.intakeSolenoid;
	}
	
	public DoubleActionSolenoid getGearShiftingSolenoid() {
		return this.gearShiftingSolenoid;
	}
	
	public Solenoid getPimpLightsSolenoid() {
		return this.pimpLightsSolenoid;
	}
	
	public DoubleActionSolenoid getScissorLiftReleaseSolenoid() {
		return this.scissorLiftReleaseSolenoid;
	}
	
	public Solenoid getShooterSolenoid() {
		return this.shooterSolenoid;
	}
	
	public double getDriveRightMotorEncoder() {
		return this.driveRightMotorMaster.getSpeed();
	}
	
	public double getDriveLeftMotorEncoder() {
		return this.driveLeftMotorMaster.getSpeed();
	}
	
/*	public boolean isBallLoaded() {
		return this.isBallLoaded;
	}*/

	
	public double getLeftDrivePositionInches() {
		return this.getDriveLeftMotorMaster().getPosition() * 5.0;
	}
	
	public double getRightDrivePositionInches() {
		return this.getDriveRightMotorMaster().getPosition() * 5.0;
	}
	
	public double getLeftDrivePosition() {
		if (this.isCompetitionRobot)
			return this.getDriveLeftMotorMaster().getPosition();
		else
			return this.getDriveLeftMotorMaster().getPosition();			
	}
	
	public double getRightDrivePosition() {
		if (this.isCompetitionRobot)
			return -this.getDriveRightMotorMaster().getPosition();
		else
			return this.getDriveRightMotorMaster().getPosition();			
	}
	
	public double getPressure() {
		return this.pressure;
	}
	
	public void reset() {
//		getArmMotorMaster().setPulseWidthPosition(0);
//		getArmMotorMaster().setEncPosition(0);
//		getArmMotorMaster().setPosition(0);
//		getArmMotorMaster().set(0.0);
//		getShooterAngleMotor().setPulseWidthPosition(0);
//		getShooterAngleMotor().setEncPosition(0);
//		getShooterAngleMotor().setPosition(0);
//		getShooterAngleMotor().set(0.0);
		this.analogGyroSensor.reset();
		//TODO make resets for the new stuff
	}
	
}
