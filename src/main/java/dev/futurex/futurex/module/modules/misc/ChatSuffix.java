package dev.futurex.futurex.module.modules.misc;

import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.client.MessageUtil;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author bon & linustouchtips
 * @since 11/21/20
 */

public class ChatSuffix extends Module {
	public ChatSuffix() {
		super("ChatSuffix", Category.MISC, "Appends a chat suffix to messages");
	}

	public String suffix = "FutureX";

	@SubscribeEvent
	public void onPacketSend(PacketSendEvent event) {
		if (event.getPacket() instanceof CPacketChatMessage) {
			if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("!") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("$") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("?") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(".") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(","))
				return;

			((CPacketChatMessage) event.getPacket()).message = ((CPacketChatMessage) event.getPacket()).getMessage() + " \u23d0 " + MessageUtil.toUnicode(suffix);
		}
	}
}
