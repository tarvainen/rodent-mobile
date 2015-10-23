package rodent.rodentmobile;

import android.view.MotionEvent;

/**
 * Created by Teemu on 23.10.2015.
 */
public interface Tool {
    void start(MotionEvent event);
    void move(MotionEvent event);
    void end(MotionEvent event);
    Shape getDrawable();
    void clear();
    boolean drawing();
}
