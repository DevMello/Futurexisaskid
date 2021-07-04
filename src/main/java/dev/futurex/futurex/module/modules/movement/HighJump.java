package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.slider.Slider;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class HighJump extends Module {
    public HighJump() {
        super("HighJump", Category.MOVEMENT, "Allows you to jump higher");
    }

    public static Checkbox packet = new Checkbox("Packet", true);
    public static Slider height = new Slider("Height", 0.0D, 1.5D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(packet);
        addSetting(height);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.gameSettings.keyBindJump.isPressed()) {
            MixinInterface.mc.player.motionY = height.getValue();

            if (packet.getValue())
                MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY, MixinInterface.mc.player.posZ, MixinInterface.mc.player.onGround));
        }
    }
}
