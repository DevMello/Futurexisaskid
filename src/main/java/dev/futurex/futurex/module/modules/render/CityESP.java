package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.combat.EnemyUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import dev.futurex.futurex.util.world.BlockUtil;
import dev.futurex.futurex.util.world.WorldUtil;
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

public class CityESP extends Module {
    public CityESP() {
        super("CityESP", Category.RENDER, "Highlights blocks where nearby players can be citied");
    }

    public static Mode mode = new Mode("Mode", "Fill", "Outline", "Both", "Claw");
    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);
    public static dev.futurex.futurex.setting.checkbox.Checkbox burrow = new dev.futurex.futurex.setting.checkbox.Checkbox("Burrow", true);

    public static dev.futurex.futurex.setting.checkbox.Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(burrow);
        addSetting(range);
        addSetting(color);
    }

    List<BlockPos> cityBlocks = new ArrayList<>();
    List<BlockPos> burrowList = new ArrayList<>();

    @Override
    public void onUpdate() {
        cityBlocks.clear();
        burrowList.clear();

        WorldUtil.getNearbyPlayers(20).forEach(entityPlayer -> EnemyUtil.getCityBlocks(entityPlayer, false).stream().filter(blockPos -> MixinInterface.mc.player.getDistanceSq(blockPos) <= range.getValue()).forEach(blockPos -> cityBlocks.add(blockPos)));

        if (burrow.getValue()) {
            WorldUtil.getNearbyPlayers(20).forEach(entityPlayer -> {
                if (BlockUtil.getBlockResistance(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)) == BlockUtil.BlockResistance.Resistant)
                    burrowList.add(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
            });
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        for (BlockPos cityPos : cityBlocks) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Both);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Claw);
                    break;
            }
        }

        for (BlockPos burrowPos : burrowList) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(burrowPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(burrowPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(burrowPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Both);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(burrowPos, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Claw);
                    break;
            }
        }
    }
}