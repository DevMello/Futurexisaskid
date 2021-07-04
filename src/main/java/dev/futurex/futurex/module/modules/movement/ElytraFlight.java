package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.module.modules.movement.elytra.ElytraMode;
import dev.futurex.futurex.module.modules.movement.elytra.modes.*;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.player.MotionUtil;
import dev.futurex.futurex.util.world.Timer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 * @updated 12/29/2020
 */

public class ElytraFlight extends Module {
    public ElytraFlight() {
        super("ElytraFlight", Category.MOVEMENT, "Allows you to fly faster on an elytra");
    }

    public static Mode mode = new Mode("Mode", "Control", "MotionControl", "Pitch", "PitchNCP", "Firework", "Deer", "Dynamic", "DynamicNCP", "Glide", "Vanilla");
    public static SubSlider rotationNCP = new SubSlider(mode, "NCP Rotation", 0.0D, 30.0D, 90.0D, 1);
    public static SubCheckbox rotationLock = new SubCheckbox(mode, "Rotation Lock", false);

    public static Mode boost = new Mode("Boost", "None", "Firework", "Accelerate");

    public static Slider hSpeed = new Slider("Glide Speed", 0.0D, 2.9D, 4.0D, 1);
    public static Slider ySpeed = new Slider("Rise Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider yOffset = new Slider("Y-Offset", 0.0D, 0.009D, 0.1D, 3);
    public static Slider fallSpeed = new Slider("Fall Speed", 0.0D, 0.0D, 0.1D, 3);

    public static Checkbox takeoff = new Checkbox("Auto-Takeoff", true);
    public static SubCheckbox takeoffTimer = new SubCheckbox(takeoff, "Takeoff Timer", true);
    public static SubSlider takeoffTicks = new SubSlider(takeoff, "Takeoff Timer Ticks", 0.1D, 0.2D, 1.0D, 2);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Ticks", 0.1D, 1.1D, 2.0D, 2);

    public static Checkbox pitchSpoof = new Checkbox("Pitch Spoof", false);

    public static Checkbox disable = new Checkbox("Disable", true);
    public static SubCheckbox waterCancel = new SubCheckbox(disable, "In Liquid", true);
    public static SubCheckbox onUpward = new SubCheckbox(disable, "On Upward Motion", false);
    public static SubCheckbox onCollision = new SubCheckbox(disable, "On Collision", false);
    public static SubCheckbox onRubberband = new SubCheckbox(disable, "On Rubberband", false);
    public static SubSlider lowestY = new SubSlider(disable, "Below Y-Level", 0.0D, 8.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(boost);
        addSetting(hSpeed);
        addSetting(ySpeed);
        addSetting(yOffset);
        addSetting(fallSpeed);
        addSetting(takeoff);
        addSetting(useTimer);
        addSetting(pitchSpoof);
        addSetting(disable);
    }

    ElytraMode elytraMode = new Control();
    Timer takeOffTimer = new Timer();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;
        
        if (takeoff.getValue() && !MixinInterface.mc.player.isElytraFlying()) {
            if (MixinInterface.mc.player.onGround)
                MixinInterface.mc.player.motionY = 0.405f;
            else if (takeOffTimer.passed(5, Timer.Format.Ticks))
                MixinInterface.mc.player.connection.sendPacket(new CPacketEntityAction(MixinInterface.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                elytraMode = new Control();
                break;
            case 1:
                elytraMode = new MotionControl();
                break;
            case 2:
                elytraMode = new Pitch();
                break;
            case 3:
                elytraMode = new PitchNCP();
                break;
            case 4:
                elytraMode = new Firework();
                break;
            case 5:
                elytraMode = new Deer();
                break;
            case 6:
                elytraMode = new Dynamic();
                break;
            case 7:
                elytraMode = new DynamicNCP();
                break;
            case 8:
                elytraMode = new Glide();
                break;
            case 9:
                elytraMode = new Vanilla();
                break;
        }

        disableCheck();
        flyTick();

        if (MixinInterface.mc.player.isElytraFlying()) {
            if (!MotionUtil.isMoving())
                elytraMode.noMovement();

            elytraMode.onAscendingMovement();
            elytraMode.onVerticalMovement();
            elytraMode.onHorizontalMovement();
            elytraMode.onRotation();
        }
    }

    public void flyTick() {
        if (!MixinInterface.mc.player.isElytraFlying() && takeoffTimer.getValue())
            MixinInterface.mc.timer.tickLength = (float) (50.0f / takeoffTicks.getValue());

        if (MixinInterface.mc.player.isElytraFlying() && !useTimer.getValue())
            MixinInterface.mc.timer.tickLength = 50;

        if (MixinInterface.mc.player.isElytraFlying() && useTimer.getValue())
            MixinInterface.mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

    }

    public void disableCheck() {
        if (!disable.getValue())
            return;

        if (MixinInterface.mc.player.posY <= lowestY.getValue())
            return;

        if ((MixinInterface.mc.player.isInWater() || MixinInterface.mc.player.isInLava()) && waterCancel.getValue())
            return;

        if (MixinInterface.mc.player.rotationPitch >= rotationNCP.getValue() && onUpward.getValue())
            return;

        if (MixinInterface.mc.player.isElytraFlying() && MixinInterface.mc.gameSettings.keyBindJump.isKeyDown() && onUpward.getValue())
            return;

        if (MixinInterface.mc.player.collidedHorizontally && onCollision.getValue())
            return;
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && MixinInterface.mc.player.isElytraFlying()) {
            if (disable.getValue() && onRubberband.getValue())
                return;

            elytraMode.onRubberband();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && pitchSpoof.getValue() && MixinInterface.mc.player.isElytraFlying()) {
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                MixinInterface.mc.getConnection().sendPacket(new CPacketPlayer.Position(((CPacketPlayer.PositionRotation) event.getPacket()).x, ((CPacketPlayer.PositionRotation) event.getPacket()).y, ((CPacketPlayer.PositionRotation) event.getPacket()).z, ((CPacketPlayer.PositionRotation) event.getPacket()).onGround));
                event.setCanceled(true);
            }

            else if (event.getPacket() instanceof CPacketPlayer.Rotation)
                event.setCanceled(true);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}