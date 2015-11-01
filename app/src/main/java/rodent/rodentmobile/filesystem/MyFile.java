package rodent.rodentmobile.filesystem;

import java.io.Serializable;
import java.util.List;

import rodent.rodentmobile.Paper;
import rodent.rodentmobile.Shape;

/**
 * Created by Teemu on 25.10.2015.
 *
 */
public abstract class MyFile implements Serializable{
    protected String filename;
    protected List<Shape> shapes;
    protected Paper paper;

    public MyFile() {

    }

    public MyFile(String filename) {
        this.filename = filename;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public abstract void save();
    public abstract void load(String path);

    public List<Shape> getShapes() { return shapes; }

    public String getFilename() { return filename; }

    public void setPaper (Paper paper) {
        this.paper = paper;
    }

    public Paper getPaper () {
        return this.paper;
    }
}
