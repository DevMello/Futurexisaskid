package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class AntiHunger extends Module {
    public AntiHunger() {
        super("AntiHunger", Category.PLAYER, "Allows you to lose no hunger");
    }

    public static Mode mode = new Mode("Mode", "Packet", "Vanilla");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public void onUpdate() {
        if (mode.getValue() == 1)
            mc.player.getFoodStats().setFoodLevel(20);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (mode.getValue() == 0) {
            if (event.getPacket() instanceof CPacketPlayer)
                ((CPacketPlayer) event.getPacket()).onGround = (mc.player.fallDistance > 0 || mc.playerController.isHittingBlock);

            if (event.getPacket() instanceof CPacketEntityAction && ((CPacketEntityAction) event.getPacket()).getAction() == START_SPRINTING || ((CPacketEntityAction) event.getPacket()).getAction() == STOP_SPRINTING)
                event.setCanceled(true);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
