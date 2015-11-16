package rodent.rodentmobile.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.drawing.shapes.PolylineShape;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;
import rodent.rodentmobile.utilities.Vector2;
import rodent.rodentmobile.utilities.VectorMath;

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

        GCode last = new GCode("");

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
                    if (z != code.getZ()) { // if z value changes, the engraving shape ends
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

    public static GCode parseGCodeFromRow (String row) throws InvalidGCodeException {
        return GCode.fromString(row);
    }

    public static GCodePackage parseFileToGCode (Context context, MyFile file) {
        GCodeBuilder builder = new GCodeBuilder(context);

        GCodePackage pack = new GCodePackage();

        String gcode = getJobStartCommands(builder);

        Vector2<Float> xMaxes = new Vector2<>(0f, 0f);
        Vector2<Float> yMaxes = new Vector2<>(0f, 0f);
        Vector2<Float> zMaxes = new Vector2<>(0f, 0f);

        for (Shape shape : file.getShapes()) {
            gcode += shape.toGCode(file.getPaper());
            xMaxes = VectorMath.getBounds(xMaxes, shape.getRealBoundsX(file.getPaper()));
            yMaxes = VectorMath.getBounds(yMaxes, shape.getRealBoundsY(file.getPaper()));
            zMaxes = VectorMath.getBounds(zMaxes, shape.getRealBoundsZ(file.getPaper()));
        }

        gcode += getJobEndCommands(builder);

        pack.setLines(gcode.split("\n"));
        pack.setXValues(xMaxes);
        pack.setYValues(yMaxes);
        pack.setZValues(zMaxes);

        return pack;
    }

    private static String getJobStartCommands (GCodeBuilder builder) {
        String gcode = "";
        gcode += toLine(builder.getRapidPositioningToZ(1.f));
        gcode += toLine(builder.getSpindleCounterwiseStartCommand());
        return gcode;
    }

    private static String getJobEndCommands (GCodeBuilder builder) {
        String gcode = "";
        gcode += toLine(builder.getRapidPositioningToZ(1.f));
        gcode += toLine(builder.getSpindleStopCommand());
        gcode += toLine(builder.getGoToHomeCommand());
        return gcode;
    }

    private static String toLine (String command) {
        return command + "\n";
    }

}
