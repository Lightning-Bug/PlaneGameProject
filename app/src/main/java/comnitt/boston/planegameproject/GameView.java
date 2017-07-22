package comnitt.boston.planegameproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;



public class GameView extends SurfaceView implements Runnable
{

   DatabaseHelper myDb;
    int k;
    Context context;               //context ,transition from GameAvtivity to MainActivity
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;

    static MediaPlayer gameOnsound,heartt,level;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy enemies;
    private heart pyaar;

    private purple purp;
    private Friend friend;
    private Boom boom; //blast
    private savefriend save;
    //the score holder
    int score;
    int highScore[] = new int[4];
    SharedPreferences sharedPreferences;
    //private int enemyCount = 3;
    private ArrayList<Star> stars = new ArrayList<Star>();
    int screenX;
    int countMisses,friendcoll=0,hearts=0;
    boolean flag ;
    private boolean isGameOver ;
    public GameView(Context context, int screenX, int screenY)
    {
        super(context);
        this.context = context;

        myDb = new DatabaseHelper(this.context);

        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);
        heartt= MediaPlayer.create(context,R.raw.heratt);
        level= MediaPlayer.create(context,R.raw.levelincreased);

        gameOnsound.start();


        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;                           //adding 100 stars
        for (int i = 0; i < starNums; i++) {
            Star s  = new Star(screenX, screenY);
            stars.add(s);
        }
        // 3enemies = new Enemy[enemyCount];
        //for(int i=0; i<enemyCount; i++){
           // enemies[i] = new Enemy(context, screenX, screenY);
       // }
        this.screenX = screenX;
        countMisses = 0;
        isGameOver = false;
        enemies = new Enemy(context, screenX, screenY);
        pyaar = new heart(context,screenX,screenY);
        boom= new Boom(context);
        save = new savefriend(context);
        purp = new purple(context);

        friend = new Friend(context, screenX, screenY);
        score = 0;
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);
        //initially all zero
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
    }

    @Override
    public void run()
    {
        while (playing) {
            update();
            draw();
            control();
        }
    }

 public int getscore()
   {

         return score;
    }


    private void update()
    {
        score++;
        player.update();

        boom.setX(-250);                           //setting boom outside the screen
        boom.setY(-250);

        save.setX(-550);                           //setting boom outside the screen
        save.setY(-550);

        purp.setX(-550);
        purp.setY(-550);

        for (Star s : stars)
        {    s.update(player.getSpeed());
        }

        if(enemies.getX()==screenX)
        {
            flag = true;
        }

        /*for (int i = 0; i < enemyCount; i++)
        { //if collision occurs
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                //displaying boom at that location
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());

                enemies[i].setX(-200);
            }
            */


        enemies.update(player.getSpeed());
        friend.update(player.getSpeed());
        pyaar.update(player.getSpeed());


        if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision()))  //detecting boom  //if collision happens

        {
            //gameOnsound.pause();
            if(score>1000)
                level.pause();
            else
                gameOnsound.pause();
            killedEnemysound.start();
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());
            enemies.setX(-200);  //playing sound

            if(score>1000)
                level.start();
            else
                gameOnsound.start();
            }

            else{
                //if the enemy has just entered
                if(flag){
                    //if player's x cd > enemies x cd , enemy has passed across the player
                    if(player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX())
                    {
                        countMisses++;
                        flag = false;     //flag false so that the else part is executed only when new enemy enters the screen
                        if(countMisses==10)     // misses>5,game over
                        {
                            myDb.insertData(score);
                            //setting playing false to stop the game.
                            playing = false;
                            isGameOver = true;

                            if(score>1000)
                                level.stop();
                            else
                                gameOnsound.stop();

                            //gameOnsound.stop();
                            gameOversound.start();

                            for(int i=0;i<4;i++)
                            {
                            if(highScore[i]<score)
                               {
                                final int finalI = i;
                                highScore[i] = score;
                                break;
                               }
                            }

                            SharedPreferences.Editor e = sharedPreferences.edit();      //storing the scores through shared Preferences
                            for(int i=0;i<4;i++){
                            int j = i+1;
                            e.putInt("score"+j,highScore[i]);
                        }
                            e.apply();

                           k= score;
                            myDb.insertData(k);
                        }
                    }
                }
            }


        if(Rect.intersects(player.getDetectCollision(),pyaar.getDetectCollision()))   //collect hearts
        {
            hearts++;
            countMisses-=2;
            friendcoll-=1;
            //gameOnsound.pause();
            if(score>1000)
                level.pause();
            else
                gameOnsound.pause();
            heartt.start();
            purp.setX(pyaar.getX());         //boom at the collision
            purp.setY(pyaar.getY());
            pyaar.setX(-200);

            if(score>1000)
                level.start();
            else
                gameOnsound.start();


        }



       // friend.update(player.getSpeed());   //friends coordinates updated

        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision()))   //collision between player and a friend
        {
            //gameOnsound.pause();
            if(score>1000)
                level.pause();
            else
                gameOnsound.pause();
            gameOversound.start();
            save.setX(friend.getX());         //boom at the collision
            save.setY(friend.getY());
            friend.setX(-200);
          //  gameOnsound.start();
            if(score>1000)
                level.start();
            else
                gameOnsound.start();

            friendcoll++;
            if (friendcoll == 6)
            {
                //countMisses=countMisses-2;


                playing = false;                      //playing false to stop the game
                isGameOver = true;                     //game over
                //gameOnsound.stop();
                if(score>1000)
                    level.stop();
                else
                    gameOnsound.stop();

                gameOversound.start();

                //Assigning the scores to the highscore integer array
                for (int i = 0; i < 4; i++) {
                    if (highScore[i] < score) {

                        final int finalI = i;
                        highScore[i] = score;
                        break;
                    }
                }
                //storing the scores through shared Preferences
                SharedPreferences.Editor e = sharedPreferences.edit();
                for (int i = 0; i < 4; i++) {
                    int j = i + 1;
                    e.putInt("score" + j, highScore[i]);
                }
                e.apply();
            }
        }
    }



    private void draw()
    {
        if (surfaceHolder.getSurface().isValid())
        {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);                   //white to draw the stars

            //drawing all stars
            for (Star s : stars)
            {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }


            //health bar

            Paint progress = new Paint();
            progress.setColor(Color.GREEN);
            progress.setTextSize(30);
            progress.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
           switch (countMisses)
           {
               case 0:
                   canvas.drawRect(10,10,1200,2,progress);
                   break;
               case 1:
                   canvas.drawRect(10, 10, 1000, 2, progress);
                   break;
               case 2:
                   canvas.drawRect(10, 10, 900, 2, progress);
                   break;
               case 3:
                   canvas.drawRect(10, 10, 800, 2, progress);
                   break;
               case 4:
                   canvas.drawRect(10, 10, 700, 2, progress);
                   break;
               case 5:
                   progress.setColor(Color.YELLOW);
                   canvas.drawRect(10, 10, 600, 2, progress);
                   break;
               case 6:
                   progress.setColor(Color.YELLOW);
                   canvas.drawRect(10, 10, 500, 2, progress);
                   break;
               case 7:
                   progress.setColor(Color.YELLOW);
                   canvas.drawRect(10, 10, 400, 2, progress);
                   break;
               case 8:
                   progress.setColor(Color.RED);
                   canvas.drawRect(10, 10, 300, 2, progress);
                   break;
               case 9:
                   progress.setColor(Color.RED);
                   canvas.drawRect(10, 10, 100, 2, progress);
                   break;
               case 10:
                   progress.setColor(Color.RED);
                   canvas.drawRect(10, 10, 10, 2, progress);
                   break;
               default:
                   progress.setColor(Color.GREEN);
                   canvas.drawRect(10, 10, 1200, 2, progress);
                   break;

           }

           //friends bar
           // progress.setColor(Color.BLUE);
           // progress.setTextSize(30);
           // progress.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
           // canvas.drawRect(0,500,1200,20,progress);





           Paint countcoll=new Paint();
            if(countMisses<5)
                countcoll.setColor(Color.GREEN);
            else if(countMisses<8)
                countcoll.setColor(Color.YELLOW);
            else
                countcoll.setColor(Color.RED);

            countcoll.setTextSize(60);
            canvas.save();
            canvas.rotate(90f, 1000, 50);
            canvas.drawText("!! "+ (10-countMisses) + " !!",1000,50,countcoll);
            canvas.restore();


            canvas.save();
            canvas.rotate(90f,720,300);
            countcoll.setColor(Color.BLUE);
            if(friendcoll>=4)
            countcoll.setColor(Color.RED);
            countcoll.setTextSize(60);
            canvas.drawText("Friend:"+ (5-friendcoll),2100,30,countcoll);
            canvas.restore();


            canvas.save();
            canvas.rotate(90f,720,300);
            countcoll.setColor(Color.rgb(143, 5, 173 ));
            countcoll.setTextSize(60);
            canvas.drawText("Hearts:"+ hearts,2100,110,countcoll);
            canvas.restore();



            //score on screen
            canvas.save();
            canvas.rotate(90f, 100, 50);
            paint.setTextSize(40);
            canvas.drawText("Score:"+score,100,50,paint);
            canvas.restore();



           // if(friendcoll<6) {
           //     int alpha = 255;
                //canvas.save();
           //     for (alpha = 255; alpha > 0; alpha -= 60) {
            //        countcoll.setColor(Color.argb(alpha, 200, 0, 200));
             //       countcoll.setTextSize(100);
            //        canvas.drawText("" + friendcoll, canvas.getWidth() / 2, (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)), countcoll);
            //    }
            //}

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);


            //drawing the enemies
            canvas.drawBitmap(
                    enemies.getBitmap(),
                    enemies.getX(),
                    enemies.getY(),
                    paint
            );


            canvas.drawBitmap(
                    save.getBitmap(),
                    save.getX(),
                    save.getY(),
                    paint
            );

            canvas.drawBitmap(
                    purp.getBitmap(),
                    purp.getX(),
                    purp.getY(),
                    paint
            );



            //drawing boom
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing friends image
            canvas.drawBitmap(

                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );


            canvas.drawBitmap(

                    pyaar.getBitmap(),
                    pyaar.getX(),
                    pyaar.getY(),
                    paint
            );


            //game is over
            if(isGameOver)
            {
                paint.setColor(Color.RED);
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.save();
                canvas.rotate(90f,canvas.getWidth()/2,yPos);
              //  int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over!! \nTouch Anywhere",canvas.getWidth()/2,yPos,paint);
               canvas.restore();

            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    private void control()
    {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause()
    {
        //gameOnsound.stop();
        if(score>1000)
            level.pause();
        else
            gameOnsound.pause();
        playing = false;                       //game paused
        try {
            //stopping the thread

            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume()
    {
        //when the game is resumed
        //gameOnsound.start();
        if(score>1000)
            level.start();
        else
            gameOnsound.start();
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }

        //game over,goes to MainActivity
        if(isGameOver)
        {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
            {
                Intent intent = new Intent(this.context, MainActivity.class);
               // intent.putExtra("score",k);
               context.startActivity(intent);

                //context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }

    //stopping music on exit
    public static void stopMusic()
      {

       gameOnsound.stop();
    }

}