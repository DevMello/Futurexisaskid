package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.slider.Slider;
import net.minecraft.init.Blocks;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class IceSpeed extends Module {
    public IceSpeed() {
        super("IceSpeed", Category.MOVEMENT, "Reduces the slipperiness of ice");
    }

    public static Slider slipperiness = new Slider("Slipperiness", 0.0D, 0.0D, 1.0D, 2);

    @Override
    public void setup() {
        addSetting(slipperiness);
    }

    @SuppressWarnings("deprecation")
    public void onUpdate() {
        if (nullCheck())
            return;

        Blocks.ICE.slipperiness = (float) slipperiness.getValue();
        Blocks.PACKED_ICE.slipperiness = (float) slipperiness.getValue();
        Blocks.FROSTED_ICE.slipperiness = (float) slipperiness.getValue();
    }

    @SuppressWarnings("deprecation")
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98F;
        Blocks.PACKED_ICE.slipperiness = 0.98F;
        Blocks.FROSTED_ICE.slipperiness = 0.98F;
    }
}
