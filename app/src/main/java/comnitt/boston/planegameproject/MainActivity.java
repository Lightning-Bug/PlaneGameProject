package comnitt.boston.planegameproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton buttonPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mp = MediaPlayer.create(this,R.raw.theme);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        mp.start();
    }

    @Override
    public void onClick(View v) {


        startActivity(new Intent(this, GameActivity.class));
    }


    //protected void onPause() {
      //  super.onPause();
        //mp.stop();
    //}
}
