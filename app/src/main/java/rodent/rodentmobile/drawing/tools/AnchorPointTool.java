package rodent.rodentmobile.drawing.tools;

import rodent.rodentmobile.drawing.helpers.AnchorPoint;
import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.utilities.VectorMath;
import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Atte on 26/10/15.
 */
public class AnchorPointTool extends MoveTool {

    AnchorPoint currentPoint;
    Shape currentShape;

    boolean touchedAnchorPoint;

    public AnchorPointTool() {
        super();
        this.clear();
        this.touchedAnchorPoint = false;
    }

    @Override
    public void onStart(Vector2<Float> position) {
        touchedAnchorPoint = handleAnchorPointTouching(position);
    }

    @Override
    public void onMove(Vector2<Float> position) {
        if (this.currentPoint != null) {
            currentPoint.setX(position.getX());
            currentPoint.setY(position.getY());
        }

        if (this.currentShape != null) {
            this.currentShape.update();
        }

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

        for (AnchorPoint point : shape.getPoints()) {
            if (VectorMath.getDistanceBetween(position, point) < Shape.SELECTION_RADIUS) {
                this.currentPoint = point;
                return true;
            }
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
