package org.tophat.qrzar.activities.gameplayactivity;

import android.os.CountDownTimer;

public class GamePlayActivityCountDownTimer extends CountDownTimer {

	private GamePlayActivity mGamePlayActivity;

	public GamePlayActivityCountDownTimer(GamePlayActivity activity, long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		mGamePlayActivity = activity;
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTick(long millisUntilFinished) {
		int secondsToGo = (int)(millisUntilFinished / 1000);
        mGamePlayActivity.setTimer(String.format("%02d:%02d", secondsToGo/60,secondsToGo%60));
        
        new Thread(new GamePlayActivityDeadCheckerRunnable(mGamePlayActivity)).run();

	}

}
