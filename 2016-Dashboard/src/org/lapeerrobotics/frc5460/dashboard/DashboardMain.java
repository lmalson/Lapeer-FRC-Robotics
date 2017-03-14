package org.lapeerrobotics.frc5460.dashboard;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class DashboardMain extends Frame implements ITableListener, WindowListener {

	
	public static Color BACKGROUND_COLOR = new Color(38,38,38,255);
	
	private final static String ROBOT_HOST = "roboRIO-5460-FRC.local"; 
//	private final static String ROBOT_HOST = "127.0.0.1";
	private RobotUSBWebcamViewer camViewer;
	private Button webCamEnabledButton;
	private boolean webCamEnabled = false;
	private NetworkTable smartDashboard;
	private UIUpdateThread uiThread;

	private long lastPaintTime = 0;
	
	private TargetImageProcessor targetImageProcessor;

	private Button videoCaptureEnabledButton;
	private boolean videoCaptureEnabled;
	
	private VideoCapture videoCapture;
	private VideoCaptureThread videoCaptureThread;

	private Button toggleImageProcessorButton;
	private int toggleImageProcessorState;

	private BallImageProcessor ballImageProcessor;
	private DashboardLayout dashboardLayout;

	private Button processImageEnabledButton;
	
	private Image offscreen;
	private BufferedImage offscreenCamera;

	private Button toggleAutonButton;
	private String[] autonLabel = new String[] {"lowbar", "cross", "gate", "cheval"};
	private int autonSelection = 0;

	public DashboardMain() {
		super("2016 FRC5460 Dashboard");
		this.setSize(1600,675);
		this.setLayout(null);

		offscreen = new BufferedImage(1600,675,BufferedImage.TYPE_INT_RGB);
		offscreenCamera = new BufferedImage(640,480,BufferedImage.TYPE_INT_RGB);
//		offscreen.getGraphics().setColor(new Color(0,0,0,0));
//		offscreen.getGraphics().fillRect(0, 0, 1600, 675);
		
		System.out.println("Started.");
		
//		camViewer = new RobotUSBWebcamViewer(ROBOT_HOST,1180,RobotUSBWebcamViewer.SIZE_320x240,10);		
		camViewer = new RobotUSBWebcamViewer(ROBOT_HOST,1180,RobotUSBWebcamViewer.SIZE_640x480,10);		
		camViewer.registerFrameCallbackHandler(new CameraFrameCallbackEventHandler() {
			@Override
			public void handleFrameEvent(int errorCd, BufferedImage frame, int cnt, String errorMessage) {
				try {
					System.out.println("Handle Frame cnt: "+cnt+" Error("+errorCd+"):"+errorMessage);
					if (errorCd == 0) {
						BufferedImage rgb = new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
						rgb.getGraphics().drawImage(frame, 0, 0, 640, 480, null, null);
						try{
				        	File outputfile = new File("cameraPicturesRaw/image" +System.currentTimeMillis()+ ".jpg");
				        	ImageIO.write(rgb, "jpg", outputfile);
				        }
				        catch(Exception e){
				        	System.out.println("error = " + e);
				        }
//						rgb.getGraphics().drawImage(frame, 0, 0, null);
						Mat m = ImageToMatConverter.img2Mat(rgb);
						Mat m2 = new Mat();
						Core.flip(m, m2, -1);
						Mat r = null;
				    	switch(toggleImageProcessorState) {
				    	case 0: 
							r = targetImageProcessor.process(m2);
				    		break;
				    	case 1:
							r = ballImageProcessor.process(m2);
				    		break;
				    	}
				    	drawCameraImage(r);
					}			
				} catch( Exception ex) {
					System.out.println("Caught Exception: "+ex);
				}					
			}

		});
		
		webCamEnabledButton = new Button();
		webCamEnabledButton.setLabel("Start RoboRio USB Cam");
		webCamEnabledButton.setBounds(20, 630, 150, 30);
		webCamEnabledButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	if (webCamEnabled) {
		    		stopWebcam();
		    	} else {
		    		startWebcam();
		    	}		    	
		    }
		});    
		
		this.add(webCamEnabledButton);

		videoCapture = new VideoCapture(0);
		
		videoCaptureEnabledButton = new Button();
		videoCaptureEnabledButton.setLabel("Start PC Web Cam");
		videoCaptureEnabledButton.setBounds(200, 630, 150, 30);
		videoCaptureEnabledButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	if (videoCaptureEnabled) {
		    		stopVideoCapture();
		    	} else {
		    		startVideoCapture();
		    	}		    	
		    }
		});    
		
		this.add(videoCaptureEnabledButton);

		toggleImageProcessorButton = new Button();
		toggleImageProcessorButton.setLabel("Target Processing");
		toggleImageProcessorButton.setBounds(400, 630, 150, 30);
		toggleImageProcessorButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	toggleImageProcessorState++;
		    	if (toggleImageProcessorState > 1)
		    		toggleImageProcessorState = 0;
		    	switch(toggleImageProcessorState) {
		    	case 0: 
		    		toggleImageProcessorButton.setLabel("Target Processing");
		    		if (videoCaptureThread != null) {
		    			videoCaptureThread.setImageProcessor(targetImageProcessor);
		    		}
		    		break;
		    	case 1:
		    		toggleImageProcessorButton.setLabel("Ball Processing");
		    		if (videoCaptureThread != null) {
		    			videoCaptureThread.setImageProcessor(ballImageProcessor);
		    		}
		    		break;
		    	}
		    }
		});    
		
		this.add(toggleImageProcessorButton);

		processImageEnabledButton = new Button();
		processImageEnabledButton.setLabel("Process cameraImage.png");
		processImageEnabledButton.setBounds(600, 630, 150, 30);
		processImageEnabledButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	
				try {
					System.out.println("Load cameraImage.png");
						BufferedImage frame = ImageIO.read(new File("res/cameraImage.png"));
						BufferedImage rgb = new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
						rgb.getGraphics().drawImage(frame, 0, 0, 640, 480, null, null);
						Mat m2 = ImageToMatConverter.img2Mat(rgb);
						System.out.println("Mat type: "+m2.type());
						Mat r = null;
				    	switch(toggleImageProcessorState) {
				    	case 0: 
							r = targetImageProcessor.process(m2);
				    		break;
				    	case 1:
							r = ballImageProcessor.process(m2);
				    		break;
				    	}				    	
				    	drawCameraImage(r);
				    	repaint();
//				    	BufferedImage i = ImageToMatConverter.mat2Img(m2);
//						getGraphics().drawImage(i, 480, 130, 640, 480, null, null);

				} catch( Exception ex) {
					System.out.println("Caught Exception: "+ex);
				}							    			    	
		    }
		});    
		
		this.add(processImageEnabledButton);

		toggleAutonButton = new Button();
		toggleAutonButton.setLabel("Auton "+autonSelection + "  "+ autonLabel[autonSelection]);
		toggleAutonButton.setBounds(800, 630, 300, 30);
		toggleAutonButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	autonSelection++;
		    	if (autonSelection > 3)
		    		autonSelection = 0;
				toggleAutonButton.setLabel("Auton "+autonSelection + "  "+ autonLabel[autonSelection]);
		    	if (smartDashboard != null) {
		    		smartDashboard.putNumber("autonMode", autonSelection);
		    	}
		    }
		});    
		
		this.add(toggleAutonButton);		
		
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress(ROBOT_HOST);
		smartDashboard = NetworkTable.getTable("SmartDashboard");
		smartDashboard.addTableListener(this);
		smartDashboard.putString("driverDashboard", "running");
		
		targetImageProcessor = new TargetImageProcessor();
		targetImageProcessor.setSmartDashboard(smartDashboard);		
		
		ballImageProcessor = new BallImageProcessor();
		ballImageProcessor.setSmartDashboard(smartDashboard);		

		dashboardLayout = new DashboardLayout();
		dashboardLayout.setSmartDashboard(smartDashboard);
		dashboardLayout.setFrame(this);
		dashboardLayout.init();

		uiThread = new UIUpdateThread();
		uiThread.setFrame((Frame)this);

	}

	public void start() {
		uiThread.setIsRunning(true);
		uiThread.run();
	}
	
	
	DashboardMain getDashboard() {
		return this;
	}

	private void stopWebcam() {
    	System.out.println("Stop Web Cam");
		webCamEnabledButton.setLabel("Start RoboRio USB Cam");		   
		camViewer.stop();
		webCamEnabled = false;		
	}
	
	private void startWebcam() {
    	System.out.println("Start Web Cam");
		webCamEnabledButton.setLabel("Stop RoboRio USB Cam");		    		
		camViewer.start();
		webCamEnabled = true;		
	}
	
	private void stopVideoCapture() {
    	System.out.println("Stop Video Capture");
    	videoCaptureEnabledButton.setLabel("Start PC Web Cam");		   
    	if (videoCaptureThread != null) {
        	videoCaptureThread.setIsRunning(false);
        	videoCaptureThread.interrupt();
        	videoCaptureThread = null;
    		videoCaptureEnabled = false;		    		
    	}
	}
	
	private void startVideoCapture() {
    	System.out.println("Start Video Capture");
    	videoCaptureEnabledButton.setLabel("Stop PC Web Cam");		    		
    	if (videoCaptureThread == null) {
        	videoCaptureThread = new VideoCaptureThread();
    		videoCaptureThread.setVideoCapture(videoCapture);
			videoCaptureThread.setDashboard(getDashboard());		    			
    		videoCaptureThread.setFrame((Frame)this);
    		videoCaptureThread.setImageProcessor(targetImageProcessor);
        	videoCaptureThread.setIsRunning(true);
        	videoCaptureThread.start();    		
    	}
		videoCaptureEnabled = true;		
	}

	public void drawCameraImage(Mat r) {
    	BufferedImage i = ImageToMatConverter.mat2Img(r);
        offscreenCamera.getGraphics().drawImage(i,0,0,640,480,null,null);    
        try{
        	File outputfile = new File("cameraPictures/image" +System.currentTimeMillis()+ ".jpg");
        	ImageIO.write(i, "jpg", outputfile);
        }
        catch(Exception e){
        	System.out.println("error = " + e);
        }
	}	
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint (Graphics g) {		
    	synchronized(this) {
    		offscreen.getGraphics().drawImage(offscreenCamera, 480, 130, 640, 480, null, null);
    		this.dashboardLayout.paint(offscreen.getGraphics());	    
		    g.drawImage(offscreen, 0, 0, this);
    	}
	}
	
	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		DashboardMain m = new DashboardMain();
		m.setVisible(true);
		m.addWindowListener(m);		
		m.start();
	}

	@Override
	public void valueChanged(ITable source, String key, Object value, boolean isNew) {
		System.out.println("SmartDashboard "+key+" = "+value);
	}
		
	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Window Closing ");
		this.dispose();
		this.stopVideoCapture();
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
