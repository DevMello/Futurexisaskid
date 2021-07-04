package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;

/**
 * @author linustouchtips
 * @since 12/11/2020
 */

// TODO: fix this
public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", Category.MOVEMENT, "Prevents you from walking off edges");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.onGround && !MixinInterface.mc.player.isSneaking() && !MixinInterface.mc.gameSettings.keyBindSneak.isPressed() && !MixinInterface.mc.gameSettings.keyBindJump.isPressed() && MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty()) {
            MixinInterface.mc.player.motionX = 0;
            MixinInterface.mc.player.motionZ = 0;
            MixinInterface.mc.player.setSneaking(true);
        }
    }
}
