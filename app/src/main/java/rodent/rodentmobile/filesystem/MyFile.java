package rodent.rodentmobile.filesystem;

import java.io.Serializable;
import java.util.List;

import rodent.rodentmobile.Shape;

/**
 * Created by Teemu on 25.10.2015.
 *
 */
public abstract class MyFile implements Serializable{
    protected String filename;
    protected List<Shape> shapes;
    protected float millInPx;

    public MyFile() {
        this.millInPx = 1.0f;
    }

    public MyFile(String filename) {
        this.filename = filename;
        this.millInPx = 1.0f;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public void setMillInPx (float millInPx) {
        this.millInPx = millInPx;
    }

    public abstract void save();
    public abstract void load(String path);

    public List<Shape> getShapes() { return shapes; }

    public float getMillInPx () {
        return this.millInPx;
    }

    public String getFilename() { return filename; }
}
