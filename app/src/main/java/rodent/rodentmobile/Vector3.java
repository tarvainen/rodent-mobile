package rodent.rodentmobile;

/**
 * Created by Atte on 22.10.2015.
 */
public class Vector3<T> extends Vector2 {
    private T z;

    public Vector3 () {
        super();
    }

    public Vector3 (T x, T y, T z) {
        super(x, y);
        this.z = z;
    }

    public T getZ () {
        return this.z;
    }

    public void setZ (T z) {
        this.z = z;
    }

}
