package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class AutoJump extends Module {
    public AutoJump() {
        super("AutoJump", Category.MOVEMENT, "Automatically jumps");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        KeyBinding.setKeyBindState(MixinInterface.mc.gameSettings.keyBindJump.getKeyCode(), true);
    }
}
