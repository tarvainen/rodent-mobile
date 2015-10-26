package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

/////////////////////////////////////////////
// THIS CODE SUCKS HERE. MAKE SURE TO MAKE IT BEAUTIFUL LATER.
/////////////////////////////////////////////
public class Paper extends Shape {

    private float millisInPx;

    public Paper() {
        super();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setARGB(255, 200, 200, 200);
        canvas.drawARGB(255, 240, 240, 240);

        int linesX = 100;
        float lineSeparatedDistance = (float) canvas.getWidth() / linesX;
        this.millisInPx = lineSeparatedDistance;
        int linesY = (int) (canvas.getHeight() / lineSeparatedDistance);

        for (int i = 0; i <= linesX; i++) {
            canvas.drawLine(i * lineSeparatedDistance, 0, i * lineSeparatedDistance, canvas.getHeight(), paint);
        }

        for (int i = 0; i <= linesY; i++) {
            canvas.drawLine(0, i * lineSeparatedDistance, canvas.getWidth(), i * lineSeparatedDistance, paint);
        }
    }

    public float getMillisInPx () {
        return this.millisInPx;
    }

    @Override
    public boolean wasTouched (Vector2<Float> position) {
        return true;
    }
}