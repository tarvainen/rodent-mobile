package rodent.rodentmobile;

import android.util.Log;

import java.util.List;

/**
 * Created by Atte on 26/10/15.
 */
public class VectorMath {

    public static float getSquaredDistanceBetween (Vector2<Float> p1, Vector2<Float> p2) {
        return sqr(p1.getX() - p2.getX()) + sqr(p1.getY() - p2.getY());
    }

    public static float getDistanceBetween (Vector2<Float> p1, Vector2<Float> p2) {
        return (float) Math.sqrt(getSquaredDistanceBetween(p1, p2));
    }

    public static float getDistanceToSegmentSquared (Vector2<Float> point, Vector2<Float> segStart, Vector2<Float> segEnd) {
        float segmentLength = getSquaredDistanceBetween(segStart, segEnd);

        if (segmentLength == 0.0f) {
            return getSquaredDistanceBetween(point, segStart);
        }

        float perc = getDotProduct(substract(point, segStart), substract(segEnd, segStart));
        perc /= segmentLength;

        if (perc < 0) {
            return getSquaredDistanceBetween(point, segStart);
        }

        if (perc > 1) {
            return getSquaredDistanceBetween(point, segEnd);
        }

        float projX = segStart.getX() + perc * (segEnd.getX() - segStart.getX());
        float projY = segStart.getY() + perc * (segEnd.getY() - segStart.getY());
        Vector2<Float> projection = new Vector2<>(projX, projY);
        return getSquaredDistanceBetween(point, projection);
    }

    public static float getDistanceToSegment (Vector2<Float> point, Vector2<Float> segStart, Vector2<Float> segEnd) {
        return (float)Math.sqrt(getDistanceToSegmentSquared(point, segStart, segEnd));
    }

    public static float getDotProduct (Vector2<Float> p1, Vector2<Float> p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    public static Vector2<Float> substract (Vector2<Float> p1, Vector2<Float> p2) {
        return new Vector2<>(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Vector2<Float> min (List<AnchorPoint> points) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;

        for (Vector2<Float> point : points) {
            if (point.getX() < minX) {
                minX = point.getX();
            }
            if (point.getY() < minY) {
                minY = point.getY();
            }
        }

        return new Vector2<>(minX, minY);
    }

    public static Vector2<Float> max (List<AnchorPoint> points) {
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (Vector2<Float> point : points) {
            if (point.getX() > maxX) {
                maxX = point.getX();
            }
            if (point.getY() > maxY) {
                maxY = point.getY();
            }
        }

        return new Vector2<>(maxX, maxY);
    }

    public static Vector2<Float> sum (Vector2<Float> p1, Vector2<Float> p2) {
        return new Vector2<>(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }

    private static float sqr (float val) {
        return val * val;
    }

    public static Vector2<Float> getScaledPosition (Vector2<Float> min, Vector2<Float> max, Vector2<Float> target, Vector2<Float> amount) {
        float x = getScaledPosition(min.getX(), max.getX(), target.getX(), amount.getX());
        float y = getScaledPosition(min.getY(), max.getY(), target.getY(), amount.getY());
        return new Vector2<>(x, y);
    }

    private static float getScaledPosition (float min, float max, float target, float amount) {
        float len = max - min;
        float elLen = target - min;
        return target + (elLen / len) * amount;
    }


}
