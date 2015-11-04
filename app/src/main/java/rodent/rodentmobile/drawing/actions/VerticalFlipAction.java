package rodent.rodentmobile.drawing.actions;

import java.util.List;

import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Atte on 04/11/15.
 */
public class VerticalFlipAction extends ShapeAction {


    public VerticalFlipAction () {
        super();
    }

    public VerticalFlipAction (List<Shape> shapes) {
        super(shapes);
    }

    @Override
    public int execute () {
        int i = 0;
        for (Shape shape : this.getShapes()) {
            if (shape.isSelected()) {
                shape.flipVertically();
                shape.update();
                i++;
            }
        }

        return i;
    }
}
