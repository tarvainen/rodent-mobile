package rodent.rodentmobile.utilities;

import java.io.Serializable;

/**
 * Created by Atte on 22.10.2015.
 */
public class Vector2<T> implements Serializable{

    private T x;
    private T y;

    public Vector2 () {

    }

    public Vector2 (T x, T y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 (Vector2<T> element) {
        this.x = element.getX();
        this.y = element.getY();
    }

    public T getX () {
        return this.x;
    }

    public void setX (T x) {
        this.x = x;
    }

    public T getY () {
        return this.y;
    }

    public void setY (T y) {
        this.y = y;
    }


}
