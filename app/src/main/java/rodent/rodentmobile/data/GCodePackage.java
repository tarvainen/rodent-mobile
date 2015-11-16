package rodent.rodentmobile.data;

import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Atte on 16/11/15.
 */
public class GCodePackage {

    private String[] lines;
    private Vector2<Float> xValues;
    private Vector2<Float> yValues;
    private Vector2<Float> zValues;

    {
        lines = null;
        xValues = new Vector2<>(0f, 0f);
        yValues = new Vector2<>(0f, 0f);
        zValues = new Vector2<>(0f, 0f);
    }

    public GCodePackage () {

    }

    public void setLines (String[] lines) {
        this.lines = lines;
    }

    public void setXValues (Vector2<Float> values) {
        this.xValues = values;
    }

    public void setXValues (float xMin, float xMax) {
        this.xValues = new Vector2<>(xMin, xMax);
    }

    public void setYValues (Vector2<Float> values) {
        this.yValues = values;
    }

    public void setYValues (float yMin, float yMax) {
        this.yValues = new Vector2<>(yMin, yMax);
    }

    public void setZValues (Vector2<Float> values) {
        this.zValues = values;
    }

    public void setZValues (float zMin, float zMax) {
        this.zValues = new Vector2<>(zMin, zMax);
    }

    public String[] getLines () {
        return this.lines;
    }

    public Vector2<Float> getXValues () {
        return this.xValues;
    }

    public Vector2<Float> getYValues () {
        return this.yValues;
    }

    public Vector2<Float> getZValues () {
        return this.zValues;
    }

}
