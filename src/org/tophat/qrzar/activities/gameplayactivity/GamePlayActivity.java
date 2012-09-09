package org.tophat.qrzar.activities.gameplayactivity;

import org.tophat.qrzar.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GamePlayActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game_play, menu);
        return true;
    }
}
