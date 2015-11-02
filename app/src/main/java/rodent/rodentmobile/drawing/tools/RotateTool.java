package rodent.rodentmobile.drawing.tools;

import android.util.Log;

import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.utilities.VectorMath;

/**
 * Created by Atte on 02/11/15.
 */
public class RotateTool extends MoveTool {
    Vector2<Float> currentPoint;
    Shape currentShape;

    Vector2<Float> lastPosition;
    boolean touchedAnchorPoint;


    int selectedCorner;

    public RotateTool () {
        super();
        this.clear();
        this.touchedAnchorPoint = false;
        this.selectedCorner = 0;
    }

    @Override
    public void onStart(Vector2<Float> position) {
        touchedAnchorPoint = handleAnchorPointTouching(position);
        lastPosition = position;
    }

    @Override
    public void onMove(Vector2<Float> position) {
        Vector2<Float> delta = VectorMath.substract(position, lastPosition);
        Log.d("delta", delta.getX() + " " + delta.getY());

        if (this.currentPoint != null) {
            currentPoint.setX(position.getX());
            currentPoint.setY(position.getY());
        }

        if (this.currentShape != null) {
            this.currentShape.scale(this.selectedCorner, delta);
            this.currentShape.update();
        }

        lastPosition = position;
    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        if (!touchedAnchorPoint) {
            this.deselectAll();
            this.selectLimitedAmountOfElementsAtPosition(position, 1);
        }
        this.currentPoint = null;
    }

    @Override
    public void clear() {
        this.currentPoint = null;
    }

    public boolean handleAnchorPointTouching (Vector2<Float> position) {
        this.currentShape = getSelectedShape();
        if (this.currentShape == null) {
            return false;
        }

        PolylineShape shape;
        if (!PolylineShape.class.isAssignableFrom(currentShape.getClass())) {
            return false;
        }
        shape = (PolylineShape) currentShape;

        int i = 0;
        for (Vector2<Float> point : shape.getBoundingBox().getCorners()) {
            if (VectorMath.getDistanceBetween(position, point) < Shape.SELECTION_RADIUS) {
                this.selectedCorner = i + 2;
                this.currentPoint = point;
                return true;
            }
            i++;
        }

        return false;

    }

    public Shape getSelectedShape () {
        for (Shape shape : this.getShapeContainer()) {
            if (shape.isSelected()) {
                return shape;
            }
        }
        return null;
    }
}
