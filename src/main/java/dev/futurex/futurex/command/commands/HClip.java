package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.command.Command;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class HClip extends Command implements MixinInterface {
    public HClip() {
        super("hclip", "[distance]", "Teleports you in the horizontal direction");
    }

    @Override
    public void onCommand(String[] args) {
        Vec3d direction = new Vec3d(Math.cos(RotationUtil.degToRad(mc.player.rotationYaw + 90f)), 0, Math.sin(RotationUtil.degToRad(mc.player.rotationYaw + 90f)));

        if (args.length > 0) {
            (mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player).setPosition(mc.player.posX + direction.x * Double.parseDouble(args[1]), mc.player.posY, mc.player.posZ + direction.z * Double.valueOf(args[1]));

            MessageUtil.addOutput("Teleported you " + args[1] + " blocks in the horizontal direction!");
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
