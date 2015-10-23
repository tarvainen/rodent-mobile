package rodent.rodentmobile;

import java.util.List;

/**
 * Created by Atte on 23.10.2015.
 */
public class RectangleTool extends Tool {

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
        this.setEndCornerPositionToEventPosition(position);
        this.setBusy(true);
    }

    @Override
    public void onMove(Vector2<Float> position) {
        this.setEndCornerPositionToEventPosition(position);
    }

    @Override
    public void onPress (Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        this.setEndCornerPositionToEventPosition(position);
        this.getShapeContainer().add(this.getShape());
        this.clear();
        this.setBusy(false);
    }

    @Override
    public void clear() {
        this.setShape(new RectangleShape());
    }

    public void setStartCornerPositionToEventPosition (Vector2<Float> position) {
        ((RectangleShape) this.getShape()).setStartCornerPosition(new Vector2<>(position.getX(), position.getY()));
    }

    public void setEndCornerPositionToEventPosition (Vector2<Float> position) {
        ((RectangleShape) this.getShape()).setEndCornerPosition(new Vector2<>(position.getX(), position.getY()));
    }
}
