package org.lapeerrobotics.frc5460.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class BallImageProcessor implements ImageProcessor {

	private final static Scalar CONTOUR_COLOR_RED_RGB = new Scalar(255,0,0,255);
    private final static Scalar CONTOUR_COLOR_GREEN_RGB = new Scalar(0,255,0,255);
    private final static Scalar CONTOUR_COLOR_BLUE_RGB = new Scalar(0,0,255,255);
    private final static Scalar CONTOUR_COLOR_WHITE_RGB = new Scalar(255,255,255,255);
    private final static Scalar CONTOUR_COLOR_YELLOW_RGB = new Scalar(255,255,0,255);
    private final static Scalar CONTOUR_COLOR_BLACK_RGB = new Scalar(0,0,0,255);
    private static Scalar GREEN_LOWER_BOUNDS_HSV = new Scalar(60,0,225);
    private static Scalar GREEN_UPPER_BOUNDS_HSV = new Scalar(120,160,255);
    private static Scalar GRAY_LOWER_BOUNDS_HSV = new Scalar(60,0,225);
    private static Scalar GRAY_UPPER_BOUNDS_HSV = new Scalar(120,160,255);

	private NetworkTable smartDashboard;
	
	public BallImageProcessor() {

	}
	
	public Mat process(Mat in) {
		
		Mat gray = new Mat();
	    Imgproc.cvtColor(in, gray, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.blur(gray, gray, new Size(3, 3));

	    
	    Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1.2, 40);
	    System.out.println("CIRCLES #rows " + circles.rows() + " #cols " + circles.cols());
	    
	    
	    Mat out = new Mat ();	    
	    Imgproc.threshold(gray, gray, 128, 250, Imgproc.THRESH_BINARY);
	    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(gray, contours, new Mat (), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.drawContours(gray, contours, -1, CONTOUR_COLOR_RED_RGB, 1); //Everything
	    
        
	    return gray;
	    
/*		int width = in.cols();
		int height = in.rows();
		int targetX = (int)(0.5*width);
		int targetY = (int)(0.5*height);
		Mat mat1 = new Mat(height,width,CvType.CV_8UC2);
		Mat mat2 = mat1.clone();
		Mat mat3 = mat1.clone();
	    Imgproc.cvtColor(in, mat1, Imgproc.COLOR_RGB2HSV);
	    Core.inRange(mat1, GREEN_LOWER_BOUNDS_HSV, GREEN_UPPER_BOUNDS_HSV, mat2);
	    Imgproc.dilate(mat2, mat3, new Mat());
	    
		Mat gray = new Mat();
	    Imgproc.cvtColor(in, gray, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.blur(gray, gray, new Size(3, 3));

	    Mat circles = new Mat();

	   // Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60);
	    Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0 );
		
	    System.out.println("#rows " + circles.rows() + " #cols " + circles.cols());
	    double x = 0.0;
	    double y = 0.0;
	    int r = 0;

	    for( int i = 0; i < circles.cols(); i++ )
	    {
	      double[] data = circles.get(0, i);
	      for(int j = 0 ; j < data.length ; j++){
	           x = data[0];
	           y = data[1];
	           r = (int) data[2];
	      }
	      Point center = new Point(x,y);
	      // circle center  
	      
	      Imgproc.circle( in, center, 3, new Scalar(0,255,0), -1);
	      // circle outline
	      Imgproc.circle( in, center, r, new Scalar(0,0,255), 1);
	    }
	    
		 return mat3; */
	}

	public void setSmartDashboard(NetworkTable smartDashboard2) {
		this.smartDashboard = smartDashboard;
	}


}
