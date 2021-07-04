package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class AirJump extends Module {
    public AirJump() {
        super("AirJump", Category.MOVEMENT, "Allows you to jump in the air");
    }

    public static Checkbox packet = new Checkbox("Packet", true);

    @Override
    public void setup() {
        addSetting(packet);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        MixinInterface.mc.player.onGround = true;

        if (packet.getValue()) {
            MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 0.41999998688698D, MixinInterface.mc.player.posZ, true));
            MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 0.7531999805211997D, MixinInterface.mc.player.posZ, true));
            MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 1.00133597911214D, MixinInterface.mc.player.posZ, true));
            MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 1.16610926093821D, MixinInterface.mc.player.posZ, true));
        }
    }
}
