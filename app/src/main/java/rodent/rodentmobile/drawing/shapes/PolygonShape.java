package rodent.rodentmobile.drawing.shapes;

import android.graphics.Canvas;

import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Atte on 23/10/15.
 */
public class PolygonShape extends PolylineShape {

    public PolygonShape () {
        super();
    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        if (this.getPoints().size() > 2) {
            connectStartAndEndPointWithLine(canvas);
        }
    }

    public void connectStartAndEndPointWithLine (Canvas canvas) {
        Vector2<Float> start = this.getPoints().get(0);
        Vector2<Float> end = this.getPoints().get(this.getPoints().size() - 1);
        canvas.drawLine(start.getX(), start.getY(), end.getX(), end.getY(), this.getPaint());
    }

}

