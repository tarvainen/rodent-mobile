package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by attetarvainen on 23/10/15.
 */
public class PolylineShape extends Shape {

    private List<Vector2<Float>> points;

    public PolylineShape () {
        super();
    }

    private void init () {
        this.points = new ArrayList<>();
    }

    void setPoints (List<Vector2<Float>> points) {
        this.points = points;
    }

    List<Vector2<Float>> getPoints () {
        return this.points;
    }

    void addPoint (Vector2<Float> point) {
        this.points.add(point);
    }

    @Override
    public void draw (Canvas canvas) {
        Path path = new Path();
        if (this.points.get(0) == null) {
            return;
        }

        Vector2<Float> startPoint = this.points.get(0);
        path.moveTo(startPoint.getX(), startPoint.getY());

        for (Vector2<Float> point : this.points) {
            path.lineTo(point.getX(), point.getY());
        }

        canvas.drawPath(path, this.getPaint());
    }

}
