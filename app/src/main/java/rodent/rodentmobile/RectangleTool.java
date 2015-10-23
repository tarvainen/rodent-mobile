package rodent.rodentmobile;

import android.view.MotionEvent;

/**
 * Created by Teemu on 23.10.2015.
 */
public class RectangleTool implements Tool {

    private boolean drawing = false;
    private Rectangle rectangle;

    @Override
    public void start(MotionEvent event) {
        drawing = true;
        rectangle = new Rectangle(event.getX(), event.getY());
    }

    @Override
    public void move(MotionEvent event) {
        rectangle.y2 = event.getY();
        rectangle.x2 = event.getX();
    }

    @Override
    public void end(MotionEvent event) {
        rectangle.y2 = event.getY();
        rectangle.x2 = event.getX();
    }

    @Override
    public Shape getDrawable() {
        return rectangle;
    }

    @Override
    public void clear() {
        rectangle = null;
        drawing = false;
    }

    @Override
    public boolean drawing() {
        return drawing;
    }
}
