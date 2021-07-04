package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.player.FlightUtil;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class BoatFlight extends Module {
    public BoatFlight() {
        super("BoatFlight", Category.MOVEMENT, "Allows you to fly on rideable entities");
    }

    public static Mode mode = new Mode("Mode", "Control", "Row");
    public static Slider hSpeed = new Slider("Horizontal Speed", 0.0D, 2.9D, 4.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider yOffset = new Slider("Y-Offset", 0.0D, 0.009D, 0.1D, 3);
    public static Slider fallSpeed = new Slider("Fall Speed", 0.0D, 0.0D, 0.1D, 3);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 1.1D, 2.0D, 2);

    public static Checkbox disable = new Checkbox("Disable", true);
    public static SubCheckbox waterCancel = new SubCheckbox(disable, "In Liquid", true);
    public static SubCheckbox onUpward = new SubCheckbox(disable, "On Upward Motion", false);
    public static SubCheckbox onCollision = new SubCheckbox(disable, "On Collision", false);
    public static SubSlider lowestY = new SubSlider(disable, "Below Y-Level", 0.0D, 8.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(hSpeed);
        addSetting(ySpeed);
        addSetting(yOffset);
        addSetting(fallSpeed);
        addSetting(useTimer);
        addSetting(disable);
    }

    @Override
    public void onDisable() {
        MixinInterface.mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        disableCheck();
        flyTick();

        switch (mode.getValue()) {
            case 0:
                flyControl();
                break;
            case 1:
                flySwim();
                break;
        }
    }

    public void flyControl() {
        if (MixinInterface.mc.player.isRiding()) {
            MixinInterface.mc.player.mc.player.ridingEntity.rotationYaw = MixinInterface.mc.player.rotationYaw;

            MixinInterface.mc.player.ridingEntity.motionY += yOffset.getValue();
            MixinInterface.mc.player.ridingEntity.setVelocity(0, -fallSpeed.getValue(), 0);

            if (MixinInterface.mc.gameSettings.keyBindJump.isKeyDown())
                MixinInterface.mc.player.ridingEntity.motionY = ySpeed.getValue();

            else if (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown())
                MixinInterface.mc.player.ridingEntity.motionY = (ySpeed.getValue() * -1);

            FlightUtil.horizontalFlight(hSpeed.getValue());
        }
    }

    public void flySwim() {
        if (MixinInterface.mc.player.isRiding()) {
            MixinInterface.mc.player.mc.player.ridingEntity.rotationYaw = MixinInterface.mc.player.rotationYaw;

            MixinInterface.mc.player.ridingEntity.motionY += yOffset.getValue();
            MixinInterface.mc.player.ridingEntity.setVelocity(0, -fallSpeed.getValue(), 0);

            if (MixinInterface.mc.gameSettings.keyBindJump.isKeyDown())
                MixinInterface.mc.player.ridingEntity.motionY = ySpeed.getValue();

            else if (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown())
                MixinInterface.mc.player.ridingEntity.motionY = (ySpeed.getValue() * -1);
        }
    }

    public void flyTick() {
        if (MixinInterface.mc.player.isRiding() && !useTimer.getValue())
            MixinInterface.mc.timer.tickLength = 50;

        if (MixinInterface.mc.player.isRiding() && useTimer.getValue())
            MixinInterface.mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());
    }

    public void disableCheck() {
        if (!disable.getValue())
            return;

        if (MixinInterface.mc.player.posY <= lowestY.getValue())
            return;

        if ((MixinInterface.mc.player.isInWater() || MixinInterface.mc.player.isInLava()) && waterCancel.getValue())
            return;

        if (MixinInterface.mc.player.rotationPitch >= 40 && onUpward.getValue())
            return;

        if (MixinInterface.mc.player.isRowingBoat() && MixinInterface.mc.gameSettings.keyBindJump.isKeyDown() && onUpward.getValue())
            return;

        if (MixinInterface.mc.player.collidedHorizontally && onCollision.getValue())
            return;
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketMoveVehicle)
            event.setCanceled(true);
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
