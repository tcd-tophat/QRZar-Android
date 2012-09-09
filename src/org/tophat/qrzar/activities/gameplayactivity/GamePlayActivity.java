package org.tophat.qrzar.activities.gameplayactivity;

import org.tophat.qrzar.R;
import org.tophat.qrzar.qrscanner.QRScanner;
import org.tophat.qrzar.qrscanner.QRScannerInterface;
import org.tophat.qrzar.sdkinterface.SDKInterface;
import org.tophat.QRzar.models.Player;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class GamePlayActivity extends Activity implements QRScannerInterface {

	private QRScanner mQRScanner;
	private GamePlayActivityMessageHandler mHandler;
	private SDKInterface sdk;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        
        mHandler = new GamePlayActivityMessageHandler(this);
        sdk = new SDKInterface((Player)getIntent().getParcelableExtra("player"));
        
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
    
    public void hasScannedResult(String result){
    	
    	
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
