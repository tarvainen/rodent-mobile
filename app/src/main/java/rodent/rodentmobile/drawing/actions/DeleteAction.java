package rodent.rodentmobile.drawing.actions;


/**
 * Created by Atte on 01/11/15.
 */
public class DeleteAction extends ShapeAction {

    public DeleteAction () {
        super();
    }

    @Override
    public int execute () {
        int removed = 0;
        for (int i = 0; i < this.getShapes().size(); i++) {
            if (this.getShapes().get(i).isSelected()) {
                this.getShapes().remove(i);
                removed++;
                i--;
            }
        }

        return removed;
    }
}
