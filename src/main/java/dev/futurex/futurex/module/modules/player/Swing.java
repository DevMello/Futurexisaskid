package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Swing extends Module {
    public Swing() {
        super("Swing", Category.PLAYER, "Swings with your offhand");
    }

    public static Mode mode = new Mode("Mode", "Offhand", "Mainhand", "Switch");
    public static Checkbox noAnimation = new Checkbox("Cancel Animation", true);
    public static Checkbox noReset = new Checkbox("No Reset", false);
    public static Checkbox dropSwing = new Checkbox("Drop Swing", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(noAnimation);
        addSetting(noReset);
        addSetting(dropSwing);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                mc.player.swingingHand = EnumHand.OFF_HAND;
                break;
            case 1:
                mc.player.swingingHand = EnumHand.MAIN_HAND;
                break;
        }

        if (mc.gameSettings.keyBindDrop.isPressed() && dropSwing.getValue())
            mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketAnimation && noAnimation.getValue())
            event.setCanceled(true);
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
