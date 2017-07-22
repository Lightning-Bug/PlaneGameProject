package comnitt.boston.planegameproject;

/**
 * Created by HP on 15-Jul-17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.Random;


public class Enemy {

    private Bitmap bitmap;

    private int x;
    private int y;

    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    private Rect detectCollision;


    public Enemy(Context context, int screenX, int screenY)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        // min and max coordinates
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //generating a random coordinate to add enemy
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(int playerSpeed)
    {
        //enemy will move right to left
        x -= playerSpeed;
        x -= speed;
        if (x < minX - bitmap.getWidth())
        {
            Random generator = new Random();                    //right edge
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        //rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public void setX(int x){
        this.x = x;
    }


    public Rect getDetectCollision() {
        return detectCollision;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

}
