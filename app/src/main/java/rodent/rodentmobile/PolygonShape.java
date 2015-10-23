package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Path;

/**
 * Created by attetarvainen on 23/10/15.
 */
public class PolygonShape extends PolylineShape {

    public PolygonShape () {
        super();
    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        int pointsAmount = this.getPoints().size();
        Vector2<Float> startPoint = this.getPoints().get(0);
        Vector2<Float> endPoint = this.getPoints().get(pointsAmount);


        Path path = new Path();
        if (startPoint != null && endPoint != null) {
            path.moveTo(startPoint.getX(), startPoint.getY());
            path.lineTo(endPoint.getX(), endPoint.getY());
            canvas.drawPath(path, this.getPaint());
        }

    }

}

