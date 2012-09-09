package org.tophat.qrzar.activities.mainactivity;

import org.tophat.android.exceptions.HttpException;
import org.tophat.android.exceptions.NoInternetConnection;
import org.tophat.qrzar.R;
import org.tophat.qrzar.activities.gameplayactivity.GamePlayActivity;
import org.tophat.qrzar.qrscanner.QRScanner;
import org.tophat.qrzar.qrscanner.QRScannerInterface;
import org.tophat.qrzar.sdkinterface.SDKInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements QRScannerInterface{

	private static final String TAG = MainActivity.class.getSimpleName();
	
	private QRScanner mQRScanner;
	private Handler mHandler;
	private SDKInterface sdk;
	
	/**
	 * Activity life cycle methods
	 */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrzar_main);
        
        mHandler = new MainActivityMessageHandler(this);
        sdk = new SDKInterface();
        
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
        getMenuInflater().inflate(R.menu.activity_qrzar_main, menu);
        return true;
    }
    
    /**
     * Game control methods
     */
    
    public void startScanForGame(){
    	mQRScanner.requestPreviewCallback();
    	findViewById(R.id.logo).setVisibility(View.INVISIBLE);
    }
    
    public void stopScanForGame(){
    	mQRScanner.cancelPreviewCallback();
    	findViewById(R.id.logo).setVisibility(View.VISIBLE);
    }
    
    public void hasScannedResult(String result){
    	
    	if(sdk.validToProcessTShirt(result)){
    		
	    	this.stopScanForGame();
	    	
	    	if(setAnonymousToken()){
    			sdk.setTShirtCode(result);
    		}
	    	
    	}else if(sdk.validToProcessGameCode(result)){
    		
    		this.stopScanForGame();
	    	
    		sdk.setGameCode(SDKInterface.decodeGameCode(result));
    		
    		if(sdk.joinGame()){
    			Log.i(TAG, "Game Joined");
    			Intent intent = new Intent(this, GamePlayActivity.class);
    			intent.putExtra("player",sdk.getPlayer());
    			startActivity(intent);
    		
    		}
    	}
    }
    
    
    /**
     * Private methods
     */
    
    private void addListenerToButtons(){
    	MainActivityButtonListener listener = new MainActivityButtonListener(this);
    	
    	Button playButton = (Button)findViewById(R.id.join_button);
    	playButton.setId(MainActivityConstants.PLAY_BUTTON);
    	playButton.setOnTouchListener(listener);
    	
    	Button infoButton = (Button)findViewById(R.id.info_button);
    	infoButton.setId(MainActivityConstants.INFO_BUTTON);
    	infoButton.setOnTouchListener(listener);
    	
    	Button rankButton = (Button)findViewById(R.id.rank_button);
    	rankButton.setId(MainActivityConstants.RANK_BUTTON);
    	rankButton.setOnTouchListener(listener);
    }
    
    
	    /**
	     *  Private interface helper methods
	     */
    
    
    private boolean setAnonymousToken(){

    	boolean didSucceed = false;
    	try 
    	{
			sdk.anonymous_connect();
			Log.i(TAG, "Successfully attained api token.");
			didSucceed = true;
    	}
    	catch (NoInternetConnection e)
    	{
    		// Do something special when there is no internet connection / server is unreachable ??
    		Log.i(TAG,e.getMessage());
    	}
    	catch (HttpException e) 
    	{
    		//This will show whatever error the user encounters via toast on the users screen.
    		Log.i(TAG,e.getMessage());
		}
    	

    	return didSucceed;
    }
    
    
    /**
     * QRScanner Interface Methods
     */
    
    @Override
    public Rect getPreviewScreenRect(){
    	View preview = findViewById(R.id.previewView);
    	return new Rect(preview.getLeft(),preview.getTop(),preview.getWidth()+preview.getLeft(),preview.getHeight()+preview.getTop());
    }
    
    
    @Override
    public Handler getHandler(){
    	return mHandler;
    }



	@Override
	public int getScreenRotation() {
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getRotation();
	}
    
}
