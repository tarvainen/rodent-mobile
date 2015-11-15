package rodent.rodentmobile.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.drawing.helpers.AnchorPoint;
import rodent.rodentmobile.drawing.shapes.Paper;
import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.RodentFile;
import rodent.rodentmobile.utilities.Vector2;

/**
 * Created by Atte on 15/11/15.
 */
public class GCodeParser {

    public static RodentFile parseFileFromGCode (String filename, List<String> gcode) throws InvalidGCodeException {

        List<Shape> shapes = createShapesFromCode(gcode);

        RodentFile file = new RodentFile(filename);
        file.setRendered(false);

        file.setShapes(shapes);

        return file;
    }

    private static List<Shape> createShapesFromCode(List<String> gcode) throws InvalidGCodeException{
        List<Shape> shapes = new ArrayList<>();

        float z = Float.MAX_VALUE;

        List<GCode> shapeRows = new ArrayList<>();

        GCode last = new GCode();

        for (int i = 0; i < gcode.size(); i++) {
            String row = gcode.get(i);
            if (isUsefulCodeRow(row)) {
                GCode code = parseGCodeFromRow(row);

                if (!code.isX()) {
                    code.setX(last.getX());
                }
                if (!code.isY()) {
                    code.setY(last.getY());
                }

                if (code.isZ()) {
                    if (z != code.getZ()) {
                        if (shapeRows.size() > 0) {
                            shapes.add(PolylineShape.fromGCodeList(shapeRows));
                            shapeRows.clear();
                        }

                    }
                    z = code.getZ();
                }

                shapeRows.add(code);

                last = code;
            }

        }

        return shapes;
    }

    private static boolean isUsefulCodeRow (String code) {
        code = code.toUpperCase();
        return code.contains("G00") || code.contains("G01") || code.contains("G0") || code.contains("G1");
    }

    private static GCode parseGCodeFromRow (String row) throws InvalidGCodeException {
        return GCode.fromString(row);
    }


}
