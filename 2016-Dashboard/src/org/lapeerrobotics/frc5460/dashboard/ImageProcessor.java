package org.lapeerrobotics.frc5460.dashboard;

import org.opencv.core.Mat;

public interface ImageProcessor {
	public Mat process(Mat in);
}
