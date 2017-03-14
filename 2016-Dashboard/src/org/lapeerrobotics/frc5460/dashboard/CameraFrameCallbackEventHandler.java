package org.lapeerrobotics.frc5460.dashboard;

import java.awt.image.BufferedImage;

public interface CameraFrameCallbackEventHandler {
	void handleFrameEvent(int errorCd, BufferedImage frame, int cnt, String errorMessage);
}
