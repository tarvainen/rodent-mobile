package rodent.rodentmobile;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Atte on 22.10.2015.
 */
public class DrawingBoard extends View {

    private List<Shape> elements;
    private Tool tool;
    private Paper paper;

    public DrawingBoard (Context context) {
        super(context);
        this.elements = new LinkedList<>();
        this.paper = new Paper();
    }

    public DrawingBoard (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.elements = new LinkedList<>();
        this.paper = new Paper();
    }

    public Paper getPaper () {
        return this.paper;
    }

    public List<Shape> getDrawableElements () {
        return this.elements;
    }

    public void addDrawableElement (Shape shape) {
        this.elements.add(shape);
    }

    public void setTool (Tool tool) {
        this.tool = tool;
    }

    public Tool getTool () {
        return this.tool;
    }

    public boolean isToolBusy () {
        return this.tool.isBusy();
    }

    public void drawBackgroundPaper (Canvas canvas) {
        this.paper.draw(canvas);
    }

    public void drawBusyElement (Canvas canvas) {
        this.getTool().getShape().draw(canvas);
    }

    public void drawElements (Canvas canvas) {
        for(Shape shape : this.elements) {
            shape.draw(canvas);
        }
    }

    public void changeTool(Tool tool) {
        this.tool = tool;
        tool.setShapeContainer(this.elements);
        postInvalidate();
    }

    public Vector2<Float> getGridSnippedPositionOfEvent (Vector2<Float> position) {
        Vector2<Float> result = new Vector2<>(0f, 0f);
        float mill = this.getPaper().getMillisInPx();
        result.setX(scaleValueToGrid(position.getX(), mill));
        result.setY(scaleValueToGrid(position.getY(), mill));
        return result;
    }

    public float scaleValueToGrid (float value, float millisInPx) {
        return ((int) (value / millisInPx)) * millisInPx;
    }

    @Override
    public void onDraw (Canvas canvas) {
        this.drawElements(canvas);
    }

}
