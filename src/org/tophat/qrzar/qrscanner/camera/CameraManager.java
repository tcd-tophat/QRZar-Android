package org.tophat.qrzar.qrscanner.camera;

import java.io.IOException;

import org.tophat.qrzar.qrscanner.QRScannerInterface;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraManager {
	
	private Camera mCamera;
	private static final String TAG = CameraManager.class.getSimpleName();
	
	@SuppressLint("NewApi")
	public CameraManager(SurfaceHolder _surfaceHolder, QRScannerInterface _activity) throws IOException{
		
		if(Build.VERSION.SDK_INT>8){
			mCamera = Camera.open(0);
			Log.i(TAG, "Only devices Gingerbread up should call this.");
		}else{
			mCamera = Camera.open();
		}
		
	
		int degreeOrientation = _activity.getScreenRotation()*90;

		Camera.Parameters parameters = mCamera.getParameters();
		
		try{
			parameters.setPictureSize(_surfaceHolder.getSurfaceFrame().width(),_surfaceHolder.getSurfaceFrame().height());
			mCamera.setParameters(parameters);
		}catch(RuntimeException e){

		}
		
		mCamera.setDisplayOrientation((450-degreeOrientation)%360);
		
		
		mCamera.setPreviewDisplay(_surfaceHolder);
		
	}
	
	public void startPreview(){
		mCamera.startPreview();
	}
	
	public void close(){

		mCamera.stopPreview();
		mCamera.release();
	}
	
	public void requestPreviewCallback(PreviewCallback _callback){
		mCamera.setPreviewCallback(_callback);
	}
	
	public void cancelPreviewCallback(){
		mCamera.setPreviewCallback(null);
	}
	
}
