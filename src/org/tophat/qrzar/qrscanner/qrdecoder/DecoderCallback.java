package org.tophat.qrzar.qrscanner.qrdecoder;

import org.tophat.qrzar.qrscanner.QRScannerInterface;
import org.tophat.qrzar.qrscanner.QRScannerConstants;
import org.tophat.qrzar.qrscanner.camera.CameraSurface;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class DecoderCallback implements PreviewCallback {
	
	
	private static final String TAG = DecoderCallback.class.getSimpleName();
	private QRCodeReader mQRCodeReader;
	private QRScannerInterface mActivity;
	private CameraSurface mCameraSurface;
	
	public DecoderCallback(QRScannerInterface _activity, CameraSurface _cameraSurface){
		mActivity = _activity;
		mCameraSurface = _cameraSurface;
		mQRCodeReader = new QRCodeReader();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Parameters params = camera.getParameters();
		decode(data,params.getPreviewSize().width,camera.getParameters().getPreviewSize().height);
	}
	
	
	private void decode(byte[] data, int width, int height) {
		
	    Result rawResult = null;
	    Rect preview = mActivity.getPreviewScreenRect();
	    
	    preview = alterPreviewRectForScalingDistortionAndRotation(preview, width, height);
		
		PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, preview.left, preview.top, preview.width(), preview.height(), false);

	    if (source != null) {
	      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	      try {
	        rawResult = mQRCodeReader.decode(bitmap);
	      } catch (ReaderException re) {
	      } finally {
	        mQRCodeReader.reset();
	      }
	    }

	    Handler handler = mActivity.getHandler();
	    if (rawResult != null) {

	      if (handler != null) {
	        Message message = Message.obtain(handler, QRScannerConstants.DECODE_SUCCEEDED, rawResult);
	        message.sendToTarget();
	      }
	    } else {
	      if (handler != null) {
	        Message message = Message.obtain(handler, QRScannerConstants.DECODE_FAILED);
	        message.sendToTarget();
	      }
	    }
	  }
	
	private Rect alterPreviewRectForScalingDistortionAndRotation(Rect preview, int width, int height){
		
		boolean landscape = mActivity.getScreenRotation()%2!=0;
	    if(landscape){
	    	
	    }else{
	    	
	    	preview.set(new Rect(preview.top, preview.left, preview.height(), preview.width()));
	    	
	    	int scaledHeight = height; // Width of screen
	    	
	    	int scaledWidth = preview.width()*height/preview.height(); // Actual size of preview relative to screen
	    	
	    	
	    	double cameraAspectRatio = (double)width/(double)height;
	    	Log.i(TAG, ""+cameraAspectRatio);
	    	double previewAspectRatio = (double)mCameraSurface.getCameraRect().height()/(double)mCameraSurface.getCameraRect().width();
	    	Log.i(TAG, ""+previewAspectRatio);
			double aspectDistortionFactor = cameraAspectRatio/previewAspectRatio;
	    	Log.i(TAG, ""+aspectDistortionFactor);
	    	scaledWidth *= aspectDistortionFactor;
	    	
	    	preview.set(new Rect(preview.left,preview.top,scaledWidth,scaledHeight));
	    }
	    return preview;
	}

}
