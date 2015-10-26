package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by attetarvainen on 26/10/15.
 */
public class AnchorPoint extends Shape {

    public AnchorPoint() {
        super();
    }

    public AnchorPoint (Vector2<Float> position) {
        super();
        this.setPosition(position);
    }

    public float getX () {
        return this.getPosition().getX();
    }

    public float getY () {
        return this.getPosition().getY();
    }

    @Override
    public void draw(Canvas canvas) {
        Vector2<Float> point = this.getPosition();
        RectF rect = new RectF(point.getX() - 1f, point.getY() - 1f, point.getX() + 1f, point.getY() + 1f);
        canvas.drawArc(rect, 0f, 360f, false, this.getPaint());
    }
}
