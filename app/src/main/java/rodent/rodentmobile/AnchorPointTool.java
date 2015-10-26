package rodent.rodentmobile;

import android.util.Log;

/**
 * Created by Atte on 26/10/15.
 */
public class AnchorPointTool extends MoveTool {

    AnchorPoint currentPoint;
    Shape currentShape;

    public AnchorPointTool() {
        super();
        this.clear();
    }

    @Override
    public void onStart(Vector2<Float> position) {
        this.deselectAll();
        super.onStart(position);
        handleAnchorPointTouching(position);
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
        this.currentPoint = null;
    }

    @Override
    public void clear() {
        this.currentPoint = null;
    }

    public void handleAnchorPointTouching (Vector2<Float> position) {
        this.currentShape = getSelectedShape();
        if (this.currentShape == null) {
            return;
        }

        PolylineShape shape;
        if (!PolylineShape.class.isAssignableFrom(currentShape.getClass())) {
            return;
        }
        shape = (PolylineShape) currentShape;

        for (AnchorPoint point : shape.getPoints()) {
            if (VectorMath.getDistanceBetween(position, point) < Shape.SELECTION_RADIUS) {
                this.currentPoint = point;
                return;
            }
        }

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
