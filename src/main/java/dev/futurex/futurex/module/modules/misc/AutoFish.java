package dev.futurex.futurex.module.modules.misc;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.player.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/28/2020
 */

public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish", Category.MISC, "Automatically fishes for you");
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (InventoryUtil.getHeldItem(Items.FISHING_ROD) && event.getPacket() instanceof SPacketSoundEffect && SoundEvents.ENTITY_BOBBER_SPLASH.equals(((SPacketSoundEffect) event.getPacket()).getSound()))
            mc.rightClickMouse();
    }
}
