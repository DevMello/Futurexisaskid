package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT, "Allows you to fall faster");
    }

    public static Slider height = new Slider("Height", 0.0D, 2.0D, 5.0D, 0);
    public static Slider speed = new Slider("Speed", 0.0D, 1.0D, 20.0D, 0);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox sneakPause = new SubCheckbox(pause, "When Sneaking", false);
    public static SubCheckbox waterPause = new SubCheckbox(pause, "When in Liquid", true);

    @Override
    public void setup() {
        addSetting(height);
        addSetting(speed);
        addSetting(pause);
    }

    @Override
    public void onUpdate() {
    	if (nullCheck())
    	    return;

        if (MixinInterface.mc.player.isInWater() && waterPause.getValue() || MixinInterface.mc.player.isInLava() && waterPause.getValue() || MixinInterface.mc.player.isOnLadder() || MixinInterface.mc.gameSettings.keyBindJump.isKeyDown() || !MixinInterface.mc.player.onGround)
            return;

        if (MixinInterface.mc.player.isSneaking() && sneakPause.getValue())
            return;

        for (double y = 0.0; y < height.getValue() + 0.5; y += 0.01) {
            if (!MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                MixinInterface.mc.player.motionY = -speed.getValue();
                break;
            }
        }
    }
}
