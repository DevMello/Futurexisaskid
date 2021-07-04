package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.module.Module;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class ExtraSlots extends Module {
    public ExtraSlots() {
        super("ExtraSlots", Category.PLAYER, "Closes your inventory without the server knowing");
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketCloseWindow)
            event.setCanceled(true);
    }
}
