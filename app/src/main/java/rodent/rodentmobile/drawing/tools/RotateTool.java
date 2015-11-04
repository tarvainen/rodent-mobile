package rodent.rodentmobile.drawing.tools;


import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.utilities.Angle;
import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Atte on 02/11/15.
 */
public class RotateTool extends MoveTool {
    Shape currentShape;

    Vector2<Float> lastPosition;
    Vector2<Float> pivotPoint;
    Angle prevAngle;
    boolean moved;

    public RotateTool () {
        super();
        this.clear();
        this.moved = false;
    }

    @Override
    public void onStart(Vector2<Float> position) {
        handleAnchorPointTouching(position);
        lastPosition = position;
        if (this.pivotPoint != null) {
            prevAngle = Angle.getAngleBetween(position, pivotPoint);
        }
        this.moved = false;
    }

    @Override
    public void onMove(Vector2<Float> position) {
        if (this.prevAngle == null) {
            return;
        }

        Angle angle = Angle.getAngleBetween(position, pivotPoint);
        prevAngle.substract(angle);

        if (this.currentShape != null) {
            this.currentShape.rotate(pivotPoint, prevAngle);
            this.currentShape.update();
        }

        lastPosition = position;
        prevAngle = angle;
    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        if (!moved) {
            this.deselectAll();
            this.selectLimitedAmountOfElementsAtPosition(position, 1);
        }

    }

    @Override
    public void clear() {

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

        shape.update();
        Vector2<Float> p = shape.getCenter();
        this.pivotPoint = new Vector2<>(p.getX(), p.getY());

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
