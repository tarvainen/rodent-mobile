package rodent.rodentmobile;

import java.util.List;

/**
 * Created by Atte on 24.10.2015.
 */
public class PolyLineTool extends Tool {


    private Vector2<Float> pointPosition;

    public PolyLineTool () {
        super();
        this.setBusy(true);
        this.clear();
    }

    @Override
    public void onStart(Vector2<Float> position) {
        this.pointPosition = position;
        ((PolylineShape)this.getShape()).addPoint(new AnchorPoint(position));
        if (((PolylineShape)this.getShape()).getPoints().size() == 1) {
            ((PolylineShape) this.getShape()).addPoint(new AnchorPoint(position));
        }
        this.getShape().setSelected(true);
    }

    @Override
    public void onMove(Vector2<Float> position) {
        this.pointPosition = position;
        updateLastPoint();
    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        this.pointPosition = position;
        updateLastPoint();
    }

    private void updateLastPoint () {
        List<AnchorPoint> points = ((PolylineShape)this.getShape()).getPoints();
        points.remove(points.size() - 1);
        points.add(new AnchorPoint(this.pointPosition));
        ((PolylineShape)this.getShape()).setPoints(points);
    }

    @Override
    public void onDeactivation () {
        this.getShapeContainer().add(this.getShape());
        this.getShape().setSelected(false);
        this.setBusy(false);
        this.clear();
    }

    @Override
    public void clear() {
        this.setShape(new PolylineShape());
        this.pointPosition = new Vector2<>(0f, 0f);
    }
}
