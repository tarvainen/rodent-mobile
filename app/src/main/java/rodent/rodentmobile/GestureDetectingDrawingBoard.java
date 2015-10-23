package rodent.rodentmobile;

import android.content.Context;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;


/**
 * Created by Atte on 22.10.2015.
 */
public class GestureDetectingDrawingBoard extends DrawingBoard {

    private final float MAX_CANVAS_SCALE_FACTOR = 5.0f;
    private final float MIN_CANVAS_SCALE_FACTOR = 1.0f;

    private float canvasScaleFactor;

    private Vector2<Float> touchStartPosition;
    private Vector2<Float> canvasTranslate;
    private Vector2<Float> previousCanvasTranslate;
    private Vector2<Float> displaySize;

    private ScaleGestureDetector scaleDetector;

    public GestureDetectingDrawingBoard (Context context) {
        super(context);
        this.canvasScaleFactor = 1.f;
        this.scaleDetector = new ScaleGestureDetector(this.getContext(), new ZoomGestureListener());
        this.touchStartPosition = new Vector2<>(0f, 0f);
        this.canvasTranslate = new Vector2<>(0f, 0f);
        this.previousCanvasTranslate = new Vector2<>(0f, 0f);

        WindowManager manager = (WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        float width = display.getWidth();
        float height = display.getHeight();
        this.displaySize = new Vector2<>(width, height);
    }

    @Override
    public void updateCanvas () {
        if (this.lockCanvasForDrawing()) {
            this.scaleCanvas();
            this.translateCanvas();
            this.drawAllElements();
            this.unlockCanvasAndUpdate();
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        this.scaleDetector.onTouchEvent(event);
        this.handleOnTouchEventGesture(event);
        return true;
    }

    public void scaleCanvas () {
        this.getCanvas().scale(this.canvasScaleFactor, this.canvasScaleFactor);
    }

    public void translateCanvas () {
        if (isViewInLeftBound()) {
            canvasTranslate.setX(0.f);
            previousCanvasTranslate.setX(0.f);
        } else if (isViewInRightBound()) {
            canvasTranslate.setX((1 - canvasScaleFactor) * displaySize.getX());
        }

        if (isViewInTopBound()) {
            canvasTranslate.setY(0.f);
            previousCanvasTranslate.setY(0.f);
        } else if (isViewInBottomBound()) {
            canvasTranslate.setY((1 - canvasScaleFactor) * displaySize.getY());
        }

        this.getCanvas().translate(canvasTranslate.getX() / canvasScaleFactor, canvasTranslate.getY() / canvasScaleFactor);
    }

    public boolean isViewInLeftBound () {
        return -canvasTranslate.getX() < 0;
    }

    public boolean isViewInRightBound () {
        return -canvasTranslate.getX() > (canvasScaleFactor - 1.f) * displaySize.getX();
    }

    public boolean isViewInTopBound () {
        return -canvasTranslate.getY() < 0;
    }

    public boolean isViewInBottomBound () {
        return -canvasTranslate.getY() > (canvasScaleFactor - 1.f) * displaySize.getY();
    }

    public void handleOnTouchEventGesture (MotionEvent e) {
        int action = e.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN: // when the canvas is pressed
                this.touchStartPosition.setX(e.getX() - previousCanvasTranslate.getX());
                this.touchStartPosition.setY(e.getY() - previousCanvasTranslate.getY());
                break;
            case MotionEvent.ACTION_MOVE: // when the finger is moved on the canvas
                this.canvasTranslate.setX(e.getX() - touchStartPosition.getX());
                this.canvasTranslate.setY(e.getY() - touchStartPosition.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // when the second finger is pressed
                break;
            case MotionEvent.ACTION_UP: // when the finger is lifted off the canvas
                this.previousCanvasTranslate = equalizeTranslate(canvasTranslate);
                break;
            case MotionEvent.ACTION_POINTER_UP: // when the second finger is lifted off the canvas
                this.previousCanvasTranslate = equalizeTranslate(canvasTranslate);
                break;
            default:
                break;
        }
    }

    public Vector2<Float> equalizeTranslate (Vector2<Float> translate2) {
         return new Vector2<>(translate2.getX(), translate2.getY());
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
