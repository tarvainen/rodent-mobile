package rodent.rodentmobile;

import android.view.MotionEvent;

import java.util.List;

/**
 * Created by Teemu on 23.10.2015.
 */
public abstract class Tool {

    private boolean isInUse;
    private Shape drawable;
    private List<Shape> shapeContainer;

    public Tool () {
        this.isInUse = false;
        this.drawable = null;
        this.shapeContainer = null;
    }

    public Tool (List<Shape> targetContainer) {
        this.shapeContainer = targetContainer;
        this.isInUse = false;
        this.drawable = null;
    }

    public boolean isBusy () {
        return this.isInUse;
    }

    public void setBusy (boolean busy) {
        this.isInUse = busy;
    }

    public void setShapeContainer (List<Shape> container) {
        this.shapeContainer = container;
    }

    public List<Shape> getShapeContainer () {
        return this.shapeContainer;
    }

    public void setShape (Shape drawable) {
        this.drawable = drawable;
    }

    public Shape getShape () {
        return this.drawable;
    }

    public abstract void onStart (Vector2<Float> position);
    public abstract void onMove (Vector2<Float> position);
    public abstract void onPress (Vector2<Float> position);
    public abstract void onEnd (Vector2<Float> position);
    public abstract void clear ();

}