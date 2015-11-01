package rodent.rodentmobile.drawing.actions;

import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Atte on 01/11/15.
 */
public abstract class ShapeAction extends Action {

    private List<Shape> shapes;

    public ShapeAction () {
        super();
        this.shapes = new ArrayList<>();
    }

    public ShapeAction (List<Shape> shapes) {
        super();
        this.shapes = shapes;
    }

    public void setShapes (List<Shape> shapes) {
        this.shapes = shapes;
    }

    public List<Shape> getShapes () {
        return this.shapes;
    }
}
