package org.tophat.qrzar.mainactivity;

import org.tophat.android.exceptions.HttpException;
import org.tophat.android.exceptions.NoInternetConnection;
import org.tophat.qrzar.R;
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
import android.widget.Toast;

public class MainActivity extends Activity implements QRScannerInterface{

	QRScanner mQRScanner;
	Handler mHandler;
	SDKInterface sdk;
	
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
    	
    	this.stopScanForGame();
    	
    	Toast t =  Toast.makeText(this.getApplicationContext(), "Successfully joined game", Toast.LENGTH_LONG);
    	
    	try 
    	{
			sdk.anonymous_connect();
    	}
    	catch (NoInternetConnection e)
    	{
    		// Do something special when there is no internet connection / server is unreachable ??
    		t.setText(e.getMessage());
    	}
    	catch (HttpException e) 
    	{
    		//This will show whatever error the user encounters via toast on the users screen.
    		t.setText(e.getMessage());
		}
    	
    	t.show();
    	
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
