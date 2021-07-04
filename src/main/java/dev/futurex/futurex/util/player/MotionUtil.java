package dev.futurex.futurex.util.player;

import dev.futurex.futurex.event.events.player.MoveEvent;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.util.client.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class MotionUtil implements MixinInterface {

    public static void impressMoveSpeed(MoveEvent event, double speed, float stepHeight) {
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        if (!MotionUtil.isMoving()) {
            event.setX(0.0);
            event.setZ(0.0);
        }

        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            }

            else if (strafe <= -1.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }

            if (forward > 0.0f)
                forward = 1.0f;

            else if (forward < 0.0f)
                forward = -1.0f;
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));

        event.setX((double) forward * speed * cos + (double) strafe * speed * sin);
        event.setZ((double) forward * speed * sin - (double) strafe * speed * cos);
        mc.player.stepHeight = stepHeight;

        if (!MotionUtil.isMoving()) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    public static void setMoveSpeed(double speed, float stepHeight) {
        Entity currentMover = mc.player.isRiding() ? mc.player.ridingEntity : mc.player;

        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        if (!MotionUtil.isMoving()) {
            currentMover.motionX = 0;
            currentMover.motionZ = 0;
        }

        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            }

            else if (strafe <= -1.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }

            if (forward > 0.0f)
                forward = 1.0f;

            else if (forward < 0.0f)
                forward = -1.0f;
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));

        currentMover.motionX = (double) forward * speed * cos + (double) strafe * speed * sin;
        currentMover.motionZ = (double) forward * speed * sin - (double) strafe * speed * cos;
        currentMover.stepHeight = stepHeight;

        if (!MotionUtil.isMoving()) {
            currentMover.motionX = 0;
            currentMover.motionZ = 0;
        }
    }

    public static double[] getMoveSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        double motionX;
        double motionZ;

        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            }

            else if (strafe <= -1.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }

            if (forward > 0.0f)
                forward = 1.0f;

            else if (forward < 0.0f)
                forward = -1.0f;
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));

        motionX = (double) forward * speed * cos + (double) strafe * speed * sin;
        motionZ = (double) forward * speed * sin - (double) strafe * speed * cos;

        if (!MotionUtil.isMoving()) {
            motionX = 0;
            motionZ = 0;
        }

        return new double[] {
                motionX, motionZ
        };
    }

    public static boolean isMoving() {
        return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
    }

    public static String getSpeed() {
        DecimalFormat formatter = new DecimalFormat("#.#");
        double deltaX = mc.player.posX - mc.player.prevPosX;
        double deltaZ = mc.player.posZ - mc.player.prevPosZ;

        double KMH = MathUtil.roundAvoid((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / 1000.0f) / (0.05f / 3600.0f), 1);

        String formattedString = formatter.format(KMH);

        if (!formattedString.contains("."))
            formattedString += ".0";

        return formattedString + TextFormatting.WHITE + " km/h";
    }

    public String format(double input) {
        DecimalFormat formatter = new DecimalFormat("#.#");
        String result = formatter.format(input);

        if (!result.contains("."))
            result += ".0";

        return result;
    }
}
