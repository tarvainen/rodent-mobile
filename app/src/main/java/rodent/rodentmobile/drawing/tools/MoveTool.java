package rodent.rodentmobile.drawing.tools;

import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.utilities.VectorMath;
import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Atte on 24.10.2015.
 */
public class MoveTool extends Tool {

    private Vector2<Float> lastMovePosition;
    private boolean moved;

    {
        this.lastMovePosition = new Vector2<>(0f, 0f);
        this.moved = false;
    }

    public MoveTool () {
        super();
    }

    public MoveTool (boolean allowTranslate){
        super();
        this.setAllowCanvasTranslate(allowTranslate);
    }

    @Override
    public void onStart(Vector2<Float> position) {
        this.lastMovePosition = position;
        this.moved = false;
    }

    @Override
    public void onMove(Vector2<Float> position) {
        this.moved = true;
        handleMovement(position);
        this.lastMovePosition = position;
    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {
        if (!this.moved) {
            this.handleSelections(position);
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void onDeactivation () {
        this.deselectAll();
    }

    public void handleSelections (Vector2<Float> position) {
        selectLimitedAmountOfElementsAtPosition(position, Integer.MAX_VALUE);
    }

    public void selectLimitedAmountOfElementsAtPosition (Vector2<Float> position, int limit) {
        int selected = 0;
        for (Shape shape : this.getShapeContainer()) {
            if (shape.wasTouched(position)) {
                selected++;
                if (selected >= limit) {
                    return;
                }
            }
        }
    }

    public void deselectAll () {
        for (Shape shape : this.getShapeContainer()) {
            shape.setSelected(false);
        }
    }

    private void handleMovement (Vector2<Float> position) {

        Vector2<Float> dPosition = VectorMath.substract(position, lastMovePosition);
        for (Shape shape : this.getShapeContainer()) {
            if (shape.isSelected()) {
                shape.move(dPosition);
            }
        }
    }

}
