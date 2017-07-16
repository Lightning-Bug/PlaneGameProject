package comnitt.boston.planegameproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by HP on 15-Jul-17.
 */

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy enemies;
    private Friend friend;
    private Boom boom;                    //blast
    //the score holder
    int score;
    int highScore[] = new int[4];                     //the high Scores Holder
    SharedPreferences sharedPreferences;              //Shared Prefernces to store the High Scores
    //private int enemyCount = 3;
    private ArrayList<Star> stars = new ArrayList<Star>();
    int screenX;
    int countMisses;
    boolean flag ;
    private boolean isGameOver ;

    public GameView(Context context, int screenX, int screenY)
    {
        super(context);

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
        boom= new Boom(context);
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
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }


    private void update()
    {
        score++;
        player.update();

        boom.setX(-250);                           //setting boom outside the screen
        boom.setY(-250);

        for (Star s : stars)
        {    s.update(player.getSpeed());
        }

        if(enemies.getX()==screenX){
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
            enemies.update(player.getSpeed());            //if collision happens

            if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision()))  //detecting boom
            {
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());
            enemies.setX(-200);  //playing sound
            }

            else{
                //if the enemy has just entered
                if(flag){
                    //if player's x cd > enemies x cd , enemy has passed across the player
                    if(player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX())
                    {
                        countMisses++;
                        flag = false;         //flag false so that the else part is executed only when new enemy enters the screen
                        if(countMisses==10)     // misses>5,game over
                        {
                            //setting playing false to stop the game.
                            playing = false;
                            isGameOver = true;

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
                        }
                    }
                }
            }

        friend.update(player.getSpeed());   //friends coordinates updated

        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision()))   //collision between player and a friend
        {
            boom.setX(friend.getX());         //boom at the collision
            boom.setY(friend.getY());
            playing = false;                      //playing false to stop the game
            isGameOver = true;                     //game over

            //Assigning the scores to the highscore integer array
            for(int i=0;i<4;i++){
            if(highScore[i]<score)
            {

                final int finalI = i;
                highScore[i] = score;
                break;
            }
        }
            //storing the scores through shared Preferences
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i=0;i<4;i++){
            int j = i+1;
            e.putInt("score"+j,highScore[i]);
        }
            e.apply();
        }
    }



    private void draw()
    {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);                   //white to draw the stars

            //drawing all stars
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //score on screen
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);

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


            //game is over
            if(isGameOver)
            {
                paint.setColor(Color.RED);
                paint.setTextSize(140);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
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

    public void pause() {

        playing = false;                       //game paused
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        return true;
    }


}