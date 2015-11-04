package rodent.rodentmobile.drawing.tools;


import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.utilities.VectorMath;

/**
 * Created by Atte on 04/11/15.
 */
public class CircleTool extends Tool {

    private Vector2<Float> origo;

    public CircleTool () {
        super();
        this.clear();
    }

    @Override
    public void onStart (Vector2<Float> position) {
        this.setBusy(true);
        this.origo = position;
    }

    @Override
    public void onMove (Vector2<Float> position) {
        float radius = VectorMath.getDistanceBetween(position, origo);
        this.setShape(PolylineShape.asCircleShape(origo, radius));

    }

    @Override
    public void onEnd (Vector2<Float> position) {
        this.setBusy(false);
        if (VectorMath.getDistanceBetween(position, origo) > 1f) {
            this.getShapeContainer().add(this.getShape());
        }
    }

    @Override
    public void clear () {
        this.setShape(new PolylineShape());
    }

    @Override
    public void onPress (Vector2<Float> position) {

    }

    @Override
    public void onDeactivation () {
        this.getShape().setSelected(false);
        this.setBusy(false);
        this.getShape().setReady(true);
    }
}
