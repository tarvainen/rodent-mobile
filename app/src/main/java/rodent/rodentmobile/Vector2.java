package rodent.rodentmobile;

/**
 * Created by Atte on 22.10.2015.
 */
public class Vector2<T> {

    private T x;
    private T y;

    public Vector2 () {

    }

    public Vector2 (T x, T y) {
        this.x = x;
        this.y = y;
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
