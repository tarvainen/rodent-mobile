package rodent.rodentmobile.filesystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import rodent.rodentmobile.drawing.shapes.Paper;
import rodent.rodentmobile.drawing.shapes.Shape;

/**
 * Created by Teemu on 25.10.2015.
 *
 */
public abstract class MyFile implements Serializable {
    protected Paper paper;
    protected String path;
    protected String filename;
    protected boolean rendered;
    protected List<Shape> shapes;
    protected transient Bitmap bitmap;

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

    public String getPath() { return path; }

    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    public Bitmap getBitmap() { return bitmap; }

    protected void saveThumbnail() {
        FileOutputStream out;
        try {
            File f = new File(path + ".png");
            out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            // Do nothing.
        }
    }

    protected void loadThumbnail() {
        bitmap = BitmapFactory.decodeFile(path + ".png");
    }
}
