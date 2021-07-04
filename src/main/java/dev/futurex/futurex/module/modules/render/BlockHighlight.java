package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BlockHighlight extends Module {
    public BlockHighlight() {
        super("BlockHighlight", Category.RENDER, "Highlights the block you're facing");
    }

    public static Mode mode = new Mode("Mode", "Claw", "Outline", "Fill", "Both");

    public static dev.futurex.futurex.setting.checkbox.Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",   new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(color);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (MixinInterface.mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(MixinInterface.mc.objectMouseOver.getBlockPos(), 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Claw);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(MixinInterface.mc.objectMouseOver.getBlockPos(), 0, 0, 0, new Color(colorPicker.getRed(), colorPicker.getGreen(), colorPicker.getBlue(), 144), RenderBuilder.RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(MixinInterface.mc.objectMouseOver.getBlockPos(), 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(MixinInterface.mc.objectMouseOver.getBlockPos(), 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Both);
                    break;
            }
        }
    }
}