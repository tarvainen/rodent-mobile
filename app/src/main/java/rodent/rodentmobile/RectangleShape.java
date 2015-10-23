package rodent.rodentmobile;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by attetarvainen on 23/10/15.
 */
public class RectangleShape extends PolygonShape {

    public RectangleShape () {
        super();
    }

    public RectangleShape (float startX, float startY) {
        this.getTransform().setPosition(new Vector3<>(startX, startY, 0.f));
    }

    public void setWidth (float width) {
        this.getTransform().setSize(new Vector2<>(width, this.getTransform().getSize().getY()));
    }

    public float getWidth () {
        return this.getTransform().getSize().getX();
    }

    public void setHeight (float height) {
        this.getTransform().setSize(new Vector2<>(this.getTransform().getSize().getX(), height));
    }

    public float getHeight () {
        return this.getTransform().getSize().getY();
    }

    @Override
    public void draw (Canvas canvas) {
        generatePoints();
        super.draw(canvas);
    }

    private void generatePoints () {
        ArrayList<Vector2<Float>> points = new ArrayList<>();
        ShapeTransform transform = this.getTransform();
        Vector2<Float> position = transform.getPosition();
        Vector2<Float> size = transform.getSize();

        points.add(position);
        points.add(new Vector2<>(position.getX() + size.getX(), position.getY()));
        points.add(new Vector2<>(position.getX() + size.getX(), position.getY() + size.getY()));
        points.add(new Vector2<>(position.getX(), position.getY() + size.getY()));
    }


}
