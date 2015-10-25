package rodent.rodentmobile.filesystem;

import java.io.Serializable;
import java.util.List;

import rodent.rodentmobile.Shape;

/**
 * Created by Teemu on 25.10.2015.
 */
public abstract class MyFile implements Serializable{
    protected String filename;
    protected List<Shape> shapes;

    public MyFile() {}

    public MyFile(String filename, List shapes) {
        this.filename = filename;
        this.shapes = shapes;
    }

    public abstract void save();
    public abstract void load(String path);
    public List<Shape> getShapes() { return shapes; }
}
