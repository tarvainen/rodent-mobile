package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atte on 23/10/15.
 */
public class PolylineShape extends Shape {

    private List<AnchorPoint> points;
    private float [] pointArray;

    public PolylineShape () {
        super();
        this.init();
    }

    private void init () {
        this.points = new ArrayList<>();
    }

    void setPoints (List<AnchorPoint> points) {
        this.points = points;
        this.renderPointsToArray();
    }

    List<AnchorPoint> getPoints () {
        return this.points;
    }

    void addPoint (AnchorPoint point) {
        this.points.add(point);
        this.renderPointsToArray();
    }

    private void renderPointsToArray () {
        if (this.points.size() <= 1) {
            return;
        }
        this.pointArray = new float [this.points.size() * 4 - 4];
        int i = 0;
        int size = this.points.size();
        this.pointArray[0] = this.points.get(0).getX();
        this.pointArray[1] = this.points.get(0).getY();
        for (AnchorPoint point : this.points) {
            if (i == 0){
                i += 2;
                continue;
            }
            this.pointArray[i] = point.getX();
            this.pointArray[i + 1] = point.getY();

            if ((i + 4) <= this.points.size() * 4 - 4) {
                this.pointArray[i + 2] = point.getX();
                this.pointArray[i + 3] = point.getY();
                i += 4;
                continue;
            }
            i += 2;
        }
    }

    @Override
    public void draw (Canvas canvas) {
        if (this.pointArray != null) {
            canvas.drawLines(this.pointArray, this.getPaint());
            if (this.isSelected()) {
                this.showCornerPinPoints(canvas);
            }
        }
    }

    public void showCornerPinPoints (Canvas canvas) {
        for (AnchorPoint point : this.points) {
            point.draw(canvas);
        }
    }

}
