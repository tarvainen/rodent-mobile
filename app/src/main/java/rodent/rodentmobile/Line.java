package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Line extends Shape {

    private float x, y;
    public float x2, y2;

    public Line(float x, float y) {
        super();
        this.x = x;
        this.y = y;
        x2 = x;
        y2 = y;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setARGB(255, 100, 100, 100);
        paint.setStrokeWidth(5);
        canvas.drawLine(x, y, x2, y2, paint);
    }
}
