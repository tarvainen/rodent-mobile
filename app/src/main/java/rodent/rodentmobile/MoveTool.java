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
}
