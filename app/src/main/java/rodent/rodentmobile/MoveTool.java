package rodent.rodentmobile;

/**
 * Created by Atte on 24.10.2015.
 */
public class MoveTool extends Tool {

    public MoveTool () {
        super();
    }

    public MoveTool (boolean allowTranslate){
        super();
        this.setAllowCanvasTranslate(allowTranslate);
    }

    @Override
    public void onStart(Vector2<Float> position) {
        this.handleSelections(position);
    }

    @Override
    public void onMove(Vector2<Float> position) {

    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {

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
        if (this.getShapeContainer() == null) {
            return;
        }
        for (Shape shape : this.getShapeContainer()) {
            shape.setSelected(false);
        }
    }
}
