package rodent.rodentmobile.data;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import rodent.rodentmobile.exceptions.InvalidGCodeException;

/**
 * Created by Atte on 15/11/15.
 */
public class GCode {

    private String code;

    private float x;
    private float y;
    private float z;
    private float g;
    private float r;

    private boolean isx;
    private boolean isy;
    private boolean isz;
    private boolean isg;
    private boolean isr;

    {
        x = 0.f;
        y = 0.f;
        z = 0.f;
        g = 0.f;
        r = 0.f;

        isx = false;
        isy = false;
        isz = false;
        isg = false;
        isr = false;
    }

    public GCode (String code) {
        this.code = code;
    }

    public void setX (float x) {
        this.x = x;
        this.isx = true;
    }

    public void setY (float y) {
        this.y = y;
        this.isy = true;
    }

    public void setZ (float z) {
        this.z = z;
        this.isz = true;
    }

    public void setG (float g) {
        this.g = g;
        this.isg = true;
    }

    public void setR (float r) {
        this.r = r;
        this.isr = true;
    }

    public float getX () {
        return this.x;
    }

    public float getY () {
        return this.y;
    }

    public float getZ () {
        return this.z;
    }

    public float getG () {
        return this.g;
    }

    public float getR () {
        return this.r;
    }

    public boolean isX () {
        return this.isx;
    }

    public boolean isY () {
        return this.isy;
    }

    public boolean isZ () {
        return this.isz;
    }

    public boolean isG () {
        return this.isg;
    }

    public boolean isR () {
        return this.isr;
    }


    public static GCode fromString (String codeRow) throws InvalidGCodeException {
        GCode code = new GCode(codeRow);

        String[] elements = codeRow.split(" ");

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].contains("G")) {
                code.setG(getValueFromString(elements[i]));
            } else if (elements[i].contains("X")) {
                code.setX(getValueFromString(elements[i]));
            } else if (elements[i].contains("Y")) {
                code.setY(getValueFromString(elements[i]));
            } else if (elements[i].contains("Z")) {
                code.setZ(getValueFromString(elements[i]));
            } else if (elements[i].contains("R")) {
                code.setR(getValueFromString(elements[i]));
            }
        }

        return code;
    }

    private static float getValueFromString (String gcode) throws InvalidGCodeException {
        String value = gcode.replaceAll("[^0-9.]", "");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.##");
        format.setDecimalFormatSymbols(symbols);

        try {
            float result = format.parse(value).floatValue();
            if (gcode.contains("Z")) {
                if (gcode.contains("-")) {
                    result *= -1;
                }
            }
            return result;
        } catch (ParseException ex) {
            throw new InvalidGCodeException();
        }
    }


    public boolean is (String str) {
        return this.code.contains(str);
    }

    public float get (String value) {
        String codes[] = this.code.split(" ");
        for (String code : codes) {
            if (code.startsWith(value)) {
                return parseValueFromCode(code.substring(value.length(), code.length()));
            }
        }
        return -1f;
    }

    private float parseValueFromCode (String code) {
        float result = -1f;
        try {
            result = Float.parseFloat(code);
        } catch (Exception ex) {

        }

        return result;
    }
}
