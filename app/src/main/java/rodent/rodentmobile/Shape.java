package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Atte on 22.10.2015.
 */
public abstract class Shape implements Serializable{

    // Have to make a CustomPaint to serialize paint.
//    private Paint paint;

    public static final float SELECTION_RADIUS = 20.0f;
    private Vector2<Float> position;
    private Vector2<Float> size;
    boolean selected;
    boolean ready;

    Paint boundingPaint;

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
        this.ready = false;
        this.boundingPaint = new Paint();
        this.boundingPaint.setStrokeWidth(2);
        this.boundingPaint.setARGB(100, 80, 80, 80);
        this.boundingPaint.setStyle(Paint.Style.STROKE);
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

    public Paint getBoundingPaint () {
        return this.boundingPaint;
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

    public void setReady (boolean ready) {
        this.ready = ready;
    }

    public boolean isReady () {
        return this.ready;
    }

    public void draw (Canvas canvas) {
        if (this.selected && this.ready) {
            this.drawBoundingBox(canvas);
        }
    }

    public void update () {

    }

    public abstract boolean wasTouched (Vector2<Float> touchPosition);

    public abstract void drawBoundingBox (Canvas canvas);

}
