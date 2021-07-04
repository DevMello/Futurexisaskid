package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.event.events.player.MoveEvent;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.player.PlayerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Category.MOVEMENT, "Allows you to walk on water");
    }

    public static Mode mode = new Mode("Mode", "Normal", "Packet", "Push", "Freeze");
    public static Slider offset = new Slider("Offset", 0.0D, 0.2D, 1.0D, 2);
    public static Slider delay = new Slider("Delay", 0.0D, 2.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(offset);
        addSetting(delay);
    }

    @SubscribeEvent
    public void onMoveEvent(MoveEvent event) {
        if (nullCheck())
            return;

        if (PlayerUtil.isInLiquid() && !MixinInterface.mc.gameSettings.keyBindJump.isPressed())
            event.setY(0);
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
