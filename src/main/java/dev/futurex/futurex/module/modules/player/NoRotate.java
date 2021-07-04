package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ZimTheDestroyer & Mallyx
 * @since 12/8/20 @ 2:20 PM CST
 */

public class NoRotate  extends Module {
    public NoRotate() {
        super("NoRotate", Category.PLAYER, "Prevents the server from rotating you");
    }

    public static Checkbox strict = new Checkbox("Strict", false);

    @Override
    public void setup() {
        addSetting(strict);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            ((SPacketPlayerPosLook) event.getPacket()).yaw = mc.player.rotationYaw;
            ((SPacketPlayerPosLook) event.getPacket()).pitch = mc.player.rotationPitch;
            float lastYaw = ((SPacketPlayerPosLook) event.getPacket()).yaw;
            float lastPitch = ((SPacketPlayerPosLook) event.getPacket()).pitch;

            if (strict.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(((SPacketPlayerPosLook) event.getPacket()).yaw, ((SPacketPlayerPosLook) event.getPacket()).pitch, mc.player.onGround));

                for (int i = 0; i <= 3; i++) {
                    lastYaw = RotationUtil.calculateAngle(lastYaw, mc.player.rotationYaw);
                    lastPitch = RotationUtil.calculateAngle(lastPitch, mc.player.rotationPitch);
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(RotationUtil.calculateAngle(lastYaw, mc.player.rotationYaw), RotationUtil.calculateAngle(lastPitch, mc.player.rotationPitch), mc.player.onGround));
                }

                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
            }
        }
    }
}
