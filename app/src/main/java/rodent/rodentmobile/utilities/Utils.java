package rodent.rodentmobile.utilities;

import java.math.BigDecimal;

/**
 * Created by Atte on 01/11/15.
 */
public class Utils {

    public static float round (float value, int decimals) {
        BigDecimal decimal = new BigDecimal(Float.toString(value));
        decimal = decimal.setScale(decimals, BigDecimal.ROUND_HALF_UP);
        return decimal.floatValue();
    }

    public static int map (int value, int maxDef, int newMax) {
        float part = (float) value / maxDef;
        return (int)(part * newMax);
    }

}
