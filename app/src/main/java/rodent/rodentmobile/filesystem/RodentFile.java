package rodent.rodentmobile.filesystem;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Teemu on 25.10.2015.
 *
 */
public class RodentFile extends MyFile {

    public RodentFile(String filename) {
        super(filename + ".rodent");
    }

    public RodentFile(File file) {
        load(file.getPath());
    }

    @Override
    public void save() {
        try {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/rodent");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, filename);
            FileOutputStream outStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(outStream);
            out.writeObject(this);
            out.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String path) {
        try {
            File file = new File(path);
            FileInputStream inStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(inStream);
            RodentFile tmp = (RodentFile) in.readObject();
            filename = tmp.filename;
            shapes = tmp.shapes;
            in.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
