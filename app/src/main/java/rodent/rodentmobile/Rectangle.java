package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Teemu on 23.10.2015.
 */
public class Rectangle extends Shape {

    public float x, y;
    public float x2, y2;

    public Rectangle(float x, float y) {
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
        paint.setStyle(Paint.Style.STROKE);

        float left = x;
        float right = x2;
        float top = y;
        float bottom = y2;
        if (right < left) {
            float tmp = left;
            left = right;
            right = tmp;
        }
        if (bottom < top) {
            float tmp = top;
            top = bottom;
            bottom = tmp;
        }
        canvas.drawRect(left, top, right, bottom, paint);
    }
}
