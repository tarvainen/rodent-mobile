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

    public static final float SELECTION_RADIUS = 20.0f;
    private Vector2<Float> position;
    private Vector2<Float> size;
    boolean selected;
    boolean ready;

    private BoundingBox boundingBox;

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
        this.boundingBox = new BoundingBox();
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

    public void setReady (boolean ready) {
        this.ready = ready;
    }

    public boolean isReady () {
        return this.ready;
    }

    public void setBoundingBox (BoundingBox box) {
        this.boundingBox = box;
    }

    public BoundingBox getBoundingBox () {
        return this.boundingBox;
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
