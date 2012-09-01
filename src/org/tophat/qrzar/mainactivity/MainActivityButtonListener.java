package org.tophat.qrzar.mainactivity;


import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivityButtonListener implements OnTouchListener {

	MainActivity mActivity;
	
	public MainActivityButtonListener(MainActivity _activity) {
		mActivity = _activity;
	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()){
		case MainActivityConstants.PLAY_BUTTON:
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mActivity.startScanForGame();
				break;
			case MotionEvent.ACTION_UP:
				mActivity.stopScanForGame();
				break;
			}
			break;
		case MainActivityConstants.INFO_BUTTON:
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				
				break;
			case MotionEvent.ACTION_UP:
				
				break;
			}
			break;
		case MainActivityConstants.RANK_BUTTON:
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				
				break;
			case MotionEvent.ACTION_UP:
				
				break;
			}
			break;
		}
		return false;
	}

}
