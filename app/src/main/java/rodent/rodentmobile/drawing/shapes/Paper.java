package rodent.rodentmobile.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

import rodent.rodentmobile.drawing.helpers.CustomPaint;
import rodent.rodentmobile.utilities.Angle;
import rodent.rodentmobile.utilities.Vector2;

/////////////////////////////////////////////
// THIS CODE SUCKS HERE. MAKE SURE TO MAKE IT BEAUTIFUL LATER.
/////////////////////////////////////////////
public class Paper extends Shape {

    private float millisInPx;
    private float widthInMills;
    private float heightInMills;

    {
        millisInPx = 12.8f;
        widthInMills = 100;
        heightInMills = 20f;
    }

    public Paper() {
        super();
    }

    @Override
    public void draw(Canvas canvas) {
        CustomPaint paint = new CustomPaint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setARGB(255, 200, 200, 200);
        canvas.drawARGB(255, 240, 240, 240);

        int linesX = (int) widthInMills;
        float lineSeparatedDistance = 12.8f;
        this.millisInPx = lineSeparatedDistance;
        int linesY = (int) heightInMills;

        this.setSize(new Vector2<>(linesX * millisInPx, linesY * millisInPx));

        for (int i = 0; i <= linesX; i++) {
            canvas.drawLine(i * lineSeparatedDistance, 0, i * lineSeparatedDistance, this.getSize().getY(), paint);
        }

        for (int i = 0; i <= linesY; i++) {
            canvas.drawLine(0, i * lineSeparatedDistance, this.getSize().getX(), i * lineSeparatedDistance, paint);
        }
    }


    public float getMillisInPx () {
        return this.millisInPx;
    }

    public void setWidthInMills (float mill) {
        this.widthInMills = mill;
    }

    public void setHeightInMills (float mill) {
        this.heightInMills = mill;
    }

    public float getWidthInMills () {
        return this.widthInMills;
    }

    public float getHeightInMills () {
        return this.heightInMills;
    }

    @Override
    public boolean wasTouched (Vector2<Float> position) {
        return true;
    }

    @Override
    public void drawBoundingBox (Canvas canvas) {

    }

    @Override
    public String toGCode (Paper paper) {
        return "";
    }

    @Override
    public void scale (int corner, Vector2<Float> amount) {

    }

    @Override
    public void rotate (Vector2<Float> pivot, Angle amount) {

    }

    @Override
    public void flipHorizontally () {

    }

    @Override
    public void flipVertically () {

    }

    @Override
    public void renderToMatchBase (Paper paper) {

    }

}