package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import dev.futurex.futurex.util.world.BlockUtil;
import dev.futurex.futurex.util.world.hole.HoleUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class VoidESP extends Module {
    public VoidESP() {
        super("VoidESP", Category.RENDER, "Highlights void holes");
    }

    public static Mode mode = new Mode("Mode", "Fill", "Outline", "Both", "Claw");
    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);
    public static dev.futurex.futurex.setting.checkbox.Checkbox portals = new dev.futurex.futurex.setting.checkbox.Checkbox("Portals", false);

    public static dev.futurex.futurex.setting.checkbox.Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(177, 50, 236, 121));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(portals);
        addSetting(color);
    }

    List<BlockPos> voidBlocks = new ArrayList<>();
    List<BlockPos> portalBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        voidBlocks.clear();

        BlockUtil.getNearbyBlocks(MixinInterface.mc.player, range.getValue(), false).stream().filter(HoleUtil::isVoidHole).forEach(blockPos ->
                voidBlocks.add(blockPos)
        );

        BlockUtil.getNearbyBlocks(MixinInterface.mc.player, range.getValue(), false).stream().filter(blockPos -> MixinInterface.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.PORTAL)).forEach(blockPos ->
                portalBlocks.add(blockPos)
        );
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        for (BlockPos voidPos : voidBlocks) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(voidPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(voidPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(voidPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Both);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(voidPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Claw);
                    break;
            }
        }

        for (BlockPos portalPos : portalBlocks) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(portalPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(portalPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(portalPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Both);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(portalPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Claw);
                    break;
            }
        }
    }
}