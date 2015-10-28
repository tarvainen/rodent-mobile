package rodent.rodentmobile;

import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Teemu on 28.10.2015.
 *
 */
public class CustomPaint extends Paint implements Serializable {

    public CustomPaint() {
        super();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Log.d("CustomPaint", "writeObject");
        out.writeInt(getColor());
        out.writeBoolean(isAntiAlias());
        out.writeFloat(getStrokeWidth());
    }

    private void readObject(ObjectInputStream in) throws IOException {
        Log.d("CustomPaint", "readObject");
        setColor(in.readInt());
        setAntiAlias(in.readBoolean());
        setStrokeWidth(in.readFloat());
        setStyle(Style.STROKE);
    }
}
