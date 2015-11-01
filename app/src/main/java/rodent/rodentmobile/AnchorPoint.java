package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by attetarvainen on 26/10/15.
 */
public class AnchorPoint extends Vector2<Float> {

    private CustomPaint paint;
    private float radius;

    public AnchorPoint() {
        super();
        this.init();
    }

    public AnchorPoint (Vector2<Float> position) {
        super();
        this.setX(position.getX());
        this.setY(position.getY());
        this.init();
    }

    public AnchorPoint (float x, float y) {
        super();
        this.setX(x);
        this.setY(y);
        this.init();
    }

    private void init () {
        this.paint = new CustomPaint();
        paint.setARGB(180, 80, 80, 80);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        this.radius = 5f;
    }

    public void draw(Canvas canvas) {
        Vector2<Float> point = this;

        RectF rect = new RectF(point.getX() - radius, point.getY() - radius, point.getX() + radius, point.getY() + radius);
        canvas.drawArc(rect, 0f, 360f, false, this.paint);
    }

}
