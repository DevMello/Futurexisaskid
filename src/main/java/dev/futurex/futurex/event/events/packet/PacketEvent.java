package dev.futurex.futurex.event.events.packet;

import dev.futurex.futurex.event.FutureXEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author bon
 * @since 11/21/20
 */

@Cancelable
public class PacketEvent extends FutureXEvent {

	Packet<?> packet;

	public PacketEvent(Packet<?> packet, Stage stage) {
		super(stage);
		this.packet = packet;
	}
	
	public Packet<?> getPacket(){
		return this.packet;
	}

}
