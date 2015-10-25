package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Atte on 22.10.2015.
 */
public abstract class Shape implements Serializable{

    // Have to make a CustomPaint to serialize paint.
//    private Paint paint;
    private Vector2<Float> position;
    private Vector2<Float> size;
    boolean selected;

    public Shape () {
//        this.paint = new Paint();
//        this.paint.setARGB(180, 80, 80, 80);
//        this.paint.setAntiAlias(true);
//        this.paint.setStrokeWidth(5);
//        this.paint.setStyle(Paint.Style.STROKE);
//        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.position = new Vector2<>(0f, 0f);
        this.size = new Vector2<>(0f, 0f);
        this.selected = false;
    }

    public Paint getPaint () {
        Paint paint = new Paint();
        paint.setARGB(180, 80, 80, 80);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    public Vector2<Float> getPosition () {
        return this.position;
    }

    public void setPosition (Vector2<Float> position) {
        this.position = position;
    }

    public Vector2<Float> getSize () {
        return this.size;
    }

    public void setSize (Vector2<Float> size) {
        this.size = size;
    }

    public void setSelected (boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected () {
        return this.selected;
    }

    public abstract void draw (Canvas canvas);


}
