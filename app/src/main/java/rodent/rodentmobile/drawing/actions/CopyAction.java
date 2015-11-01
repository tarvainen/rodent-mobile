package rodent.rodentmobile.drawing.actions;

import java.util.List;

import rodent.rodentmobile.utilities.SerialCloner;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Atte on 01/11/15.
 */
public class CopyAction extends ShapeAction {

    public CopyAction () {
        super();
    }

    public CopyAction (List<Shape> shapes) {
        super(shapes);
    }

    @Override
    public int execute () throws Exception {
        int j = this.getShapes().size();
        for (int i = 0; i < j; i++) {
            if (this.getShapes().get(i).isSelected()) {
                Shape s = SerialCloner.clone(this.getShapes().get(i));

                if (s == null) {
                    return -1;
                }

                this.getShapes().get(i).setSelected(false);
                s.setSelected(true);
                s.move(new Vector2<>(40f, 0f));
                this.getShapes().add(s);
            }
        }

        return 0;
    }

}
