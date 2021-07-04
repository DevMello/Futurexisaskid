package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.player.FlightUtil;

/**
 * @author linustouchtips
 * @since 11/30/2020
 * @author AcaiBerii
 * @since 5/11/2021
 */

public class Flight extends Module {
    public Flight() {
        super("Flight", Category.MOVEMENT, "Allows you to fly");
    }

    public static Mode mode = new Mode("Mode", "Creative", "Vanilla");
    public static Slider hSpeed = new Slider("Horizontal Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(hSpeed);
        addSetting(ySpeed);
    }

    @Override
    public void onDisable() {
        MixinInterface.mc.player.capabilities.isFlying = false;
        MixinInterface.mc.player.capabilities.allowFlying = false;
    }

    @Override
    public void onEnable() {
        if (MixinInterface.mc.player.capabilities.isFlying) {
            MixinInterface.mc.player.capabilities.isFlying = false;
        }
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                if (MixinInterface.mc.gameSettings.keyBindJump.isKeyDown())
                    MixinInterface.mc.player.motionY = ySpeed.getValue();
                else if (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown())
                    MixinInterface.mc.player.motionY = (ySpeed.getValue() * -1);
                else
                    MixinInterface.mc.player.motionY = 0;

                FlightUtil.horizontalFlight(hSpeed.getValue());
                break;
            case 1:
                MixinInterface.mc.player.capabilities.setFlySpeed((float) (hSpeed.getValue() / 23));
                MixinInterface.mc.player.capabilities.isFlying = true;
                MixinInterface.mc.player.capabilities.allowFlying = true;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
