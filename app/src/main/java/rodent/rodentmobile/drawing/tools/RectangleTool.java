package rodent.rodentmobile.drawing.tools;

import java.util.List;

import rodent.rodentmobile.drawing.helpers.AnchorPoint;
import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.drawing.shapes.PolygonShape;
import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Atte on 23.10.2015.
 */
public class RectangleTool extends Tool {

    AnchorPoint p1;
    AnchorPoint p2;
    AnchorPoint p3;
    AnchorPoint p4;

    public RectangleTool () {
        super();
        this.clear();
    }

    public RectangleTool (List<Shape> container) {
        super(container);
        this.clear();
    }

    @Override
    public void onStart(Vector2<Float> position) {
        this.setStartCornerPositionToEventPosition(position);
        this.setBusy(true);
    }

    @Override
    public void onMove(Vector2<Float> position) {
        this.setEndCornerPositionToEventPosition(position);
        this.getShape().update();
    }

    @Override
    public void onPress (Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        this.setEndCornerPositionToEventPosition(position);
        this.getShapeContainer().add(this.getShape());
        this.getShape().update();
        this.getShape().setReady(true);
        this.clear();
        this.setBusy(false);
    }

    @Override
    public void clear() {
        this.setShape(new PolygonShape());
    }

    public void setStartCornerPositionToEventPosition (Vector2<Float> position) {
        this.p1 = new AnchorPoint(position.getX(), position.getY());
        this.p2 = new AnchorPoint(position.getX(), position.getY());
        this.p3 = new AnchorPoint(position.getX(), position.getY());
        this.p4 = new AnchorPoint(position.getX(), position.getY());

        ((PolygonShape) this.getShape()).addPoint(p1);
        ((PolygonShape) this.getShape()).addPoint(p2);
        ((PolygonShape) this.getShape()).addPoint(p3);
        ((PolygonShape) this.getShape()).addPoint(p4);
    }

    public void setEndCornerPositionToEventPosition (Vector2<Float> position) {
        this.p2.setX(position.getX());
        this.p3.setX(position.getX());
        this.p3.setY(position.getY());
        this.p4.setY(position.getY());
    }
}
