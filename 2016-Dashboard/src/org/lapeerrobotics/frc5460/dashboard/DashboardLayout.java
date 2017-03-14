package org.lapeerrobotics.frc5460.dashboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class DashboardLayout {

	public static Color GREEN_COLOR = new Color(0,255,65,255);
	public static Color BLACK_COLOR = new Color(0,0,0,255);
	public static Color BACKGROUND_COLOR = new Color(38,38,38,255);
	public static Color SECTION_BG_COLOR = new Color(64,64,64,255);
	public static Color DESC_TEXT_COLOR = new Color(140,140,140,255);
	public static Color BORDER_COLOR = new Color(166,166,166,255);
	public static Color TEXT_COLOR = new Color(255,255,255,255);
	public static Color BUTTON_COLOR = new Color(49,133,156,255);
	public static Color LINE_COLOR = new Color(166,166,166,255);

	private NetworkTable smartDashboard;
	private DashboardMain dashboardMain;
	private Font smallFont = new Font("Helvetica", Font.PLAIN, 20);
	private Font boldFont = new Font("Helvetica", Font.PLAIN, 28);        
	private Font descFont = new Font("Helvetica", Font.PLAIN, 14);        
	
	private double heading = 0.0;
	private double targetHeading = 0.0;	
	private double armAngle = 0.0;
	private double targetArmAngle = 10.0;
	private double shooterAngle = 0.0;
	private double targetShooterAngle = 10.0;
	private boolean isHighGear = false;
	private boolean isAutoTarget = false;
	private String driveMode = "";
	private String cameraView = "cam0";
	private boolean isAutoAngle = false;
	private double armAngleRaw = 0.0;
	private double shooterAngleRaw = 0.0;
	private double pressure = 0.0;
	private int aMode = 0;
	private boolean ballIn = false;
	private boolean intakeOn = false;
	private double rightDrivePos = 0.0; 
	private double leftDrivePos = 0.0; 
	
	private BufferedImage compass;
	private BufferedImage arm;
	private BufferedImage shooter;
	
	public DashboardLayout() {
		
	}

	public void init() {
		
		System.out.println("!!!!!!!!!!!!! Registering Listeners...   ");
		
		smartDashboard.addTableListener("D.isHGr", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Gear Change...   ");
				if (value instanceof Boolean) {
					isHighGear = (Boolean)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.inBall", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				if (value instanceof Boolean) {
					ballIn = (Boolean)value;
				}
			}
		}, true);
		
		smartDashboard.addTableListener("S.inOn", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				if (value instanceof Boolean) {
					intakeOn = (Boolean)value;
				}
			}
		}, true);
		
		smartDashboard.addTableListener("R.aMode", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Auton Change...   ");
				if (value instanceof Double) {
					aMode = ((Double)value).intValue();
				}
			}
		}, true);
		
		smartDashboard.addTableListener("D.isAH", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Auto Heading Change...   ");
				if (value instanceof Boolean) {
					isAutoTarget = (Boolean)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("DC.drMd", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Drive Mode Change...   ");
				if (value instanceof String) {
					driveMode = (String)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.h", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Heading Change...   ");
				if (value instanceof Double) {
					heading = (double)value;
//					dashboardMain.repaint();
				}
			}
		}, true);
		
		smartDashboard.addTableListener("H.rghtDrEncDst", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				if (value instanceof Double) {
					rightDrivePos = (double)value;
				}
			}
		}, true);
		
		smartDashboard.addTableListener("H.lftDrEncDst", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				if (value instanceof Double) {
					leftDrivePos = (double)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.p", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Pressure Change...   ");
				if (value instanceof Double) {
					pressure = (double)value;
				}
			}
		}, true);
		
		smartDashboard.addTableListener("DC.tH", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Target Heading Change...   ");
				if (value instanceof Double) {
					targetHeading = (double)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("D.srcCam", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Camera Source Change...   ");
				if (value instanceof String) {
					cameraView = (String)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("D.isAA", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Auto Angle Change...   ");
				if (value instanceof Boolean) {
					isAutoAngle = (Boolean)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.aAng", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Arm Angle Change...   ");
				if (value instanceof Double) {
					armAngle = (double)value;
					if (armAngle < 0.0)
						armAngle = 0.0;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.aAngRaw", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Arm Angle Raw Change...   ");
				if (value instanceof Double) {
					armAngleRaw = (double)value;
				}
			}
		}, true);
		
		smartDashboard.addTableListener("A.aAngTgt", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Arm Angle Target Change...   ");
				if (value instanceof Double) {
					targetArmAngle = (double)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.sAng", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Shooter Angle Change...   ");
				if (value instanceof Double) {
					shooterAngle = (double)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("H.sAngRaw", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Shooter Angle Raw Change...   ");
				if (value instanceof Double) {
					shooterAngleRaw = (double)value;
				}
			}
		}, true);

		smartDashboard.addTableListener("A.sAngTgt", new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean updated) {
				System.out.println("!!!!!!!!!!!!! Shooter Angle Target Change...   ");
				if (value instanceof Double) {
					targetShooterAngle = (double)value;
				}
			}
		}, true);
		
        try {
        	compass = ImageIO.read(new File("res/compassCBG.png"));
        	arm = ImageIO.read(new File("res/armCBG.png"));
        	shooter = ImageIO.read(new File("res/shooterCBG.png"));
        } catch (Exception e) {
            e.printStackTrace();        	
        }
	}

	private void drawGearButton(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 20;
	    int y = 130;
	    int w = 450;
	    int h = 80;
	    int b1x = x + 170;
	    int b2x = x + 310; 
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); //gear background
		
		if (isHighGear) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b1x, y+20, 120, 40); //high lit gear high
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b2x, y+20, 120, 40); // gear low			
		} else {
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); // gear high
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b2x, y+20, 120, 40); //low			
		}
		
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
//		g2.drawString("HIGH", b1x+20, y+48);
//		g2.drawString("LOW", b2x+20, y+48);
		g2.drawString("HIGH", b1x+35, y+48);
		g2.drawString("LOW", b2x+35, y+48);
	    g2.setFont(boldFont);
		g2.drawString("GEAR", x+20, y+42);
		g2.setColor(DESC_TEXT_COLOR);
	    g2.setFont(descFont);
		g2.drawString("Driver Right Bumper", x+20, y+64);

	}

	private void drawAutoHeadingButton(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 20;
	    int y = 220;
	    int w = 450;
	    int h = 80;
	    int b1x = x + 170;
	    int b2x = x + 310; 
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); //gear background
		
		if (isAutoTarget) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b1x, y+20, 120, 40); //high lit gear high
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b2x, y+20, 120, 40); // gear low			
		} else {
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); // gear high
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b2x, y+20, 120, 40); //low			
		}
		
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
		g2.drawString("ON", b1x+36, y+48);
		g2.drawString("OFF", b2x+36, y+48);
	    g2.setFont(boldFont);
		g2.drawString("AUTO HEA", x+20, y+42);
		g2.setColor(DESC_TEXT_COLOR);
	    g2.setFont(descFont);
		g2.drawString("Driver Left Bumper", x+20, y+64);

	}
	
	private void drawDriveModeButton(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 20; 
//	    int y = 220;
	    int y = 310;
	    int w = 450;
	    int h = 140;
	    int b1x = x + 170;
	    int b2x = x + 310; 
	    int y2 = y+60;
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); // background
		
		if (driveMode.equals("M")) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b1x, y+20, 120, 40); 
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b2x, y+20, 120, 40); 			
			g2.drawRect(b1x, y2+20, 120, 40); 			
			g2.drawRect(b2x, y2+20, 120, 40); 			
		} else if (driveMode.equals("T")) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b2x, y+20, 120, 40); 
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 			
			g2.drawRect(b1x, y2+20, 120, 40); 			
			g2.drawRect(b2x, y2+20, 120, 40); 			
		} else if (driveMode.equals("P")) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b1x, y2+20, 120, 40); 
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 			
			g2.drawRect(b2x, y+20, 120, 40); 			
			g2.drawRect(b2x, y2+20, 120, 40); 			
		} else if (driveMode.equals("L")) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b2x, y2+20, 120, 40); 
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 			
			g2.drawRect(b2x, y+20, 120, 40); 			
			g2.drawRect(b1x, y2+20, 120, 40); 			
		} else 
		{
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 			
			g2.drawRect(b2x, y+20, 120, 40); 			
			g2.drawRect(b1x, y2+20, 120, 40); 			
			g2.drawRect(b2x, y2+20, 120, 40); 						
		}
		
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont); 
		g2.drawString("MANUAL", b1x+20, y+48);
		g2.drawString("TARGET", b2x+20, y+48);
		g2.drawString("PICKUP", b1x+20, y2+48);
		g2.drawString("TURTLE", b2x+20, y2+48);
	    g2.setFont(boldFont);
		g2.drawString("DRIVE", x+20, y+42);
		g2.setColor(DESC_TEXT_COLOR);
	    g2.setFont(descFont);
		g2.drawString("Driver Buttons ", x+20, y+64);
		g2.drawString("    X      B ", x+20, y+84);
		g2.drawString("    A      Y ", x+20, y+104);
				
	}
	
	private void drawHeading(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 20; 
	    int y = 460;
	    int w = 450;
	    int h = 150;
	    int b1x = x + 170;
	    int b2x = x + 310; 
	    int y2 = y+60;
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); // background

	    g2.setColor(BLACK_COLOR);
		g2.fillRect(x+20, y+90, 410, 40); 
	    
	    int center = x+20+205;
	    
	    int xHeading = center+(int)(this.heading*190.0/180.0); // 205
	    		
		Polygon headingTop = new Polygon();
		headingTop.addPoint(xHeading, y+131);
		headingTop.addPoint(xHeading+6, y+148);
		headingTop.addPoint(xHeading-6, y+148);
		headingTop.addPoint(xHeading, y+131);

	    g2.setColor(BLACK_COLOR);
		g2.fillPolygon(headingTop);

	    g2.setColor(BUTTON_COLOR);
		g2.drawPolygon(headingTop);
		g2.drawLine(xHeading, y+92, xHeading, y+127);

	    int xTargetHeading = center+(int)(this.targetHeading*190.0/180.0); // 205

		Polygon targetHeadingTop = new Polygon();
		targetHeadingTop.addPoint(xTargetHeading, y+89);
		targetHeadingTop.addPoint(xTargetHeading+6, y+72);
		targetHeadingTop.addPoint(xTargetHeading-6, y+72);
		targetHeadingTop.addPoint(xTargetHeading, y+89);

	    g2.setColor(BLACK_COLOR);
		g2.fillPolygon(targetHeadingTop);

	    g2.setColor(GREEN_COLOR);
		g2.drawPolygon(targetHeadingTop);
		g2.drawLine(xTargetHeading, y+92, xTargetHeading, y+127);
		
		
		g2.drawImage(compass, null, x+20, y+90);
		
		g2.setColor(BORDER_COLOR);
		g2.drawRect(x+20, y+90, 410, 40); //gyro
				
		g2.setColor(TEXT_COLOR);
	    g2.setFont(boldFont);
		g2.drawString("HEADING", x+20, y+42);
		g2.setColor(DESC_TEXT_COLOR);
	    g2.setFont(descFont);
		g2.drawString("Target & Gyro Heading ", x+20, y+64);
		
	    g2.setFont(smallFont);
		g2.setColor(GREEN_COLOR);
		g2.drawString("TARGET", b1x+20, y+36);

		g2.setColor(BUTTON_COLOR);
		g2.drawString("ACTUAL", b2x+20, y+36);
		
		g2.setColor(TEXT_COLOR);
		g2.drawString(String.format("%4.1f\u00b0", targetHeading), b1x+46, y+64);
		g2.drawString(String.format("%4.1f\u00b0", heading), b2x+48, y+64);
		
	}
	
	private void drawCameraViewButton(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 480;
	    int y = 40;
	    int w = 640;
	    int h = 80;
	    int b1x = x + 170;
	    int b2x = x + 310; 
	    int b3x = x + 450; 
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); //gear background
		
		if (cameraView.equals("cam0")) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b1x, y+20, 120, 40); 
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b2x, y+20, 120, 40); 			
			g2.drawRect(b3x, y+20, 120, 40); 			
		} else if (cameraView.equals("cam1")){
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b2x, y+20, 120, 40); 			
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 
			g2.drawRect(b3x, y+20, 120, 40); 
		} else if (cameraView.equals("cam2")){
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b3x, y+20, 120, 40); 			
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 
			g2.drawRect(b2x, y+20, 120, 40); 
		} else
		{
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 
			g2.drawRect(b2x, y+20, 120, 40); 
			g2.drawRect(b3x, y+20, 120, 40); 			
		}
		
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
		g2.drawString("FRONT", b1x+20, y+48);
		g2.drawString("BACK", b2x+20, y+48);
		g2.drawString("TARGET", b3x+20, y+48);
	    g2.setFont(boldFont);
		g2.drawString("VIEW", x+20, y+48);
	}

	private void drawAutonButton(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 480;
	    int y = 40;
	    int w = 640;
	    int h = 80;
	    int b1x = x + 170;
	    int b2x = x + 310; 
	    int b3x = x + 450; 
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); //gear background
		
		if (aMode == 0) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b1x, y+20, 120, 40); 
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b2x, y+20, 120, 40); 			
			g2.drawRect(b3x, y+20, 120, 40); 			
		} else if (aMode == 1){
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b2x, y+20, 120, 40); 			
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 
			g2.drawRect(b3x, y+20, 120, 40); 
		} else if (aMode == 2){
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(b3x, y+20, 120, 40); 			
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 
			g2.drawRect(b2x, y+20, 120, 40); 
		} else
		{
			g2.setColor(BORDER_COLOR);
			g2.drawRect(b1x, y+20, 120, 40); 
			g2.drawRect(b2x, y+20, 120, 40); 
			g2.drawRect(b3x, y+20, 120, 40); 			
		}
		
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
		g2.drawString("AUTON 0", b1x+20, y+48);
		g2.drawString("AUTON 1", b2x+20, y+48);
		g2.drawString("AUTON 2", b3x+20, y+48);
	    g2.setFont(boldFont);
		g2.drawString("AUTON", x+20, y+48);
	}

	private void drawDiagnosticsView(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 1130;
	    int y = 130;
	    int w = 450;
	    int h = 480;
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); //diagnostics background

		g2.setFont(smallFont);
		g2.setColor(GREEN_COLOR);
		g2.drawString("Pressure", x+60, y+75);
		g2.setColor(BUTTON_COLOR);
		g2.drawString("Left Pos", x+60, y+125);
		g2.drawString("Right Pos", x+60, y+175);
		//g2.setColor(BUTTON_COLOR);
		//g2.drawString("ACTUAL", x+79, y+120); // 135
			
		g2.setColor(TEXT_COLOR);
		g2.drawString(String.format("%4.1f", this.pressure), x+80, y+95); // 105
		g2.drawString(String.format("%4.1f", this.leftDrivePos), x+80, y+145); // 165
		g2.drawString(String.format("%4.1f", this.rightDrivePos), x+80, y+195); // 165
			
	}
	
	private void drawDiagnosticsViewOld(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    int x = 1130;
	    int y = 130;
	    int w = 450;
	    int h = 480;
	    
	    g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(x, y, w, h); //diagnostics background

	    g2.setColor(BLACK_COLOR);
		g2.fillRect(x+20, y+60, 40, 410); 
		g2.setColor(BORDER_COLOR);
		g2.drawRect(x+20, y+60, 40, 410); 

	    g2.setColor(BLACK_COLOR);
		g2.fillRect(x+w-60, y+60, 40, 410); 
		g2.setColor(BORDER_COLOR);
		g2.drawRect(x+w-60, y+60, 40, 410); 
				
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
		g2.drawString("SHOOTER ANGLE", x+20, y+42);
		g2.drawString("ARM ANGLE", x+w-138, y+42);

				
		// Arm Angle
		
	    int yArmAngle = y+60+410-(int)(this.armAngle*190.0/45.0); // 205
	    int xSlider = x+w-60;
	    		
		Polygon armAngleP = new Polygon();
		armAngleP.addPoint(xSlider+41,yArmAngle); 
		armAngleP.addPoint(xSlider+58,yArmAngle+6); 
		armAngleP.addPoint(xSlider+58,yArmAngle-6); 
		armAngleP.addPoint(xSlider+41,yArmAngle); 

	    g2.setColor(BLACK_COLOR);
		g2.fillPolygon(armAngleP);

	    g2.setColor(BUTTON_COLOR);
		g2.drawPolygon(armAngleP);
		g2.drawLine(xSlider+3, yArmAngle, xSlider+37, yArmAngle);

	    int yTargetArmAngle = y+60+410-(int)(this.targetArmAngle*190.0/45.0);

		Polygon targetArmAngleP = new Polygon();
		targetArmAngleP.addPoint(xSlider-2,yTargetArmAngle); 
		targetArmAngleP.addPoint(xSlider-19,yTargetArmAngle+6); 
		targetArmAngleP.addPoint(xSlider-19,yTargetArmAngle-6); 
		targetArmAngleP.addPoint(xSlider-2,yTargetArmAngle); 

	    g2.setColor(BLACK_COLOR);
		g2.fillPolygon(targetArmAngleP);

	    g2.setColor(GREEN_COLOR);
		g2.drawPolygon(targetArmAngleP);
		g2.drawLine(xSlider+3, yTargetArmAngle, xSlider+37, yTargetArmAngle);
		g2.drawImage(arm, null, xSlider, y+60);
								
		// Shooter Angle
		
	    int middle = y+60+205;
	    int yShooterAngle = middle-(int)(this.shooterAngle*190.0/120.0); // 205		
	    xSlider = x+20; // x+w-60;
	    
		Polygon shooterAngleP = new Polygon();
		shooterAngleP.addPoint(xSlider+41,yShooterAngle); 
		shooterAngleP.addPoint(xSlider+58,yShooterAngle+6); 
		shooterAngleP.addPoint(xSlider+58,yShooterAngle-6); 
		shooterAngleP.addPoint(xSlider+41,yShooterAngle); 

	    g2.setColor(BLACK_COLOR);
		g2.fillPolygon(shooterAngleP);

	    g2.setColor(BUTTON_COLOR);
		g2.drawPolygon(shooterAngleP);
		g2.drawLine(xSlider+3, yShooterAngle, xSlider+37, yShooterAngle);

	    int yTargetShooterAngle = middle-(int)(this.targetShooterAngle*190.0/120.0); // 205

		Polygon targetShooterAngleP = new Polygon();
		targetShooterAngleP.addPoint(xSlider-1,yTargetShooterAngle); 
		targetShooterAngleP.addPoint(xSlider-17,yTargetShooterAngle+6); 
		targetShooterAngleP.addPoint(xSlider-17,yTargetShooterAngle-6); 
		targetShooterAngleP.addPoint(xSlider-1,yTargetShooterAngle); 

	    g2.setColor(BLACK_COLOR);
		g2.fillPolygon(targetShooterAngleP);

	    g2.setColor(GREEN_COLOR);
		g2.drawPolygon(targetShooterAngleP);
		g2.drawLine(xSlider+3, yTargetShooterAngle, xSlider+37, yTargetShooterAngle);
		g2.drawImage(shooter, null, xSlider, y+60);

		
	    g2.setFont(smallFont);
		g2.setColor(GREEN_COLOR);
		g2.drawString("TARGET", x+75, y+75);
		g2.setColor(BUTTON_COLOR);
		g2.drawString("ACTUAL", x+79, y+120); // 135
		
		g2.setColor(TEXT_COLOR);
		g2.drawString(String.format("%4.1f\u00b0", targetShooterAngle), x+105, y+95); // 105
		g2.drawString(String.format("%4.1f\u00b0", shooterAngle), x+105, y+140); // 165
		
		g2.setColor(GREEN_COLOR);
		g2.drawString("TARGET", x+w-150, y+75);
		g2.setColor(BUTTON_COLOR);
		g2.drawString("ACTUAL", x+w-148, y+120); // 135
		
		g2.setColor(TEXT_COLOR);
		g2.drawString(String.format("%4.1f\u00b0", targetArmAngle), x+w-120, y+95); // 105
		g2.drawString(String.format("%4.1f\u00b0", armAngle), x+w-120, y+140); // 165
		
		g2.setColor(DESC_TEXT_COLOR);
	    g2.setFont(descFont);
		//g2.drawString("Raw:"+String.format("%5.1f",shooterAngleRaw), x+85, y+155);
		//g2.drawString("Raw:"+String.format("%5.1f",armAngleRaw), x+w-140, y+155);

		g2.drawString("P:"+String.format("%4.1f",pressure), x+80, y+160);
		g2.drawString("L Dist:"+String.format("%4.1f", leftDrivePos), x+150, y+160);
		g2.drawString("R Dist:"+String.format("%4.1f", rightDrivePos), x+280, y+160);
		// draw arm line
		
		g2.setColor(GREEN_COLOR);

		int ax1 = x+w-120;
		int ay1 = y+340;

		int ax2 = ax1 - (int)(150 * Math.cos(Math.PI*armAngle/180.0));
		int ay2 = ay1 - (int)(150 * Math.sin(Math.PI*armAngle/180.0));
		
		g2.drawLine(ax1, ay1, ax2, ay2);
		
		double relShooterAngle = shooterAngle + armAngle;
		
		double relSAng = relShooterAngle+90.0;
		
		int sx1 = ax1 - (int)(160 * Math.cos(Math.PI*armAngle/180.0));
		int sy1 = ay1 - (int)(160 * Math.sin(Math.PI*armAngle/180.0));
		
		
		int sx2a = sx1 - (int)(10 * Math.cos(Math.PI*relSAng/180.0));
		int sy2a = sy1 - (int)(10 * Math.sin(Math.PI*relSAng/180.0));

		g2.drawLine(sx1, sy1, sx2a, sy2a);
		
		int sx2b = sx1 + (int)(30 * Math.cos(Math.PI*relSAng/180.0));
		int sy2b = sy1 + (int)(30 * Math.sin(Math.PI*relSAng/180.0));
		
		g2.drawLine(sx1, sy1, sx2b, sy2b);
		
		// shooter top 
		
		int stx1 = sx2a - (int)(40 * Math.cos(Math.PI*relShooterAngle/180.0));
		int sty1 = sy2a - (int)(40 * Math.sin(Math.PI*relShooterAngle/180.0));

		g2.drawLine(sx2a, sy2a, stx1, sty1);
		
		int sbx1 = sx2b - (int)(40 * Math.cos(Math.PI*relShooterAngle/180.0));
		int sby1 = sy2b - (int)(40 * Math.sin(Math.PI*relShooterAngle/180.0));

		g2.drawLine(sx2b, sy2b, sbx1, sby1);
		
		if (intakeOn) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(x+95, y+180, 120, 40); //high lit gear high
		} else {
			g2.setColor(BORDER_COLOR);
			g2.drawRect(x+95, y+180, 120, 40); // gear high
		}
		if (ballIn) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(x+235, y+180, 120, 40); //low			
		} else {
			g2.setColor(BORDER_COLOR);
			g2.drawRect(x+235, y+180, 120, 40); // gear low			
		}
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
		g2.drawString("INTAKE", x+126, y+206);
		g2.drawString("BALL IN", x+256, y+206);
		
		
		if (isAutoAngle) {
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(x+95, y+430, 120, 40); //high lit gear high
			g2.setColor(BORDER_COLOR);
			g2.drawRect(x+235, y+430, 120, 40); // gear low			
		} else {
			g2.setColor(BORDER_COLOR);
			g2.drawRect(x+95, y+430, 120, 40); // gear high
			g2.setColor(BUTTON_COLOR);
			g2.fillRect(x+235, y+430, 120, 40); //low			
		}
		
		g2.setColor(TEXT_COLOR);
		g2.setFont(smallFont);
		g2.drawString("AUTO", x+126, y+456);
		g2.drawString("MANUAL", x+256, y+456);
	    g2.setFont(boldFont);
		g2.drawString("ARM CONTROL", x+124, y+420);
		g2.setColor(DESC_TEXT_COLOR);
	    g2.setFont(descFont);
		g2.drawString("Operator Y", x+124, y+395);

						
	}
		
	public void setSmartDashboard(NetworkTable s) {
		this.smartDashboard = s;
	}
	
	
	public void paint (Graphics g) {
	    
	    drawLogos(g);
	    
		drawGearButton(g);
		drawAutoHeadingButton(g);
		drawDriveModeButton(g);
		drawHeading(g);
		drawAutonButton(g);
		drawDiagnosticsView(g);								

//		drawCameraViewButton(g);
	}

	private void drawLogos(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    
		g2.setColor(SECTION_BG_COLOR);
		g2.fillRect(20, 40, 450, 80); //top left logo 
		g2.fillRect(1130, 40, 450, 80); //top right logo

	    g2.setFont(boldFont);
		g2.setColor(TEXT_COLOR);
		g2.drawString("5460", 215, 90);
		g2.drawString("STRIKE ZONE", 1255, 90);
		
	}
	
	public void setFrame(DashboardMain dashboardMain) {
		this.dashboardMain = dashboardMain;		
	}

}

	

//g2.drawString("MOTORS", 1330, 180);
//g2.drawString("STATES", 1470, 180);
/*g2.drawString("MOTOR 1", 1150, 240);
g2.drawString("MOTOR 2", 1150, 270);
g2.drawString("MOTOR 3", 1150, 300);
g2.drawString("MOTOR 4", 1150, 330);
g2.drawString("MOTOR 5", 1150, 360);
g2.drawString("MOTOR 6", 1150, 390);
g2.drawString("MOTOR 7", 1150, 420);
g2.drawString("MOTOR 8", 1150, 450);
g2.drawString("MOTOR 9", 1150, 480);
g2.drawString("MOTOR 10", 1150, 510);
*/
//g2.drawString("SHOOTER", 1170, 260);
//g2.drawString("TARGET", 1316, 260);
//g2.drawString("PICKUP", 1462, 260);
/*g2.drawRect(1310, 225, 260, 15); // motor1
g2.drawRect(1310, 255, 260, 15); // motor2
g2.drawRect(1310, 285, 260, 15); // motor3
g2.drawRect(1310, 315, 260, 15); // motor4
g2.drawRect(1310, 345, 260, 15); // motor5
g2.drawRect(1310, 375, 260, 15); // motor6
g2.drawRect(1310, 405, 260, 15); // motor7
g2.drawRect(1310, 435, 260, 15); // motor8
g2.drawRect(1310, 465, 260, 15); // motor9
g2.drawRect(1310, 495, 260, 15); // motor10
*/


//g2.drawRect(1150, 230, 126, 40); // shooter
//g2.drawRect(1296, 230, 126, 40); // target
//g2.drawRect(1442, 230, 126, 40); //pickup

//g2.setColor(LINE_COLOR);
//g2.drawLine(1130, 210, 1580, 210);
//g2.drawLine(1130, 290, 1580, 290);
