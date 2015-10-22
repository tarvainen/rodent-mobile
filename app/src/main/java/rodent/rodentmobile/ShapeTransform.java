package rodent.rodentmobile;

import java.util.Vector;

/**
 * Created by Atte on 22.10.2015.
 */
public class ShapeTransform {

    private Vector3<Float> position;
    private Vector<Float> size;

    public ShapeTransform () {

    }

    public Vector3<Float> getPosition () {
        return this.position;
    }

    public void setPosition (Vector3<Float> position) {
        this.position = position;
    }

    public Vector<Float> getSize () {
        return this.size;
    }

    public void setSize (Vector<Float> size) {
        this.size = size;
    }


}
