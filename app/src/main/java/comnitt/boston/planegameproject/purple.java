package comnitt.boston.planegameproject;

/**
 * Created by HP on 22-Jul-17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class purple {

    private Bitmap bitmap;

    private int x;
    private int y;

    public purple(Context context)
    {

        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.loduu);
        x = -250;
        y = -250;                    //after collision
    }

    // set methods for return
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    //get methods
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}