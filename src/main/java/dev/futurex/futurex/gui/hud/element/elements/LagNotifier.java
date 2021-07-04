package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.gui.main.HUDScreen;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.util.world.Timer;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.setting.slider.Slider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/24/2020
 */

public class LagNotifier extends HUDElement {
    public LagNotifier() {
        super("LagNotifier", 300, 2, Category.MISC, AnchorPoint.None);
    }

    public static Slider threshHold = new Slider("Threshold", 0.0D, 3.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(threshHold);
    }

    Timer lagTimer = new Timer();

    @Override
    public void renderElement() {
        float seconds = ((System.currentTimeMillis() - lagTimer.time) / 1000.0f) % 60.0f;
        width = (int) FontUtil.getStringWidth("Server has stopped responding for X.X seconds!");

        if (mc.currentScreen instanceof HUDScreen)
            FontUtil.drawString("Server has stopped responding for X seconds!", this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);

        if (seconds < threshHold.getValue())
            return;

        if (!(mc.currentScreen instanceof HUDScreen))
            FontUtil.drawString("Server has stopped responding for " + new DecimalFormat("#.#").format(seconds) + " seconds!", this.x, this.y, -1);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        lagTimer.reset();
    }
}