package rodent.rodentmobile.drawing.shapes;

import android.graphics.Canvas;

import rodent.rodentmobile.drawing.helpers.AnchorPoint;
import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Atte on 23/10/15.
 */
public class LineShape extends PolylineShape {

    private float [] pointArray;
    
    public LineShape () {
        super();
        this.pointArray = new float[4];
    }

    public Vector2<Float> getStartPoint () {
        return this.getPosition();
    }

    public void setStartPoint (Vector2<Float> point) {
        this.setPosition(point);
        this.updatePoints();
    }

    public Vector2<Float> getEndPoint () {
        return this.getSize();
    }

    public void setEndPoint (Vector2<Float> point) {
        this.setSize(point);
        this.updatePoints();
    }

    private void updatePoints () {
        this.getPoints().clear();
        this.addPoint(new AnchorPoint(this.getPosition()));
        this.addPoint(new AnchorPoint(this.getSize()));
    }

    @Override
    public void draw (Canvas canvas){
        super.draw(canvas);
    }

}
