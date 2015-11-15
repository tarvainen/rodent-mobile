package rodent.rodentmobile.data;

import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.drawing.shapes.Paper;
import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.RodentFile;

/**
 * Created by Atte on 15/11/15.
 */
public class GCodeParser {

    public static RodentFile parseFileFromGCode (String filename, List<String> gcode) throws InvalidGCodeException {

        List<Shape> shapes = createShapesFromCode(gcode);
        Log.d("shapelen", shapes.size() + "");
        Paper paper = new Paper();

        RodentFile file = new RodentFile(filename);
        file.setPaper(paper);
        file.setShapes(shapes);

        return file;
    }

    private static List<Shape> createShapesFromCode(List<String> gcode) throws InvalidGCodeException{
        List<Shape> shapes = new ArrayList<>();

        float z = Float.MAX_VALUE;

        List<GCode> shapeRows = new ArrayList<>();

        GCode last = null;
        for (int i = 0; i < gcode.size(); i++) {
            String row = gcode.get(i);
            if (isUsefulCodeRow(row)) {
                GCode code = parseGCodeFromRow(row);

                Log.d("useful", code.getX() + " " + code.getY());
                if (code.isZ()) {
                    if (z != code.getZ()) {
                        if (shapeRows.size() > 0) {
                            shapes.add(PolylineShape.fromGCodeList(shapeRows));
                            shapeRows.clear();
                        }
                    }
                    z = code.getZ();
                }
                if (z < 0.f) {

                    shapeRows.add(code);
                }

                last = code;
            }

        }

        return shapes;
    }

    private static boolean isUsefulCodeRow (String code) {
        code = code.toUpperCase();
        return code.contains("G00") || code.contains("G01");
    }

    private static GCode parseGCodeFromRow (String row) throws InvalidGCodeException {
        return GCode.fromString(row);
    }


}
