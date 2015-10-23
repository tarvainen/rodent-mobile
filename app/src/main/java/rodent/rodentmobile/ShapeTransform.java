package rodent.rodentmobile;

import java.util.Vector;

/**
 * Created by Atte on 22.10.2015.
 */
public class ShapeTransform {

    private Vector2<Float> position;
    private Vector2<Float> size;

    public ShapeTransform () {
        this.position = new Vector2<>(0f, 0f);
        this.size = new Vector2<>(0f, 0f);
    }

    public Vector2<Float> getPosition () {
        return this.position;
    }

    public void setPosition (Vector2<Float> position) {
        this.position = position;
    }

    public Vector2<Float> getSize () {
        return this.size;
    }

    public void setSize (Vector2<Float> size) {
        this.size = size;
    }


}
