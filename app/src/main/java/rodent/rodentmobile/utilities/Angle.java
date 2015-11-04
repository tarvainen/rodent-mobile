package rodent.rodentmobile.utilities;

import java.io.Serializable;

/**
 * Created by Atte on 02/11/15.
 */
public class Angle implements Serializable {

    private float angle;

    {
        this.angle = 0.0f;
    }

    public Angle () {

    }

    public static Angle fromDegrees (float degrees) {
        Angle angle = new Angle();
        angle.setDegrees(degrees);
        return angle;
    }

    public static Angle fromRadians (float radians) {
        Angle angle = new Angle();
        angle.setRadians(radians);
        return angle;
    }

    public static Angle getAngleBetween (Vector2<Float> a, Vector2<Float> b) {
        Angle angle = new Angle();
        float dX = b.getX() - a.getX();
        float dY = b.getY() - a.getY();
        angle.setRadians((float)Math.atan2(dY, dX));
        return angle;
    }

    public static float toRadians (float degrees) {
        return (float)(degrees * Math.PI / 180);
    }

    public static float toDegrees (float radians) {
        return (float)(radians * 180 / Math.PI);
    }

    public float getDegrees () {
        return Angle.toDegrees(this.angle);
    }

    public void setDegrees (float degrees) {
        this.angle = Angle.toRadians(degrees);
    }

    public float getRadians () {
        return this.angle;
    }

    public void setRadians (float radians) {
        this.angle = radians;
    }

    public void add (Angle angle) {
        this.angle += angle.getRadians();
    }

    public void substract (Angle angle) {
        this.angle -= angle.getRadians();
    }

}
