package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.managers.notification.Notification.Type;
import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.util.world.WorldUtil;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author linustouchtips & Hoosiers
 * @since 11/30/2020
 */

public class Blink extends Module {
    public Blink() {
        super("Blink", Category.PLAYER, "Cancels all player packets");
    }

    public static Checkbox playerModel = new Checkbox("Player Model", true);

    @Override
    public void setup() {
        addSetting(playerModel);
    }

    Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (playerModel.getValue())
            WorldUtil.createFakePlayer(null, true, true, true, true, 6640);

        NotificationManager.addNotification(new Notification("Cancelling all player packets!", Type.Info));
    }

    @Override
    public void onDisable() {
        if (nullCheck())
            return;

        mc.world.removeEntityFromWorld(69420);

        for (Packet<? extends INetHandler> packet : packets) {
            mc.player.connection.sendPacket(packet);
        }

        packets.clear();
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketConfirmTeleport || event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketTabComplete || event.getPacket() instanceof CPacketClientStatus)
            return;

        packets.add(event.getPacket());
        event.setCanceled(true);
    }
}
