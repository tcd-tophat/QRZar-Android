package org.tophat.qrzar.activities.gameplayactivity;

import org.tophat.qrzar.qrscanner.QRScannerConstants;

import com.google.zxing.Result;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GamePlayActivityMessageHandler extends Handler {

private static final String TAG = GamePlayActivityMessageHandler.class.getSimpleName();
	
	private GamePlayActivity mActivity;
	
	public GamePlayActivityMessageHandler(GamePlayActivity _activity){
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
