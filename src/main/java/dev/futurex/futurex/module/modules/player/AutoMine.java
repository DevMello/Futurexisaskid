package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class AutoMine extends Module {
    public AutoMine() {
        super("AutoMine", Category.PLAYER, "Automatically mines blocks in your crosshairs");
    }

    public static Mode mode = new Mode("Mode", "Auto-Tunnel", "Frenzy");
    public static SubSlider delay = new SubSlider(mode, "Rotate Delay", 0.0D, 5.0D, 50.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        mc.sendClickBlockToController(true);

        if (mode.getValue() == 0)
            autoTunnel();
    }

    public void autoTunnel() {
        RotationUtil.resetYaw(30);
        RotationUtil.resetPitch(30);

        int angle = 360 / 4;
        float yaw = mc.player.rotationYaw;
        yaw = Math.round(yaw / angle) * angle;

        mc.player.rotationYaw = yaw;

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);

        if (mc.player.ticksExisted % delay.getValue() == 0)
            mc.player.rotationPitch += 75;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
