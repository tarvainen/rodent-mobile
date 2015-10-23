package rodent.rodentmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Atte on 23/10/15.
 */
public class RectangleShape extends PolygonShape {

    private Vector2<Float> startPosition;
    private Vector2<Float> endPosition;

    public RectangleShape () {
        super();
        this.startPosition = new Vector2<>(0f, 0f);
        this.endPosition = new Vector2<>(0f, 0f);
    }

    public void setStartCornerPosition (Vector2<Float> position) {
        this.startPosition = position;
    }

    public void setEndCornerPosition (Vector2<Float> position) {
        this.endPosition = position;
        this.setRealCornerValues();
    }

    @Override
    public void draw (Canvas canvas) {
        canvas.drawRect(this.getPosition().getX(), this.getPosition().getY(), this.getSize().getX(), this.getSize().getY(), this.getPaint());
    }

    private void setRealCornerValues () {
        this.setRealXValues();
        this.setRealYValues();
    }

    private void setRealXValues () {
        if (this.startPosition.getX() > this.endPosition.getX()) {
            this.getPosition().setX(this.endPosition.getX());
            this.getSize().setX(this.startPosition.getX());
        } else {
            this.getPosition().setX(this.startPosition.getX());
            this.getSize().setX(this.endPosition.getX());
        }
    }

    private void setRealYValues () {
        if (this.startPosition.getY() > this.endPosition.getY()) {
            this.getPosition().setY(this.endPosition.getY());
            this.getSize().setY(this.startPosition.getY());
        } else {
            this.getPosition().setY(this.startPosition.getY());
            this.getSize().setY(this.endPosition.getY());
        }
    }

}
