package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder.RenderMode;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class BreakESP extends Module {
    public BreakESP() {
        super("BreakESP", Category.RENDER, "Highlights blocks being broken");
    }

    public static Mode mode = new Mode("Mode", "Fill", "Outline", "Both", "Claw");
    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);
    public static Checkbox showInfo = new Checkbox("Render Info", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",  new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(showInfo);
        addSetting(color);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            destroyBlockProgress.getPosition();
            if (destroyBlockProgress.getPosition().getDistance((int) mc.player.posX,(int)  mc.player.posY,(int)  mc.player.posZ) <= range.getValue()) {
                switch (mode.getValue()) {
                    case 0:
                        RenderUtil.drawBoxBlockPos(destroyBlockProgress.getPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Fill);
                        break;
                    case 1:
                        RenderUtil.drawBoxBlockPos(destroyBlockProgress.getPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Outline);
                        break;
                    case 2:
                        RenderUtil.drawBoxBlockPos(destroyBlockProgress.getPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Both);
                        break;
                    case 3:
                        RenderUtil.drawBoxBlockPos(destroyBlockProgress.getPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Claw);
                        break;
                }

                if (showInfo.getValue()) {
                    try {
                        RenderUtil.drawNametagFromBlockPos(destroyBlockProgress.getPosition(), 0.6f, mc.world.getEntityByID(integer).getName());
                        RenderUtil.drawNametagFromBlockPos(destroyBlockProgress.getPosition(), 0.2f, (destroyBlockProgress.getPartialBlockDamage() * 10) + "%");
                    } catch (Exception ignored) {

                    }
                }
            }
        });
    }
}