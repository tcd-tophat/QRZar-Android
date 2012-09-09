package org.tophat.qrzar.activities.mainactivity;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.Result;

import org.tophat.qrzar.qrscanner.QRScannerConstants;

public class MainActivityMessageHandler extends Handler {
	
	private static final String TAG = MainActivityMessageHandler.class.getSimpleName();
	
	private MainActivity mActivity;
	
	public MainActivityMessageHandler(MainActivity _activity){
		mActivity = _activity;
	}

	@Override
	public void handleMessage(Message message){
		
		switch(message.what){
		
		case QRScannerConstants.DECODE_SUCCEEDED:
			Result result = (Result)message.obj;
			Log.i(TAG, "Decode Successful:"+result.getText());
			mActivity.hasScannedResult(result.getText());
			break;
			
		case QRScannerConstants.DECODE_FAILED:
			Log.i(TAG, "Decode Unsuccessful");
			break;
		}
		
	}
	

	

}
