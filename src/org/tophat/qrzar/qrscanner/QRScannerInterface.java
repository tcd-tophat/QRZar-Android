package org.tophat.qrzar.qrscanner;

import android.graphics.Rect;
import android.os.Handler;

public interface QRScannerInterface {
	
	/**
	 * Allows the QRScanner to obtain the Handler to receive the Message objects.
	 * @return The Handler to receive the QRScanner's message objects.
	 */
	public Handler getHandler();
	
	/**
	 * Allows the QRScanner to obtain the Rect that the user is directly previewing the scan through, relative to the Surface View.
	 * @return The preview Rect
	 */
	public Rect getPreviewScreenRect();
	
	/**
	 * Allows the QRScanner to obtain the devices rotation. Should return the result of a call to getRotation() (http://developer.android.com/reference/android/view/Display.html#getRotation())
	 * @return The Screen's Rotation
	 */
	public int getScreenRotation();
}
