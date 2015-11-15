package rodent.rodentmobile.filesystem;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.drawing.shapes.Paper;
import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Teemu on 25.10.2015.
 *
 */
public abstract class MyFile implements Serializable {
    protected String filename;
    protected List<Shape> shapes;
    protected Paper paper;
    protected boolean rendered;

    {
        this.paper = new Paper();
        this.shapes = new ArrayList<>();
        this.filename = "";
        this.rendered = false;
    }

    public MyFile() {

    }

    public MyFile(String filename) {
        this.filename = filename;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public abstract void save() throws IOException;
    public abstract void load(String path) throws Exception;

    public List<Shape> getShapes() { return shapes; }

    public String getFilename() { return filename; }

    public void setPaper (Paper paper) {
        this.paper = paper;
    }

    public Paper getPaper () {
        return this.paper;
    }

    public void setRendered (boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isRendered () {
        return this.rendered;
    }
}
