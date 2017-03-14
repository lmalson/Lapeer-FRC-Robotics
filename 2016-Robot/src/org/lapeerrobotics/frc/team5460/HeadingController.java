package org.lapeerrobotics.frc.team5460;

public class HeadingController {
	public final static double HEADING_KP = 0.035; // 0.025
	private static final double HEADING_KD = 0.0;
	private static final double MAX_HEADING_PWR = 4.0;
	private static final double MIN_HEADING_PWR = -4.0;
	
	private double headingError = 0.0;
	private double prevHeadingError = 0.0;
	private double deltaHeadingError = 0.0;

	private double autoHeadingTarget = 0.0;
	
	private double rotatePower = 0.0;
	
	private RobotHardware robotHardware;
	
	public HeadingController() {
		
		
	}
	
	public double getAutoHeadingTarget() {
		return this.autoHeadingTarget;
	}
	
	public void process() {
		prevHeadingError = headingError;
		headingError = autoHeadingTarget - this.robotHardware.getHeading();
		if (headingError > 180.0) {
			headingError -= 360.0;
		} else if (headingError < -180.0) {
			headingError += 360.0;
		}
		deltaHeadingError = headingError - prevHeadingError;

		rotatePower = HEADING_KP * headingError + HEADING_KD * deltaHeadingError;
		if (rotatePower > MAX_HEADING_PWR)
			rotatePower = MAX_HEADING_PWR;
		else if (rotatePower < MIN_HEADING_PWR)
			rotatePower = MIN_HEADING_PWR;
		
//		System.out.println("heading process " + rotatePower);
	}
	
	public void setAutoHeadingTarget(double t) {
		this.autoHeadingTarget = t;
	}
	
	public double getRotatePower() {
		return this.rotatePower;
	}
	
	public void setRobotHardware(RobotHardware robotHardware) {
		this.robotHardware = robotHardware;
	}
	
	public void reset() {
		this.headingError = 0;
		this.autoHeadingTarget = this.robotHardware.getHeading();
	}
	
}
