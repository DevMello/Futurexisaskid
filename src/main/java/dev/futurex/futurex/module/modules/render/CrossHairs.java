package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.SubMode;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.render.Render2DUtil;
import dev.futurex.futurex.util.world.Timer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class CrossHairs extends Module {
    public CrossHairs() {
        super("CrossHairs", Category.RENDER, "Allows you to change the default crosshair");
    }

    public static dev.futurex.futurex.setting.checkbox.Checkbox custom = new dev.futurex.futurex.setting.checkbox.Checkbox("Custom", true);
    public static SubMode mode = new SubMode(custom, "Mode", "Dynamic", "Static");
    public static SubSlider thickness = new SubSlider(custom, "Thickness", 0.0D, 2.0D, 5.0D, 1);
    public static SubSlider separation = new SubSlider(custom, "Separation", 0.0D, 2.0D, 10.0D, 1);
    public static SubSlider width = new SubSlider(custom, "Width", 0.0D, 4.0D, 10.0D, 1);
    public static SubSlider bend = new SubSlider(custom, "Bend", 0.0D, 0.0D, 5.0D, 0);

    public static dev.futurex.futurex.setting.checkbox.Checkbox hitMarkers = new dev.futurex.futurex.setting.checkbox.Checkbox("HitMarkers", true);
    public static ColorPicker hitMarkerPicker = new ColorPicker(hitMarkers, "HitMarkers Picker", new Color(250, 0, 250, 50));

    public static dev.futurex.futurex.setting.checkbox.Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(custom);
        addSetting(hitMarkers);
        addSetting(color);
    }

    Timer hitTimer = new Timer();

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK)
            hitTimer.reset();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (hitTimer.reach(300, Timer.Format.System) && hitMarkers.getValue())
            Render2DUtil.drawHitMarkers(hitMarkerPicker.getColor());

        if (custom.getValue() && event.getType() == ElementType.CROSSHAIRS)
            event.setCanceled(true);

        Render2DUtil.drawCrosshairs(separation.getValue(), bend.getValue(), width.getValue(), thickness.getValue(), mode.getValue() == 0,  colorPicker.getColor().getRGB());
    }
}