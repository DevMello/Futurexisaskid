package dev.futurex.futurex.event.events.packet;

import dev.futurex.futurex.event.FutureXEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author bon
 * @since 11/21/20
 */

@Cancelable
public class PacketReceiveEvent extends PacketEvent {
	public PacketReceiveEvent(Packet<?> packet, FutureXEvent.Stage stage) {
		super(packet, stage);
	}
}
