package rodent.rodentmobile;

import android.view.MotionEvent;

/**
 * Created by Teemu on 23.10.2015.
 */
public class LineTool implements Tool {

    boolean drawing = false;
    private Line line;

    public LineTool() {
    }

    public void start(MotionEvent event) {
        drawing = true;
        line = new Line(event.getX(), event.getY());
    }

    public void move(MotionEvent event) {
        line.x2 = event.getX();
        line.y2 = event.getY();
    }

    public void end(MotionEvent event) {
        line.x2 = event.getX();
        line.y2 = event.getY();
    }

    public Line getDrawable() {
        return line;
    }

    public void clear() {
        line = null;
        drawing = false;
    }

    public boolean drawing() {
        return drawing;
    }
}
