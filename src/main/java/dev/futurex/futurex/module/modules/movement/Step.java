package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.player.MotionUtil;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips & seasnail8169
 * @since 12/03/2020
 */

public class Step extends Module {
    public Step() {
        super("Step", Category.MOVEMENT, "Increases player step height");
    }

    public static Mode mode = new Mode("Mode", "Teleport", "Spider", "Vanilla");
    public static Slider height = new Slider("Height", 0.0D, 2.0D, 2.5D, 1);

    public static Mode disable = new Mode("Disable", "Never", "Completion", "Unsafe");

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 0.5D, 2.0D, 2);

    public static Checkbox entityStep = new Checkbox("Entity Step", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox sneakPause = new SubCheckbox(pause, "When Sneaking", false);
    public static SubCheckbox waterPause = new SubCheckbox(pause, "When in Liquid", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(height);
        addSetting(disable);
        addSetting(useTimer);
        addSetting(entityStep);
        addSetting(pause);
    }

    double[] forwardStep;
    double originalHeight;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalHeight = MixinInterface.mc.player.posY;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!MixinInterface.mc.player.collidedHorizontally)
            return;

        if (MixinInterface.mc.player.isOnLadder() || MixinInterface.mc.player.movementInput.jump)
            return;

        if ((MixinInterface.mc.player.isInWater() || MixinInterface.mc.player.isInLava()) && waterPause.getValue())
            return;

        if (!MixinInterface.mc.player.onGround)
            return;

        if (useTimer.getValue())
            MixinInterface.mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

        if (entityStep.getValue() && MixinInterface.mc.player.isRiding())
            MixinInterface.mc.player.ridingEntity.stepHeight = (float) height.getValue();

        if (MixinInterface.mc.player.isSneaking() && sneakPause.getValue())
            return;

        forwardStep = MotionUtil.getMoveSpeed(0.1);

        if (getStepHeight().equals(StepHeight.Unsafe)) {
            if (disable.getValue() == 2)
                this.disable();

            return;
        }

        switch (mode.getValue()) {
            case 0:
                stepTeleport();
                break;
            case 1:
                stepSpider();
                break;
            case 2:
                stepVanilla();
                break;
        }

        if (MixinInterface.mc.player.posY > originalHeight + getStepHeight().height && disable.getValue() == 1)
            this.disable();
    }

    public void stepTeleport() {
        updateStepPackets(getStepHeight().stepArray);
        MixinInterface.mc.player.setPosition(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + getStepHeight().height, MixinInterface.mc.player.posZ);
    }

    public void stepSpider() {
       updateStepPackets(getStepHeight().stepArray);

        MixinInterface.mc.player.motionY = 0.2;
        MixinInterface.mc.player.fallDistance = 0;
    }

    public void stepVanilla() {
        MixinInterface.mc.player.setPosition(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + getStepHeight().height, MixinInterface.mc.player.posZ);
    }

    public void updateStepPackets(double[] stepArray) {
        for (double v : stepArray) {
            MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + v, MixinInterface.mc.player.posZ, MixinInterface.mc.player.onGround));
        }
    }

    public StepHeight getStepHeight() {
        if (MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.0, forwardStep[1])).isEmpty() && !MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 0.6, forwardStep[1])).isEmpty())
            return StepHeight.One;
        else if (MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.6, forwardStep[1])).isEmpty() && !MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.4, forwardStep[1])).isEmpty())
            return StepHeight.OneHalf;
        else if (MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.1, forwardStep[1])).isEmpty() && !MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.9, forwardStep[1])).isEmpty())
            return StepHeight.Two;
        else if (MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.6, forwardStep[1])).isEmpty() && !MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.4, forwardStep[1])).isEmpty())
            return StepHeight.TwoHalf;
        else
            return StepHeight.Unsafe;
    }

    public enum StepHeight {
        One(1, new double[] { 0.42, 0.753 }),
        OneHalf(1.5, new double[] { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 }),
        Two(2, new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 }),
        TwoHalf(2.5, new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 }),
        Unsafe(3, new double[] { 0 });

        double[] stepArray;
        double height;

        StepHeight(double height, double[] stepArray) {
            this.height = height;
            this.stepArray = stepArray;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
