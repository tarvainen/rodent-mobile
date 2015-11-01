package rodent.rodentmobile;

import android.graphics.Canvas;

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
        super.draw(canvas);
        if (this.pointArray != null) {
            canvas.drawLines(this.pointArray, this.getPaint());
        }
        if (this.isSelected()) {
            this.showCornerPinPoints(canvas);
        }
    }

    public void showCornerPinPoints (Canvas canvas) {
        for (AnchorPoint point : this.points) {
            point.draw(canvas);
        }
    }

    @Override
    public boolean wasTouched (Vector2<Float> touchPosition) {
        Vector2<Float> vsPoint = null;
        for (AnchorPoint point : this.points) {
            if (vsPoint == null) {
                vsPoint = point;
                continue;
            }
            Vector2<Float> cp = point;
            if (VectorMath.getDistanceToSegment(touchPosition, cp, vsPoint) < SELECTION_RADIUS) {
                this.setSelected(!this.isSelected());
                return true;
            }
            vsPoint = cp;
        }
        return false;
    }

    @Override
    public void drawBoundingBox (Canvas canvas) {
        Vector2<Float> min = VectorMath.min(this.getPoints());
        Vector2<Float> max = VectorMath.max(this.getPoints());
        this.getBoundingBox().setCornersFromMinAndMaxValuesOfShape(min, max);
        this.getBoundingBox().draw(canvas);
    }

    @Override
    public void update () {
        this.renderPointsToArray();
    }

    @Override
    public String toGCode (Paper paper) {
        String result = "";
        float millisInPx = paper.getMillisInPx();
        float height = paper.getSize().getY() / millisInPx;

        AnchorPoint start = this.points.get(0);
        result += "G00 X" + round(start.getX() / millisInPx)  + " Y" + flipMap(round(start.getY() / millisInPx), height) + "\n";
        result += "G00 Z" + this.getDepth() + "\n";

        for (AnchorPoint point : this.points) {
            result += "G00 X" + round(point.getX() / millisInPx);
            result += " Y" + flipMap(round(point.getY() / millisInPx), height) + "\n";
        }

        result += "G00 Z1.00\n";
        return result;
    }

    @Override
    public void move (Vector2<Float> delta) {
        for (AnchorPoint point : this.points) {
            Vector2<Float> p = point;
            Vector2<Float> result = VectorMath.sum(point, delta);
            point.setX(result.getX());
            point.setY(result.getY());
            this.update();
        }
    }
}
