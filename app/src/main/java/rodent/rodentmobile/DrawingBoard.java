package rodent.rodentmobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Atte on 22.10.2015.
 */
public class DrawingBoard extends SurfaceView implements Runnable {

    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private List<Shape> elements;

    private boolean isDrawingPaused = true;

    public DrawingBoard (Context context) {
        super(context);
        this.surfaceHolder = this.getHolder();
        this.elements = new LinkedList<>();
        this.elements.add(new Paper());
        this.elements.add(new Line());
    }

    public DrawingBoard (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.surfaceHolder = this.getHolder();
        this.elements = new LinkedList<>();
        this.elements.add(new Paper());
        this.elements.add(new Line());
    }

    public Canvas getCanvas () {
        return this.canvas;
    }

    public List<Shape> getDrawableElements () {
        return this.elements;
    }

    public void addDrawableElement (Shape shape) {
        this.elements.add(shape);
    }

    @Override
    public void run (){
        while (!this.isDrawingPaused) {
            if (this.isDrawingSurfaceValid()) {
                this.updateCanvas();
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

    public boolean lockCanvasForDrawing () {
        this.canvas = this.surfaceHolder.lockCanvas();
        this.canvas.save();
        return this.canvas != null;
    }

    public void unlockCanvasAndUpdate () {
        this.canvas.restore();
        this.surfaceHolder.unlockCanvasAndPost(this.canvas);
        this.canvas = null;
    }

    public void updateCanvas () {
        if (this.lockCanvasForDrawing()) {
            this.drawAllElements();
            this.unlockCanvasAndUpdate();
        }
    }

    public void drawAllElements () {
        for(Shape shape : this.elements) {
            draw(shape);
        }
    }

    public void draw (Shape shape) {
        shape.draw(this.canvas);
    }


    /////////////////////////////////////////////
    // THIS CODE SUCKS HERE. MAKE SURE TO MAKE IT BEAUTIFUL LATER.
    /////////////////////////////////////////////
    public class Line extends Shape {
        public Line () {
            super();
        }

        @Override
        public void draw (Canvas canvas) {
            Paint paint = new Paint();

            paint.setARGB(255, 100, 100, 100);
            paint.setStrokeWidth(5);
            canvas.drawLine(0, 0, 100, 100, paint);
        }
    }

    /////////////////////////////////////////////
    // THIS CODE SUCKS HERE. MAKE SURE TO MAKE IT BEAUTIFUL LATER.
    /////////////////////////////////////////////
    public class Paper extends Shape {
        public Paper () {
            super();
        }

        @Override
        public void draw (Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setARGB(255, 200, 200, 200);
            canvas.drawARGB(255, 240, 240, 240);

            int linesX = 100;
            float lineSeparatedDistance = (float)canvas.getWidth() / linesX;
            int linesY = (int)(canvas.getHeight() / lineSeparatedDistance);

            for (int i = 0; i <= linesX; i++) {
                canvas.drawLine(i * lineSeparatedDistance, 0, i * lineSeparatedDistance, canvas.getHeight(), paint);
            }

            for (int i = 0; i <= linesY; i++) {
                canvas.drawLine(0, i * lineSeparatedDistance, canvas.getWidth(), i * lineSeparatedDistance, paint);
            }
        }
    }
}
