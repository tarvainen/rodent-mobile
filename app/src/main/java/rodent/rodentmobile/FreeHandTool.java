package rodent.rodentmobile;


/**
 * Created by Atte on 24.10.2015.
 */
public class FreeHandTool extends Tool {

    private Vector2<Float> lastMovePosition;

    public FreeHandTool () {
        super();
    }

    @Override
    public void onStart (Vector2<Float> position) {
        this.setBusy(true);
        this.lastMovePosition = position;
        this.clear();
        ((PolylineShape) this.getShape()).addPoint(new AnchorPoint(position));
        this.getShape().setSelected(true);
    }

    @Override
    public void onMove(Vector2<Float> position) {
        if (getDistanceBetween(position, lastMovePosition) > 10) {
            lastMovePosition = position;
            ((PolylineShape) this.getShape()).addPoint(new AnchorPoint(position));
        }
    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        this.getShapeContainer().add(this.getShape());
        this.getShape().setSelected(false);
        this.getShape().setReady(true);
        this.setBusy(false);
        this.clear();
    }

    @Override
    public void clear() {
        this.setShape(new PolylineShape());
    }

    public double getDistanceBetween (Vector2<Float> point1, Vector2<Float> point2) {
        double dx = point1.getX() - point2.getX();
        double dy = point1.getY() - point2.getY();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
