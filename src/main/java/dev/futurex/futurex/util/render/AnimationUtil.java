package dev.futurex.futurex.util.render;

/**
 * @author memessz & linustouchtips
 * @since 12/17/2020
 */

public class AnimationUtil {

    public static float moveTowards(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;

        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        }

        else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
    }

    public static double moveTowards(double target, double current, double speed) {
        boolean larger = target > current;

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1D)
            factor = 0.1D;

        if (larger)
            current += factor;
        else
            current -= factor;

        return current;
    }

    public static double expand(double target, double current, double speed) {
        if (current > target)
            current = target;

        if (current < -target)
            current = -target;

        current += speed;

        return current;
    }
}
