package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.world.hole.HoleUtil;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class Anchor extends Module {
    public Anchor() {
        super("Anchor", Category.MOVEMENT, "Stops all movement above a hole");
    }

    public static Slider height = new Slider("Height", 1.0D, 3.0D, 5.0D, 0);
    public static Slider pitch = new Slider("Pitch", 0.0D, 75.0D, 90.0D, 0);

    public static Checkbox pull = new Checkbox("Pull", false);
    public static SubSlider speed = new SubSlider(pull, "Pull Speed", 0.0D, 3.0D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(height);
        addSetting(pitch);
        addSetting(pull);
    }

    boolean anchored;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.rotationPitch <= 90 - pitch.getValue())
            return;

        for (int i = 0; i < MixinInterface.mc.player.posY - height.getValue(); i++) {
            BlockPos belowPos = new BlockPos(MixinInterface.mc.player.posX, i, MixinInterface.mc.player.posZ);

            if (HoleUtil.isHole(belowPos))
                anchored = true;
        }

        if (anchored) {
            MixinInterface.mc.player.motionX = 0;
            MixinInterface.mc.player.motionZ = 0;

            if (pull.getValue())
                MixinInterface.mc.player.motionY -= speed.getValue();
        }
    }
}
