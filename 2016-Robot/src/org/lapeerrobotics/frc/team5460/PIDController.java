package org.lapeerrobotics.frc.team5460;

public class PIDController {

	private double target = 0.0;
	private double actual = 0.0;
	private double prevError = 0.0;
	private double deltaError = 0.0;
	private double errorSum = 0.0;
	private double error = 0.0;
	private double output = 0.0;
	private double kP = 0.0;
	private double kI = 0.0;
	private double kD = 0.0;
	private double outputLimitHigh = 0.0;
	private double outputLimitLow = 0.0;
	
	public PIDController() {		
	}

	public void process() {
		prevError = error;
		error = target - actual;
		deltaError = error - prevError;
		errorSum += error;
		double out = kP * error + kD * deltaError + kI * errorSum;
		if (out > outputLimitHigh) 
			out = outputLimitHigh;
		if (out < outputLimitLow) 
			out = outputLimitLow;
		output = out;
	}
	
	public void reset() {
		error = 0.0;
		deltaError = 0.0;
		errorSum = 0.0;
	}
	
	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public double getActual() {
		return actual;
	}

	public void setActual(double actual) {
		this.actual = actual;
	}

	public double getOutput() {
		return output;
	}

	public double getkP() {
		return kP;
	}

	public void setkP(double kP) {
		this.kP = kP;
	}

	public double getkI() {
		return kI;
	}

	public void setkI(double kI) {
		this.kI = kI;
	}

	public double getkD() {
		return kD;
	}

	public void setkD(double kD) {
		this.kD = kD;
	}

	public double getError() {
		return this.error;
	}
	
	public double getOutputLimitHigh() {
		return outputLimitHigh;
	}

	public void setOutputLimitHigh(double outputLimitHigh) {
		this.outputLimitHigh = outputLimitHigh;
	}

	public double getOutputLimitLow() {
		return outputLimitLow;
	}

	public void setOutputLimitLow(double outputLimitLow) {
		this.outputLimitLow = outputLimitLow;
	}
}
