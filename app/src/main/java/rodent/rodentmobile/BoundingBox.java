package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Atte on 27/10/15.
 */
public class BoundingBox {

    private static final int UPPER_LEFT = 0;
    private static final int UPPER_RIGHT = 1;
    private static final int LOWER_RIGHT = 2;
    private static final int LOWER_LEFT = 3;

    private Vector2<Float>[] corners;
    private float pinRadius;

    private Paint paint;

    public BoundingBox () {
        this.corners = new Vector2[4];
        this.paint = new Paint();
        this.paint.setARGB(120, 80, 80, 80);
        this.paint.setStrokeWidth(2);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.pinRadius = 5f;
    }

    public void setCorners(Vector2<Float> upperLeft, Vector2<Float> upperRight, Vector2<Float> lowerRight, Vector2<Float> lowerLeft) {
        this.setCorner(upperLeft, UPPER_LEFT);
        this.setCorner(upperRight, UPPER_RIGHT);
        this.setCorner(lowerLeft, LOWER_LEFT);
        this.setCorner(lowerRight, LOWER_RIGHT);
    }

    public void setCorner (Vector2<Float> position, int corner) {
        this.corners[corner] = position;
    }

    public Vector2<Float> getCorner (int corner) {
        if (corner > 0 && corner < corners.length) {
            return corners[corner];
        }
        return null;
    }

    public void setCornersFromMinAndMaxValuesOfShape (Vector2<Float> min, Vector2<Float> max) {
        this.setCorner(new Vector2<>(min.getX(), min.getY()), UPPER_LEFT);
        this.setCorner(new Vector2<>(max.getX(), min.getY()), UPPER_RIGHT);
        this.setCorner(new Vector2<>(max.getX(), max.getY()), LOWER_RIGHT);
        this.setCorner(new Vector2<>(min.getX(), max.getY()), LOWER_LEFT);
    }

    public void draw (Canvas canvas) {
        RectF rect = new RectF();
        rect.left = this.corners[UPPER_LEFT].getX();
        rect.right = this.corners[UPPER_RIGHT].getX();
        rect.top = this.corners[UPPER_RIGHT].getY();
        rect.bottom = this.corners[LOWER_RIGHT].getY();
        canvas.drawRect(rect, this.paint);
        drawCornerPoints(canvas);
    }

    private void drawCornerPoints (Canvas canvas) {
        for (Vector2<Float> pin : this.corners) {
            RectF rect = new RectF(pin.getX() - pinRadius, pin.getY() - pinRadius, pin.getX() + pinRadius, pin.getY() + pinRadius);
            canvas.drawArc(rect, 0, 360, true, this.paint);
        }
    }

}
