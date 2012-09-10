package org.tophat.qrzar.activities.gameplayactivity;

import java.util.HashMap;

import org.tophat.android.exceptions.HttpException;
import org.tophat.qrzar.R;
import org.tophat.qrzar.activities.mainactivity.MainActivity;
import org.tophat.qrzar.qrscanner.QRScanner;
import org.tophat.qrzar.qrscanner.QRScannerInterface;
import org.tophat.qrzar.sdkinterface.SDKInterface;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GamePlayActivity extends Activity implements QRScannerInterface {

	private QRScanner mQRScanner;
	private GamePlayActivityMessageHandler mHandler;
	private SDKInterface sdk;
	private TextView mTeam1Score, mTeam2Score, mTimer;
	private boolean mDead;
	
	private GamePlayActivityCountDownTimer mCountdownTimer;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        
        mHandler = new GamePlayActivityMessageHandler(this);
        
        //Bundle b = getIntent().getExtras();
        //org.tophat.QRzar.models.Player p = (org.tophat.QRzar.models.Player)b.getParcelable("player");
        sdk = MainActivity.sdk;
        
        mTeam1Score = (TextView)findViewById(R.id.team1Score);
        mTeam2Score = (TextView)findViewById(R.id.team2Score);
        mTimer = (TextView)findViewById(R.id.timer);
        
        mDead = false;
        
        updateScoresAndTimer();
        
        addListenerToButtons();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	mQRScanner = new QRScanner(findViewById(R.id.cameraSurface));
    }

    @Override
    public void onPause(){
    	super.onPause();
    	mQRScanner.close();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game_play, menu);
        return true;
    }
    
    
    /**
     * Game control methods
     */
    
    public void startScan(){
    	mQRScanner.requestPreviewCallback();
    }
    
    public void stopScan(){
    	mQRScanner.cancelPreviewCallback();
    }
    
    public void hasScannedResult(String result)
    {
    	try 
    	{
			sdk.kill(sdk.getPlayer(), result);
		} 
    	catch (HttpException e) 
    	{
			e.printStackTrace();
		}	
    }
    
    public void setTimer(String timeToSet){
    	mTimer.setText(timeToSet);
    }
    
    public void checkIfDead(){
    	
    }
    
    /**
     * Private methods
     */
    
    private void addListenerToButtons(){
    	GamePlayActivityButtonListener listener = new GamePlayActivityButtonListener(this);
    	
    	Button shootButton = (Button)findViewById(R.id.shoot_button);
    	shootButton.setId(GamePlayActivityConstants.SHOOT_BUTTON);
    	shootButton.setOnTouchListener(listener);
    	
    }
    
    private void updateScoresAndTimer(){
    	
    	HashMap<String,Integer>map = sdk.getTeamScoresAndRemainingTime();
        
        mTeam1Score.setText(Integer.toString(map.get("team1Score")));
        mTeam2Score.setText(Integer.toString(map.get("team2Score")));
        
        if(mCountdownTimer!=null)
        	mCountdownTimer.cancel();
        
        mCountdownTimer = new GamePlayActivityCountDownTimer(this, map.get("timer")*1000, 1000);
        
        mCountdownTimer.start();
    }
    /**
     * Interface required methods.
     */

	@Override
	public Handler getHandler() {
		
		return mHandler;
	}

	@Override
	public Rect getPreviewScreenRect() {
		View preview = findViewById(R.id.previewView);
    	return new Rect(preview.getLeft(),preview.getTop(),preview.getWidth()+preview.getLeft(),preview.getHeight()+preview.getTop());
	}

	@Override
	public int getScreenRotation() {
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getRotation();
	}
}
