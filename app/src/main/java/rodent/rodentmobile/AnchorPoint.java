package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by attetarvainen on 26/10/15.
 */
public class AnchorPoint extends Vector2<Float> {

    private Paint paint;

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

    private void init () {
        this.paint = new Paint();
        paint.setARGB(180, 80, 80, 80);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void draw(Canvas canvas) {
        Vector2<Float> point = this;
        RectF rect = new RectF(point.getX() - 1f, point.getY() - 1f, point.getX() + 1f, point.getY() + 1f);
        canvas.drawArc(rect, 0f, 360f, false, this.paint);
    }

}
