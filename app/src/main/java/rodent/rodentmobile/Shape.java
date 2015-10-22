package rodent.rodentmobile;

import android.graphics.Canvas;

/**
 * Created by Atte on 22.10.2015.
 */
public abstract class Shape {

    private ShapeTransform transform;

    public Shape () {

    }

    public ShapeTransform getTransform () {
        return this.transform;
    }

    public void setTransform (ShapeTransform transform) {
        this.transform = transform;
    }


    public abstract void draw (Canvas canvas);

}
