package rodent.rodentmobile;

import android.view.MotionEvent;

import java.util.List;

/**
 * Created by Atte on 23.10.2015.
 */
public class LineTool extends Tool {

    public LineTool () {
        super();
        this.clear();
    }

    public LineTool (List<Shape> container) {
        super(container);
        this.clear();
    }

    @Override
    public void onStart (Vector2<Float> position) {
        this.setStartPointToEventPosition(position);
        this.setEndPointToEventPosition(position);
        this.setBusy(true);
        this.getShape().setSelected(true);
    }

    @Override
    public void onMove(Vector2<Float> position) {
        this.setEndPointToEventPosition(position);
    }

    @Override
    public void onPress (Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        this.setBusy(false);
        this.setEndPointToEventPosition(position);
        if (!isEndPointSameThanStartPoint()) {
            this.getShapeContainer().add(this.getShape());
        }
        this.getShape().setSelected(false);
        this.getShape().setReady(true);
        this.clear();
    }

    @Override
    public void clear() {
        this.setShape(new LineShape());
    }

    private void setStartPointToEventPosition (Vector2<Float> position) {
        ((LineShape) this.getShape()).setStartPoint(new Vector2<>(position.getX(), position.getY()));
    }

    private void setEndPointToEventPosition (Vector2<Float> position) {
        ((LineShape) this.getShape()).setEndPoint(new Vector2<>(position.getX(), position.getY()));
    }

    private boolean isEndPointSameThanStartPoint () {
        LineShape shape = (LineShape) this.getShape();
        return VectorMath.getDistanceBetween(shape.getStartPoint(), shape.getEndPoint()) < 0.01f;
    }
}
