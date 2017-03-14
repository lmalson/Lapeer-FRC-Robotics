package org.lapeerrobotics.frc5460.dashboard;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class VideoCaptureThread extends Thread implements Runnable {

	private VideoCapture videoCapture;
	private Frame frame;
	private boolean isRunning = false;
	private ImageProcessor imageProcessor;
	private DashboardMain dashboard;
	
	public VideoCaptureThread() {
		
	}
	
	public void setVideoCapture(VideoCapture v) {
		this.videoCapture = v;
	}
		
	public void setIsRunning(boolean i) {
		this.isRunning = i;
	}
	
	public void run() {
		Mat image = new Mat();
		while(isRunning) {
			videoCapture.read(image);
			if (image != null && frame.getGraphics() != null) {
				Mat r = imageProcessor.process(image);
				dashboard.drawCameraImage(r);
			}
			 try {
	                Thread.sleep(5);
	            } catch (InterruptedException e){
	            }
		}
	}

	public void setFrame(Frame f) {
		this.frame = f;		
	}

	public void setImageProcessor(ImageProcessor imageProcessor) {
		this.imageProcessor = imageProcessor;
	}

	public void setDashboard(DashboardMain dashboard) {
		this.dashboard = dashboard;		
	}
}
