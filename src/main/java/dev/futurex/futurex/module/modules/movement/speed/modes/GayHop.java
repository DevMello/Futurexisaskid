package dev.futurex.futurex.module.modules.movement.speed.modes;

import dev.futurex.futurex.event.events.player.MoveEvent;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.modules.movement.speed.SpeedMode;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.util.player.MotionUtil;
import dev.futurex.futurex.module.modules.movement.Speed;

/**
 * @author linustouchtips
 * @since 02/03/2021
 */

public class GayHop extends SpeedMode {

    @Override
    public void onMotionUpdate() {
        lastDist = Math.sqrt(Math.pow(MixinInterface.mc.player.posX - MixinInterface.mc.player.prevPosX, 2) + Math.pow(MixinInterface.mc.player.posZ - MixinInterface.mc.player.prevPosZ, 2));
    }

    @Override
    public void handleSpeed(MoveEvent event) {
        MessageUtil.sendClientMessage("" + moveSpeed);

        if (MotionUtil.isMoving()) {
            if (MixinInterface.mc.player.onGround)
                setStage(Stage.Jump);

            if (Speed.useTimer.getValue())
                MixinInterface.mc.timer.tickLength = (float) (50f / Speed.timerTicks.getValue());
        }

        if (stage == Stage.Pre && MotionUtil.isMoving()) {
            setStage(Stage.Jump);
            moveSpeed = 1.38 * Speed.speed.getValue();
        }

        else if (stage == Stage.Jump) {
            setStage(Stage.Post);

            if (Speed.jump.getValue()) {
                MixinInterface.mc.player.motionY = 0.3994f;
                event.setY(0.3994f);
            }

            moveSpeed *= 2.149;
        }

        else if (stage == Stage.Post) {
            setStage(Stage.Cycle);
            moveSpeed = lastDist - (0.66 * (lastDist - Speed.speed.getValue()));
        }

        else {
            if (MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(0.0, MixinInterface.mc.player.motionY, 0.0)).size() > 0 || MixinInterface.mc.player.collidedVertically)
                setStage(Stage.Pre);

            moveSpeed = lastDist - (lastDist / 159.0);
        }

        moveSpeed = Math.max(moveSpeed, Speed.speed.getValue());

        MotionUtil.impressMoveSpeed(event, moveSpeed, (float) Speed.stepHeight.getValue());
    }
}
