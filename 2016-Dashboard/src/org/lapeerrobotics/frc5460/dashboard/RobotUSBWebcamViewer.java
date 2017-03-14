package org.lapeerrobotics.frc5460.dashboard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.net.Socket;
import java.net.ConnectException;
import java.util.Arrays;

/**
 * SmartDashboard extension for viewing a MJPEG stream from the robot,
 * typically forwarded from a USB webcam, or images processed by a user
 * program. This is mostly compatible with the LabVIEW dashboard in
 * "HW Compression" mode.
 *
 * @author Tom Clark
 */
public class RobotUSBWebcamViewer implements Runnable {

    public static final String NAME = "USB Webcam Viewer";

    private final static int FPS = 30;

//    private final static int PORT = 1180;
    private final static byte[] MAGIC_NUMBERS = { 0x01, 0x00, 0x00, 0x00 };
    public final static int SIZE_640x480 = 0;
    public final static int SIZE_320x240 = 1;
    public final static int SIZE_160x120 = 2;
    private final static int HW_COMPRESSION = -1;

    private BufferedImage frame = null;
    private final Object frameMutex = new Object();;
    private String errorMessage = null;

    private Socket socket;
    private Thread thread;

	private String host;
	private int port;
	private int size;
	private int fps;
	
	private int cnt;
	static final int huffman_table_int[] = {
	        255, 196, 1, 162, 0, 0, 1, 5, 1, 1, 
	        1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 
	        0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 
	        9, 10, 11, 1, 0, 3, 1, 1, 1, 1, 
	        1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 
	        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
	        10, 11, 16, 0, 2, 1, 3, 3, 2, 4, 
	        3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 
	        2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 
	        6, 19, 81, 97, 7, 34, 113, 20, 50, 129, 
	        145, 161, 8, 35, 66, 177, 193, 21, 82, 209, 
	        240, 36, 51, 98, 114, 130, 9, 10, 22, 23, 
	        24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 
	        53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 
	        71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 
	        89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 
	        115, 116, 117, 118, 119, 120, 121, 122, 131, 132, 
	        133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 
	        150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 
	        167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 
	        184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 
	        201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 
	        218, 225, 226, 227, 228, 229, 230, 231, 232, 233, 
	        234, 241, 242, 243, 244, 245, 246, 247, 248, 249, 
	        250, 17, 0, 2, 1, 2, 4, 4, 3, 4, 
	        7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 
	        2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 
	        81, 7, 97, 113, 19, 34, 50, 129, 8, 20, 
	        66, 145, 161, 177, 193, 9, 35, 51, 82, 240, 
	        21, 98, 114, 209, 10, 22, 36, 52, 225, 37, 
	        241, 23, 24, 25, 26, 38, 39, 40, 41, 42, 
	        53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 
	        71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 
	        89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 
	        115, 116, 117, 118, 119, 120, 121, 122, 130, 131, 
	        132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 
	        149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 
	        166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 
	        183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 
	        200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 
	        217, 218, 226, 227, 228, 229, 230, 231, 232, 233, 
	        234, 242, 243, 244, 245, 246, 247, 248, 249, 250
	    };
	byte[] huffman_table;
	
	private CameraFrameCallbackEventHandler frameCallbackHandler;
	
    public RobotUSBWebcamViewer(String host, int port, int size, int fps) {
    	this.host = host;
    	this.port = port;
    	this.size = size;
    	this.fps = fps;
    	this.cnt = 0;
    	huffman_table = new byte[huffman_table_int.length];
        for(int i = 0; i < huffman_table.length; i++)
            huffman_table[i] = (byte)huffman_table_int[i];
    }
    
    public void registerFrameCallbackHandler(CameraFrameCallbackEventHandler handler) {
    	this.frameCallbackHandler = handler;
    }
    
    public void start() {

        this.thread = new Thread(this);
        this.thread.start();

        ImageIO.setUseCache(false);
    }

    public void stop() {
        this.thread.stop();

        if (this.socket != null) {
            try {
                this.socket.close();
            }
            catch (IOException e) {}
        }
    }

    public BufferedImage getFrame() {
        synchronized (this.frameMutex) {
        	return this.frame;
        }
    }
    
    public int getCnt() {
    	return this.cnt;
    }
    
    public String getErrorMessage() {
    	return this.errorMessage;
    }
    
    /**
     * Continuously request and receive frames from the roboRIO
     */
    public void run() {
    	int errorCd = 0;
        while(true) {
            try {
            	errorCd = 0;
            	this.cnt++;	
                this.socket = new Socket(this.host, this.port);
                DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());

                /* Send the request */
                outputStream.writeInt(this.fps);
                outputStream.writeInt(HW_COMPRESSION);
                outputStream.writeInt(this.size);
                outputStream.flush();

                /* Get the response from the robot */
                while (!Thread.interrupted()) {
                    /* Each frame has a header with 4 magic bytes and the number of bytes in the image */
                    byte[] magic = new byte[4];
                    inputStream.readFully(magic);
                    int size = inputStream.readInt();

                    assert Arrays.equals(magic, MAGIC_NUMBERS);


                    /* Get the image data itself, and make sure that it's a valid JPEG image (it starts with
                     * [0xff,0xd8] and ends with [0xff,0xd9] */
                    byte[] data = new byte[size+huffman_table.length];
                    inputStream.readFully(data, 0, size);

                    assert ((size >= 4) && ((data[0] & 0xFF) == 255) && ((data[1] & 0xFF) == 216) && ((data[size-2] & 0xFF) == 255) && ((data[size-1] & 0xFF) == 217));
                    
                    int pos = 2;
                    boolean has_dht = false;
                    while(!has_dht) {
                    	assert (pos + 4 <= size);
                    	assert ((data[pos] & 0xFF) == 255);
                    	if ((data[(pos+1)] & 0xFF) == 196) {
                    		has_dht = true;
                    	} else {
                    		if ((data[(pos+1)] & 0xFF) == 218) {
                    			break;
                    		}
                    	}
                    	int marker_size = ((data[(pos+2)] & 0xFF) << 8) + (data[(pos+3)] & 0xFF); 
                    	pos += marker_size + 2;
                    }
                    if(!has_dht) {
                    	System.arraycopy(data, pos, data, pos+huffman_table.length,  size-pos);
                    	System.arraycopy(huffman_table, 0, data, pos, huffman_table.length);
                    	size += huffman_table.length;
                    }
                    
                    /* Decode the data and re-paint the component with the new frame */
                    synchronized (this.frameMutex) {
                        if (this.frame != null) {
                            this.frame.flush();
                        }

                        this.frame = ImageIO.read(new ByteArrayInputStream(data));
                        this.errorMessage = null; 
                   		frameCallbackHandler.handleFrameEvent(errorCd, frame, cnt, errorMessage);
                    }
                    // frame updated, callback listener

                } 
            } catch (ConnectException e) {
            	errorCd = 500;
                if (this.errorMessage == null) {
                    this.errorMessage = e.getMessage();
                }
            } catch (EOFException e) {
            	errorCd = 600;
                if (this.errorMessage == null) {
                    this.errorMessage = "Robot stopped returning images";
                }
            } catch (IOException e) {
            	errorCd = 700;
                if (this.errorMessage == null) {
                    this.errorMessage = e.getMessage();
                }
            } finally {
           		frameCallbackHandler.handleFrameEvent(errorCd, null, cnt, errorMessage);
                if (this.socket != null) {
                    try {
                        this.socket.close();
                    } catch (IOException e) {
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                }
            }            
        }
    }


    /**
     * Draw the latest image to the screen, blocking if one is being received,
     * or showing an error message if there's some problem with the image.
     *
     * @param g the <code>Graphics</code> context in which to paint
     */

/*    
    protected void paintComponent(Graphics g) {
        int imageX = 0,
            imageY = 0,
            imageWidth = this.getWidth(),
            imageHeight = this.getHeight();

        synchronized (this.frameMutex) {
            if (this.frame != null) {
                float thisAspectRatio = (float) this.getWidth() / this.getHeight();
                float imageAspectRatio = (float) this.frame.getWidth(null) / this.frame.getHeight(null);

                if (imageAspectRatio < thisAspectRatio) {
                    imageWidth = (int) (this.getHeight() * imageAspectRatio);
                    imageX = (this.getWidth() - imageWidth) / 2;
                } else {
                    imageHeight = (int) (this.getWidth() / imageAspectRatio);
                    imageY = (this.getHeight() - imageHeight) / 2;
                }

                g.drawImage(this.frame, imageX, imageY, imageWidth, imageHeight, null, null);
            }

            if (this.errorMessage != null) {
                g.setClip(imageX, imageY, imageWidth, imageHeight);

                g.setColor(Color.pink);
                g.fillRect(imageX, imageY + imageHeight - 18, imageWidth, 18);
                g.setColor(Color.black);

                Font font = g.getFont();

                g.setFont(font.deriveFont(Font.BOLD));
                g.drawString("Error: ", imageX + 2, imageY + imageHeight - 6);
                g.setFont(font);
                g.drawString(this.errorMessage, imageX + 40, imageY + imageHeight - 6);
            }
        }
    }
    */
}