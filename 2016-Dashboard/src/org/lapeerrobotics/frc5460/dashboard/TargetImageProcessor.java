package org.lapeerrobotics.frc5460.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class TargetImageProcessor implements ImageProcessor {

    private final static Scalar CONTOUR_COLOR_RED_RGB = new Scalar(255,0,0,255);
    private final static Scalar CONTOUR_COLOR_GREEN_RGB = new Scalar(0,255,0,255);
    private final static Scalar CONTOUR_COLOR_BLUE_RGB = new Scalar(0,0,255,255);
    private final static Scalar CONTOUR_COLOR_WHITE_RGB = new Scalar(255,255,255,255);
    private final static Scalar CONTOUR_COLOR_YELLOW_RGB = new Scalar(255,255,0,255);
    private final static Scalar CONTOUR_COLOR_BLACK_RGB = new Scalar(0,0,0,255);
	private static final double MIN_TARGET_SIZE = 1000;
//    private static Scalar GREEN_LOWER_BOUNDS_HSV = new Scalar(60,0,225);
//    private static Scalar GREEN_UPPER_BOUNDS_HSV = new Scalar(120,160,255);
//    private static Scalar GREEN_LOWER_BOUNDS_HSV = new Scalar(80,120,80);
//    private static Scalar GREEN_UPPER_BOUNDS_HSV = new Scalar(120,240,240);
//    private static Scalar GREEN_LOWER_BOUNDS_HSV = new Scalar(72,10,100);
//    private static Scalar GREEN_UPPER_BOUNDS_HSV = new Scalar(120,240,240);
    private static Scalar GREEN_LOWER_BOUNDS_HSV = new Scalar(60,150,80);
    private static Scalar GREEN_UPPER_BOUNDS_HSV = new Scalar(80,250,200);
    Mat mHierarchy = new Mat();
	private NetworkTable smartDashboard;
	private double targetImageCount = 0;
	
	private Mat mat1;
	private Mat mat1a;
	private Mat mat2;
	private Mat mat2a;
	private Mat mat3;
	
	public TargetImageProcessor() {

	}
	
	public Mat process(Mat in) {

		boolean foundTarget = false;
        int errorX = 0;
        int errorY = 0;
        
		int width = in.cols();
		int height = in.rows();
		int targetX = 270;  //  (int)(0.5*width);
		int targetY = 285; //(int)(0.7*height); //.25
		
		if(mat1 == null)
		{
			mat1 = new Mat(height,width,CvType.CV_8UC3); 
			mat1a = mat1.clone();
			mat2 = mat1.clone();
			mat2a = mat1.clone();
			mat3 = mat1.clone();
	
		}
		
		in.convertTo(mat1, CvType.CV_8UC3);
		Imgproc.cvtColor(mat1, mat1a, Imgproc.COLOR_RGB2HSV);
				
		Core.inRange(mat1a, GREEN_LOWER_BOUNDS_HSV, GREEN_UPPER_BOUNDS_HSV, mat2);
	    Imgproc.dilate(mat2, mat2a, new Mat());
	    Imgproc.dilate(mat2a, mat3, new Mat()); //fill in holes  
	    
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        // find countours(shapes)
        Imgproc.findContours(mat3, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //draw - on mat1 image
        Imgproc.drawContours(mat1, contours, -1, CONTOUR_COLOR_RED_RGB, 1); //Everything
        
        Iterator<MatOfPoint> each = contours.iterator();
        
        Rect[] candidateTargets = new Rect[10]; //rectangle array 1st 10 potential targets 
        int candidateTargetIndex = 0;
        
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            if (area > MIN_TARGET_SIZE) {
                System.out.println("1. Contour area: "+area);
                boolean isRealTarget = matchTarget(wrapper,mat1); // check if real target

                if (isRealTarget) {
                    System.out.println("2. Real Target: ");
                	foundTarget = true;
                    Rect r = Imgproc.boundingRect(wrapper);
            		Imgproc.rectangle(mat1, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), CONTOUR_COLOR_BLUE_RGB, 1);
                    candidateTargets[candidateTargetIndex] = r; 
                    if (candidateTargetIndex < 9)
                    candidateTargetIndex++;
                }
    		}              
        }

        System.out.println("3. Compare Widths ");

        if (foundTarget) {
        	int maxTargetWidth = 0;
        	int maxTargetIndex = 0;
        	for(int i=0; i<candidateTargets.length; i++) {
        		if (candidateTargets[i] != null) {
        			if (candidateTargets[i].width > maxTargetWidth) {
        				maxTargetWidth = candidateTargets[i].width;
        				maxTargetIndex = i;
        			}	
        		}	
        	}	
        	
        	Rect r = candidateTargets[maxTargetIndex];
    		int centerX = (int)(r.x + 0.5*r.width);
    		int centerY = (int)(r.y + 0.5*r.height);
    		errorX = targetX - centerX;
    		errorY = -(targetY - centerY); 

    		if ((Math.abs(errorX) < 40) && (Math.abs(errorY) < 40)) { //on target
            	Imgproc.rectangle(mat1, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), CONTOUR_COLOR_GREEN_RGB, 2);
                Imgproc.circle(mat1, new Point(centerX,centerY), 10, CONTOUR_COLOR_GREEN_RGB);
                Imgproc.line(mat1, new Point(centerX-20,centerY), new Point(centerX+20,centerY), CONTOUR_COLOR_GREEN_RGB,2);
                Imgproc.line(mat1, new Point(centerX,centerY-15), new Point(centerX,centerY+15), CONTOUR_COLOR_GREEN_RGB,2);
    			
    		}
    		else { //not on target
            	Imgproc.rectangle(mat1, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), CONTOUR_COLOR_BLUE_RGB, 2);
                Imgproc.circle(mat1, new Point(centerX,centerY), 10, CONTOUR_COLOR_BLUE_RGB);
                Imgproc.line(mat1, new Point(centerX-20,centerY), new Point(centerX+20,centerY), CONTOUR_COLOR_BLUE_RGB,2);
                Imgproc.line(mat1, new Point(centerX,centerY-15), new Point(centerX,centerY+15), CONTOUR_COLOR_BLUE_RGB,2);    			
    		}
    		
    		
    		System.out.println("errorX "+errorX+" errorY "+errorY);
    		Imgproc.putText(mat1, "errorX: "+errorX, new Point(20,10), 3, 0.5, CONTOUR_COLOR_WHITE_RGB);
    		Imgproc.putText(mat1, "errorY: "+errorY, new Point(20,30), 3, 0.5, CONTOUR_COLOR_WHITE_RGB);
    		double rPwr = smartDashboard.getNumber("rPwr",0.0);
    		Imgproc.putText(mat1, "rPwr: "+rPwr, new Point(20,50), 3, 0.5, CONTOUR_COLOR_WHITE_RGB);
    		double aTg = smartDashboard.getNumber("A.armAngTgt",0.0);
    		Imgproc.putText(mat1, "armTarg: "+aTg, new Point(20,70), 3, 0.5, CONTOUR_COLOR_WHITE_RGB);    	    		    	    		
    		double aCom = smartDashboard.getNumber("A.armAngCmd",0.0);
    		Imgproc.putText(mat1, "armComm: "+aCom, new Point(20,90), 3, 0.5, CONTOUR_COLOR_WHITE_RGB);    	    		    	    		
    		double aAdj = smartDashboard.getNumber("A.adj",0.0);
    		Imgproc.putText(mat1, "adjust: "+aAdj, new Point(20,110), 3, 0.5, CONTOUR_COLOR_WHITE_RGB);    	    		    	    		
        }	
                    
        Imgproc.circle(mat1, new Point(targetX,targetY), 10, CONTOUR_COLOR_YELLOW_RGB);
        Imgproc.line(mat1, new Point(targetX-20,targetY), new Point(targetX+20,targetY), CONTOUR_COLOR_YELLOW_RGB,2);
        Imgproc.line(mat1, new Point(targetX,targetY-15), new Point(targetX,targetY+15), CONTOUR_COLOR_YELLOW_RGB,2);
                    
  		targetImageCount++;  		
   		smartDashboard.putNumber("tgtImgCnt", targetImageCount );
		smartDashboard.putBoolean("targetDetected", foundTarget);
		smartDashboard.putNumber("errorY", errorY);
		smartDashboard.putNumber("errorX", errorX);
				
		return mat1;
	}

	private boolean matchTarget(MatOfPoint wrapper, Mat in) {
        Rect r = Imgproc.boundingRect(wrapper); // creates rectangle from counter points
        int rInX = r.x + (int)(.30*r.width);
        int rInY = r.y;
        int rInX2 = r.x + (int)(.70*r.width);
        int rInY2 = r.y + (int)(.55*r.height);
        //draw rectangle in red
        
		Imgproc.rectangle(in, new Point(rInX, rInY), new Point(rInX2, rInY2), CONTOUR_COLOR_RED_RGB, 1);
		Imgproc.rectangle(in, new Point(r.x, r.y), new Point(r.x + r.width, r.y + r.height), CONTOUR_COLOR_RED_RGB, 1);

		Point[] p = wrapper.toArray();
		boolean realTarget = true;
		for(int i = 0;i<p.length;i++) { //do any points fall into inner rectangle - middle targeting  
			if(p[i].x > rInX && p[i].x < rInX2 && p[i].y > rInY && p[i].y < rInY2){
				realTarget = false;
				break;
			}
		}        
		return realTarget;
	}

	public void setSmartDashboard(NetworkTable smartDashboard) {
		this.smartDashboard = smartDashboard;		
	}
	
}


//	    MatOfPoint polyline = new MatOfPoint(new Point(300, 350), new Point(640, 350), new Point(640, 480), new Point(300, 480), new Point(300, 350));
//MatOfPoint polyline = new MatOfPoint(new Point(300, 280), new Point(640, 280), new Point(640, 480), new Point(300, 480), new Point(300, 280));
//List<MatOfPoint> polylines = new ArrayList<MatOfPoint>();
//polylines.add(polyline);
//Imgproc.fillPoly(in, polylines, CONTOUR_COLOR_BLACK_RGB);
//Imgproc.fillPoly(mat1, polylines, CONTOUR_COLOR_BLACK_RGB);
