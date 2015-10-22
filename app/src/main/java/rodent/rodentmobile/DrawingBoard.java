package rodent.rodentmobile;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Atte on 22.10.2015.
 */
public class DrawingBoard extends SurfaceView implements Runnable {

    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private boolean isDrawingPaused;

    public DrawingBoard (Context context) {
        super(context);
        this.surfaceHolder = this.getHolder();
    }

    @Override
    public void run (){
        while (!this.isDrawingPaused) {
            if (this.isDrawingSurfaceValid()) {
                this.drawAllElementsOnCanvas();
            }
        }
    }

    public void pause () {
        this.isDrawingPaused = true;
        this.stopDrawingThread();
    }

    public void resume () {
        this.isDrawingPaused = false;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stopDrawingThread () {
        try {
            this.thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.thread = null;
    }

    public boolean isDrawingSurfaceValid () {
        return this.surfaceHolder.getSurface().isValid();
    }

    public void drawAllElementsOnCanvas () {
        // TODO just draw all elements here
    }
}
