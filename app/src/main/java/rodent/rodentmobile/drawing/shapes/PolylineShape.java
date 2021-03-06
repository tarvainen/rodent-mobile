package rodent.rodentmobile.drawing.shapes;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.data.GCode;
import rodent.rodentmobile.drawing.helpers.AnchorPoint;
import rodent.rodentmobile.utilities.Angle;
import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.utilities.VectorMath;

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

    public void setPoints (List<AnchorPoint> points) {
        this.points = points;
        this.renderPointsToArray();
    }

    public List<AnchorPoint> getPoints () {
        return this.points;
    }


    public void addPoint (AnchorPoint point) {
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
        Vector2<Float> min = VectorMath.min(this.getPoints());
        Vector2<Float> max = VectorMath.max(this.getPoints());
        this.setPosition(min);
        this.setSize(new Vector2<>(max.getX() - min.getX(), max.getY() - min.getY()));
    }

    @Override
    public String toGCode (Paper paper) {
        String result = "";
        float millisInPx = paper.getMillisInPx();
        float height = paper.getSize().getY() / millisInPx;

        AnchorPoint start = this.points.get(0);
        result += "G00 X" + round(start.getX() / millisInPx)  + " Y" + flipMap(round(start.getY() / millisInPx), height) + "\n";
        result += "G00 Z-" + this.getDepth() + "\n";

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
        }
        this.update();
    }

    @Override
    public void scale (int corner, Vector2<Float> amount) {
        for (AnchorPoint point : this.points) {
            Vector2<Float> res = VectorMath.getScaledPosition(this.getBoundingBox().getCorner(corner), this.getBoundingBox().getCorner(corner + 2), point, amount);
            point.setX(res.getX());
            point.setY(res.getY());
        }
    }

    @Override
    public void rotate (Vector2<Float> pivot, Angle amount) {
        for (AnchorPoint point : this.points) {
            Vector2<Float> result = VectorMath.getRotatedPosition(pivot, point, amount);
            point.setX(result.getX());
            point.setY(result.getY());
        }
    }

    @Override
    public void flipHorizontally () {
        Vector2<Float> max = VectorMath.max(this.points);
        float xPin = max.getX();
        for (AnchorPoint point : this.points) {
            float delta = xPin - point.getX();
            point.setX(xPin + delta);
        }
    }

    @Override
    public void flipVertically () {
        Vector2<Float> max = VectorMath.max(this.points);
        float yPin = max.getY();
        for (AnchorPoint point : this.points) {
            float delta = yPin - point.getY();
            point.setY(yPin + delta);
        }
    }

    @Override
    public void renderToMatchBase (Paper paper) {
        float factor = paper.getMillisInPx();

        float height = paper.getHeightInMills();

        for (AnchorPoint a : this.points) {
            a.setX(a.getX() * factor);
            a.setY((height - a.getY()) * factor);
        }

        update();
        setReady(true);
    }

    @Override
    public Vector2<Float> getRealBoundsX (Paper paper) {
        float max = 0f;
        float min = 0f;

        for (AnchorPoint a : this.points) {
            if (a.getX() > max) {
                max = a.getX();
            }
            else if (a.getX() < min) {
                min = a.getX();
            }
        }

        min /= paper.getMillisInPx();
        max /= paper.getMillisInPx();

        return new Vector2<>(min, max);
    }

    @Override
    public Vector2<Float> getRealBoundsY (Paper paper) {
        float max = 0f;
        float min = 0f;

        for (AnchorPoint a : this.points) {
            if (a.getY() > max) {
                max = a.getY();
            }
            else if (a.getY() < min) {
                min = a.getY();
            }
        }

        min /= paper.getMillisInPx();
        max /= paper.getMillisInPx();

        return new Vector2<>(min, max);
    }

    public static PolylineShape asCircleShape (Vector2<Float> position, float radius) {
        PolylineShape result = new PolylineShape();
        Angle angle = new Angle();
        angle.setDegrees(0);
        float segments = 20;
        float angleIncrement = 360 / segments;

        for (int i = 0; i <= segments; i++) {
            result.addPoint(circumfencePointAtAngleAndRadius(angle, radius, position));
            angle.setDegrees(angle.getDegrees() + angleIncrement);
        }

        return result;

    }

    private static AnchorPoint circumfencePointAtAngleAndRadius (Angle angle, float radius, Vector2<Float> origo) {
        float x = (float)Math.cos(angle.getRadians()) * radius + origo.getX();
        float y = (float)Math.sin(angle.getRadians()) * radius + origo.getY();
        return new AnchorPoint(x, y);
    }

    public static PolylineShape fromGCodeList (List<GCode> codes) {
        PolylineShape shape = new PolylineShape();
        shape.setDepth(-codes.get(0).getZ());
        for (int i = 0; i < codes.size(); i++) {
            shape.addPoint(new AnchorPoint(codes.get(i).getX(), codes.get(i).getY()));
        }
        return shape;
    }
}
