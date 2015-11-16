package rodent.rodentmobile.data;

import android.content.Context;
import android.content.res.Resources;

import java.text.DecimalFormat;

import rodent.rodentmobile.R;

/**
 * Created by Atte on 16/11/15.
 */
public class GCodeBuilder {

    private Context context;
    private DecimalFormat format;

    public GCodeBuilder (Context context) {
        this.context = context;
        this.format = new DecimalFormat("0.##");
    }

    private String getResourceString (int id) {
        return context.getString(id);
    }


    ///////////////////////////////
    // Coordinate value formatting
    ///////////////////////////////

    private String toXString (float x) {
        return "X" + format.format(x);
    }

    private String toYString (float y) {
        return "Y" + format.format(y);
    }

    private String toZString (float z) {
        return "Z" + format.format(z);
    }


    /////////////////////////////////
    // Positioning
    /////////////////////////////////

    public String getRapidPositioningToX (float x) {
        return getResourceString(R.string.rapid_positioning) + " " + toXString(x);
    }

    public String getRapidPositioningToY (float y) {
        return getResourceString(R.string.rapid_positioning) + " " + toYString(y);
    }

    public String getRapidPositioningToZ (float z) {
        return getResourceString(R.string.rapid_positioning) + " " + toZString(z);
    }

    public String getRapidPositioningToXY (float x, float y) {
        return getResourceString(R.string.rapid_positioning) + " " + toXString(x) + " " + toYString(y);
    }

    public String getRapidPositioningTo (float x, float y, float z) {
        return getResourceString(R.string.rapid_positioning) + " " + toXString(x) + " " + toYString(y) + " " + toZString(z);
    }


    //////////////////////////////////
    // Machine commands
    //////////////////////////////////

    public String getSpindleCounterwiseStartCommand () {
        return getResourceString(R.string.spindle_counterwise_start);
    }

    public String getSpindleStopCommand () {
        return getResourceString(R.string.spindle_stop);
    }

    public String getGoToHomeCommand () {
        return getResourceString(R.string.goto_home_positioning);
    }

    public String getPauseCommand () {
        return getResourceString(R.string.pause_job);
    }

}
