package rodent.rodentmobile.drawing.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import rodent.rodentmobile.drawing.tools.FreeHandTool;
import rodent.rodentmobile.drawing.tools.MoveTool;
import rodent.rodentmobile.drawing.tools.Tool;
import rodent.rodentmobile.utilities.Vector2;


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

    private boolean snipetisnapActivated;
    private boolean modified = false;

    public GestureDetectingDrawingBoard (Context context) {
        super(context);
        this.init();
    }

    public GestureDetectingDrawingBoard (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init () {
        this.canvasScaleFactor = 1.f;
        this.scaleDetector = new ScaleGestureDetector(this.getContext(), new ZoomGestureListener());
        this.touchStartPosition = new Vector2<>(0f, 0f);
        this.canvasTranslate = new Vector2<>(0f, 0f);
        this.previousCanvasTranslate = new Vector2<>(0f, 0f);
        this.setSnippingActivate(true);
        this.setTool(new MoveTool());
    }

    @Override
    public void onDraw (Canvas canvas) {
        scaleCanvas(canvas);
        translateCanvas(canvas);
        drawBackgroundPaper(canvas);
        drawElements(canvas);

        if (isToolBusy()) {
            drawBusyElement(canvas);
        }
        Log.d("on draw", this.getPaper().getMillisInPx() + "");
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        this.scaleDetector.onTouchEvent(event);
        this.handleOnTouchEventGesture(event);
        this.modified = true;
        return true;
    }

    public void scaleCanvas (Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        this.displaySize = new Vector2<>(width, height);
        canvas.scale(this.canvasScaleFactor, this.canvasScaleFactor);
    }

    public void translateCanvas (Canvas canvas) {
        if (isViewInLeftBound()) {
            canvasTranslate.setX(0.f);
            previousCanvasTranslate.setX(0.f);
        } else if (isViewInRightBound()) {
            canvasTranslate.setX((1 - canvasScaleFactor) * displaySize.getX());
            previousCanvasTranslate.setX(canvasTranslate.getX());
        }

        if (isViewInTopBound()) {
            canvasTranslate.setY(0.f);
            previousCanvasTranslate.setY(0.f);
        } else if (isViewInBottomBound()) {
            canvasTranslate.setY((1 - canvasScaleFactor) * displaySize.getY());
            previousCanvasTranslate.setY(canvasTranslate.getY());
        }

        canvas.translate(canvasTranslate.getX() / canvasScaleFactor, canvasTranslate.getY() / canvasScaleFactor);
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
        Vector2<Float> position = getScaledPositionOfEvent(e);
        Tool tool = this.getTool();

        if (this.snipetisnapActivated && !FreeHandTool.class.isAssignableFrom(this.getTool().getClass())) {
            position = getGridSnippedPositionOfEvent(position);
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            tool.onStart(position);
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            tool.onMove(position);
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            tool.onEnd(position);
        }

        if (tool.isCanvasTranslateAllowed()) {
            handleCanvasTranslateGestures(e);
        }

        this.postInvalidate();
    }

    public void handleCanvasTranslateGestures (MotionEvent e) {
        switch (e.getAction()) {
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

    public Vector2<Float> getScaledPositionOfEvent (MotionEvent event) {
        Vector2<Float> result = new Vector2<>(0f, 0f);
        result.setX((event.getX() - this.previousCanvasTranslate.getX()) / canvasScaleFactor);
        result.setY((event.getY() - this.previousCanvasTranslate.getY()) / canvasScaleFactor);
        return result;
    }

    public boolean isGridSnippingActivated () {
        return this.snipetisnapActivated;
    }

    public void setSnippingActivate (boolean snips) {
        this.snipetisnapActivated = snips;
    }

    private class ZoomGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale (ScaleGestureDetector detector) {
            if (!getTool().isCanvasTranslateAllowed()) {
                return true;
            }
            setCanvasScaleFactor(detector.getScaleFactor() * canvasScaleFactor);
            return true;
        }
    }

    /**
     * Makes pretty fucking ugly thumbnails. Needs some work.
     * @return Thumbnail Bitmap
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(85, 85, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(0.25f, 0.25f); // Magic variables.
        drawElements(canvas);
        invalidate();
        return bitmap;
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
