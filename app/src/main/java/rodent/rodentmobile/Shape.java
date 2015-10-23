package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Atte on 22.10.2015.
 */
public abstract class Shape {

    private ShapeTransform transform;
    private Paint paint;

    public Shape () {
        this.paint = new Paint();
        this.paint.setARGB(255, 80, 80, 80);
        this.paint.setStrokeWidth(5);
    }

    public Paint getPaint () {
        return this.paint;
    }

    public ShapeTransform getTransform () {
        return this.transform;
    }

    public void setTransform (ShapeTransform transform) {
        this.transform = transform;
    }


    public abstract void draw (Canvas canvas);

}
