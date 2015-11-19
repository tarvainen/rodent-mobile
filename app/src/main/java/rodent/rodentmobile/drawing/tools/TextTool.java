package rodent.rodentmobile.drawing.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import rodent.rodentmobile.R;
import rodent.rodentmobile.drawing.helpers.AnchorPoint;
import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Teemu on 16.11.2015.
 *
 */
public class TextTool extends Tool {

    private Activity activity;

    private Path path = new Path();
    private Paint paint = new Paint();
    private String text = "";
    private float textsize = 0f;
    private Vector2<Float> position;

    private final int SAMPLES = 30; // Points per letter.
    private final float SPACING = 2f; // Spacing between letters.

    public TextTool(Activity activity) {
        this.clear();
        this.activity = activity;
    }

    private void createDialog() {
        View view = activity.getLayoutInflater().inflate(R.layout.texttool_dialog, null);
        final EditText textfield = (EditText) view.findViewById(R.id.editText_text);
        final EditText sizefield = (EditText) view.findViewById(R.id.editText_size);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle("Text tool");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text = textfield.getText().toString();
                textsize = Float.valueOf(sizefield.getText().toString());
                create();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void create() {
        paint.setTextSize(textsize);

        // Track the end coordinate of the last letter.
        float endX = position.getX();

        // Go through every letter.
        int strlen = text.length();
        for (int n = 0; n < strlen; n++) {
            ArrayList<Vector2<Float>> points = new ArrayList<>();

            // Create path object from string.
            paint.getTextPath(text, n, n + 1, endX + SPACING, position.getY(), path);
            path.close();
            PathMeasure pm = new PathMeasure(path, false);

            // Get points from along the path.
            float pathlen = pm.getLength();
            float currentpos = 0f;
            float delta = pathlen / SAMPLES;
            for (int i = 0; i < SAMPLES; i++) {
                float[] point = new float[2];
                pm.getPosTan(currentpos, point, null);
                points.add(new Vector2<>(point[0], point[1]));
                currentpos += delta;
                if (point[0] > endX) endX = point[0];
            }
            points.add(points.get(0)); // Close the path.

            // Add points to PolyLineShape.
            for (Vector2<Float> p : points) {
                ((PolylineShape) getShape()).addPoint(new AnchorPoint(p));
            }
            getShapeContainer().add(getShape());
            clear();
        }
    }

    @Override
    public void clear() {
        setShape(new PolylineShape());
    }

    @Override
    public void onStart(Vector2<Float> position) {
        this.position = position;
        createDialog();
    }

    @Override
    public void onMove(Vector2<Float> position) {

    }

    @Override
    public void onPress(Vector2<Float> position) {

    }

    @Override
    public void onEnd(Vector2<Float> position) {

    }
}
