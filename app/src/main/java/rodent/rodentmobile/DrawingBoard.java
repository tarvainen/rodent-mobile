package rodent.rodentmobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Atte on 22.10.2015.
 */
public class DrawingBoard extends View {

    private List<Shape> elements;


    public DrawingBoard (Context context) {
        super(context);
        this.elements = new LinkedList<>();
        this.elements.add(new Paper());
//        this.elements.add(new Line());
    }

    public DrawingBoard (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.elements = new LinkedList<>();
        this.elements.add(new Paper());
//        this.elements.add(new Line());
    }

    public List<Shape> getDrawableElements () {
        return this.elements;
    }

    public void addDrawableElement (Shape shape) {
        this.elements.add(shape);
    }

    @Override
    public void onDraw (Canvas canvas) {
        for(Shape shape : this.elements) {
            shape.draw(canvas);
        }
    }
}
