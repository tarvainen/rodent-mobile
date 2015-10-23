package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

/////////////////////////////////////////////
// THIS CODE SUCKS HERE. MAKE SURE TO MAKE IT BEAUTIFUL LATER.
/////////////////////////////////////////////
public class Line extends Shape {
    public Line() {
        super();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setARGB(255, 100, 100, 100);
        paint.setStrokeWidth(5);
        canvas.drawLine(0, 0, 100, 100, paint);
    }
}
