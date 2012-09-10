package org.tophat.qrzar.activities.mainactivity;

import java.util.ArrayList;
import java.util.Map;

import org.tophat.QRzar.models.Player;
import org.tophat.android.exceptions.HttpException;
import org.tophat.android.exceptions.NoInternetConnection;
import org.tophat.qrzar.R;
import org.tophat.qrzar.activities.gameplayactivity.GamePlayActivity;
import org.tophat.qrzar.qrscanner.QRScanner;
import org.tophat.qrzar.qrscanner.QRScannerInterface;
import org.tophat.qrzar.sdkinterface.SDKInterface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements QRScannerInterface{

	private static final String TAG = MainActivity.class.getSimpleName();
	
	private QRScanner mQRScanner;
	private Handler mHandler;
	public static SDKInterface sdk;
	
	public static Player p;
	
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
	    	
	    	sdk.setTShirtCode(result);
	    	
	    	new AnonymousUserTask().execute();
	    	
    	}else if(sdk.validToProcessGameCode(result)){
    		
    		this.stopScanForGame();
	    	
    		sdk.setGameCode(SDKInterface.decodeGameCode(result));
    		
    		new JoinGameTask().execute();
    	}
    }
    
    private class AnonymousUserTask extends AsyncTask<Void, Boolean, Boolean> 
	{	
		private ProgressDialog dialog;
		
		@Override    
		protected void onPreExecute() 
		{       
		    super.onPreExecute();
		    
		    dialog = ProgressDialog.show(MainActivity.this, "", 
	                "Loading Server Details. Please wait...", true);
		}
		    
		protected Boolean doInBackground(Void... details) 
		{
			return MainActivity.this.setAnonymousToken();
		}

	     protected void onPostExecute(Boolean data)
	     {
	    	this.dialog.cancel();
	    	
	    	if (data)
	    	{
	 			Log.i(TAG, "T-Shirt code loaded");
	    	}
	     }
	}
    
    private class JoinGameTask extends AsyncTask<Void, Boolean, Boolean> 
	{	
		private ProgressDialog dialog;
		
		@Override    
		protected void onPreExecute() 
		{       
		    super.onPreExecute();
		    
		    dialog = ProgressDialog.show(MainActivity.this, "", 
	                "Join Game. Please wait...", true);
		}
		    
		protected Boolean doInBackground(Void... details) 
		{
			return sdk.joinGame();
		}

	     protected void onPostExecute(Boolean data)
	     {
	    	this.dialog.cancel();
	    	
	    	if (data)
	    	{
	 			Intent intent = new Intent(MainActivity.this, GamePlayActivity.class);
				
	 			MainActivity.p = sdk.getPlayer();
	 			startActivity(intent);
	 			Log.i(TAG, "Game Joined");
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
