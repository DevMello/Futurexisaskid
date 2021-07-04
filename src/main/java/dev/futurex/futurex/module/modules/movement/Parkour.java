package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Parkour extends Module {
    public Parkour() {
        super("Parkour", Category.MOVEMENT, "Automatically jumps at the edge of a block");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.onGround && !MixinInterface.mc.player.isSneaking() && !MixinInterface.mc.gameSettings.keyBindSneak.isPressed() && !MixinInterface.mc.gameSettings.keyBindJump.isPressed() && MixinInterface.mc.world.getCollisionBoxes(MixinInterface.mc.player, MixinInterface.mc.player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty())
            MixinInterface.mc.player.jump();
    }
}
