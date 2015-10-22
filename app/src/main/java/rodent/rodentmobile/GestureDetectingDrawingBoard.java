package rodent.rodentmobile;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


/**
 * Created by Atte on 22.10.2015.
 */
public class GestureDetectingDrawingBoard extends DrawingBoard {

    private enum GestureType {
        NONE, DRAG, ZOOM
    }

    private GestureType currentGestureType;

    private float canvasScaleFactor;
    private final float MAX_CANVAS_SCALE_FACTOR = 5.0f;
    private final float MIN_CANVAS_SCALE_FACTOR = 1.0f;

    private Vector2<Float> touchStartPosition;
    private Vector2<Float> canvasTranslate;
    private Vector2<Float> previousCanvasTranslate;

    private boolean wasDragged;

    private ScaleGestureDetector scaleDetector;

    public GestureDetectingDrawingBoard (Context context) {
        super(context);
        this.canvasScaleFactor = 1.f;
        this.scaleDetector = new ScaleGestureDetector(this.getContext(), new ZoomGestureListener());
        this.touchStartPosition = new Vector2<>();
        this.canvasTranslate = new Vector2<>();
        this.previousCanvasTranslate = new Vector2<>();
        this.wasDragged = false;
    }

    @Override
    public void updateCanvas () {
        if (this.lockCanvasForDrawing()) {
            this.getCanvas().scale(this.canvasScaleFactor, this.canvasScaleFactor);
            this.drawAllElements();
            this.unlockCanvasAndUpdate();
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        this.scaleDetector.onTouchEvent(event);
        //this.handleOnTouchEventAction(event);
        return true;
    }

    public void handleOnTouchEventAction (MotionEvent e) {
        int action = e.getAction() & MotionEvent.ACTION_MASK;
        Vector2<Float> eventPosition = new Vector2<>(e.getX(), e.getY());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Action", "Drag");
                this.currentGestureType = GestureType.DRAG;
                this.touchStartPosition.setX(eventPosition.getX() - previousCanvasTranslate.getX());
                this.touchStartPosition.setY(eventPosition.getY() - previousCanvasTranslate.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Action", "Move");
                this.canvasTranslate.setX(eventPosition.getX() - touchStartPosition.getX());
                this.canvasTranslate.setY(eventPosition.getY() - touchStartPosition.getY());
                this.wasDragged = wasCanvasDragged();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("Action", "Zoom");
                currentGestureType = GestureType.ZOOM;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("Action", "None");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("Action", "Drag");
                break;
            default:
                break;
        }
    }

    public double getDistanceFromOrigo (double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public boolean wasCanvasDragged () {
        double deltaX = canvasTranslate.getX() - previousCanvasTranslate.getX();
        double deltaY = canvasTranslate.getY() - previousCanvasTranslate.getY();
        return getDistanceFromOrigo(deltaX, deltaY) > 0;
    }

    public void setCanvasScaleFactor (float factor) {
        this.canvasScaleFactor = Math.max(MIN_CANVAS_SCALE_FACTOR, Math.min(factor, MAX_CANVAS_SCALE_FACTOR));
    }

    private class ZoomGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale (ScaleGestureDetector detector) {
            setCanvasScaleFactor(detector.getScaleFactor() * canvasScaleFactor);
            return true;
        }
    }
}
