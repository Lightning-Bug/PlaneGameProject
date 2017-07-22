package comnitt.boston.planegameproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public class MainActivity extends AppCompatActivity
{
    private ImageButton buttonPlay,sound;
    private ImageButton buttonScore;
    private ImageButton gamerules,git;
    public Button database;
         int x,s=0;


        DatabaseHelper myDb;
        //GameView score;
        private Animation myAnim,myRotate,zoomin;

        private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       myDb = new DatabaseHelper(this);


         mp = MediaPlayer.create(this,R.raw.theme);
         mp.start();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonScore = (ImageButton) findViewById(R.id.buttonScore);
        gamerules = (ImageButton) findViewById(R.id.imageButton2);
        sound = (ImageButton) findViewById(R.id.sound);
        git = (ImageButton) findViewById(R.id.imageButton);
        database = (Button) findViewById(R.id.button);

        myAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        myRotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
        zoomin = AnimationUtils.loadAnimation(this,R.anim.scale);

        buttonPlay.setAnimation(myAnim);
        buttonScore.setAnimation(myAnim);
        gamerules.setAnimation(myRotate);
        database.setAnimation(myAnim);
        sound.setAnimation(myRotate);



       // x = getIntent().getIntExtra("score",0);
      //  boolean isInserted = myDb.insertData(x);
      //  if(isInserted == true)
       //     Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_SHORT).show();
     //   else
       //     Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_SHORT).show();

    }



       //int x = getIntent().getIntExtra("score",0);
       // boolean isInserted = myDb.insertData(x);

     public void GotomyGit(View v)
    {
        Uri uriUrl = Uri.parse("https://github.com/Lightning-Bug/PlaneGameProject");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


     public void highscores(View v)
     {
         v.setAnimation(zoomin);
        // int x = getIntent().getIntExtra("score",0);
            //boolean isInserted = myDb.insertData(x);
            Cursor res = myDb.getAllData();
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Id :" + res.getString(0) + "\n");
                buffer.append("Score:" + res.getString(1) + "\n");
                // Show all data

            }

            showMessage("Data", buffer.toString());
        }

        public void showMessage(String title,String Message)
        {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setTitle(title);
                    builder.setMessage(Message);
                    builder.show();
                }





        protected void onPause() {
            super.onPause();
            mp.stop();
        }


        protected void onResume() {
        super.onResume();
        mp.start();
       }


    public void onClick(View v)
    {
        v.startAnimation(zoomin);
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
            //startActivity(new Intent(MainActivity.this, GameActivity.class));
        }

    public void History(View v) {

        v.startAnimation(zoomin);
        Intent intent = new Intent(this, HighScore.class);
        startActivity(intent);
            //startActivity(new Intent(MainActivity.this, HighScore.class));
        }

     public void Gamerules(View V) {
         Intent intent = new Intent(this, GameRules.class);
         startActivity(intent);
     }

    public void soundcontrol(View V) {
        s++;
        if(s%2 != 0) {
            mp.pause();
            sound.setImageResource(R.drawable.nosound);
        }
        else {
            mp.start();
            sound.setImageResource(R.drawable.sound);
        }


}




    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        GameView.stopMusic();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
