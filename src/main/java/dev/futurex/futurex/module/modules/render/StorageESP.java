package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.managers.ColorManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", Category.RENDER, "Highlights containers");
    }

    public static Mode mode = new Mode("Mode", "Claw", "Outline", "Fill", "Both");

    public static dev.futurex.futurex.setting.checkbox.Checkbox chests = new dev.futurex.futurex.setting.checkbox.Checkbox("Chests", true);
    public static ColorPicker chestPicker = new ColorPicker(chests, "Chest Picker", new Color(46, 83, 215));

    public static dev.futurex.futurex.setting.checkbox.Checkbox enderChests = new dev.futurex.futurex.setting.checkbox.Checkbox("Ender Chests", true);
    public static ColorPicker enderPicker = new ColorPicker(enderChests, "Ender Chest Picker", new Color(156, 46, 215));

    public static dev.futurex.futurex.setting.checkbox.Checkbox shulkers = new dev.futurex.futurex.setting.checkbox.Checkbox("Shulkers", true);
    public static ColorPicker shulkerPicker = new ColorPicker(shulkers, "Shulker Picker", new Color(215, 46, 198));

    public static dev.futurex.futurex.setting.checkbox.Checkbox hoppers = new dev.futurex.futurex.setting.checkbox.Checkbox("Hoppers", true);
    public static ColorPicker hopperPicker = new ColorPicker(hoppers, "Hopper Picker", new Color(106, 106, 114));

    public static dev.futurex.futurex.setting.checkbox.Checkbox droppers = new dev.futurex.futurex.setting.checkbox.Checkbox("Droppers", true);
    public static ColorPicker dropperPicker = new ColorPicker(droppers, "Dropper Picker", new Color(106, 106, 114));

    public static dev.futurex.futurex.setting.checkbox.Checkbox furnaces = new dev.futurex.futurex.setting.checkbox.Checkbox("Furnaces", true);
    public static ColorPicker furnacePicker = new ColorPicker(furnaces, "Furnace Picker", new Color(106, 106, 114));

    public static dev.futurex.futurex.setting.checkbox.Checkbox beds = new Checkbox("Beds", true);
    public static ColorPicker bedPicker = new ColorPicker(beds, "Bed Picker", new Color(208, 40, 60));

    public static Slider range = new Slider("Range", 0.0D, 30.0D, 100.0D, 0);
    public static Slider lineWidth = new Slider("Line Width", 0.0D, 2.5D, 4.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(chests);
        addSetting(enderChests);
        addSetting(shulkers);
        addSetting(hoppers);
        addSetting(droppers);
        addSetting(furnaces);
        addSetting(beds);
        addSetting(range);
        addSetting(lineWidth);
    }

    ColorManager colorManager = new ColorManager();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        colorManager.registerAbstractColor(TileEntityChest.class, chestPicker.getColor());
        colorManager.registerAbstractColor(TileEntityEnderChest.class, enderPicker.getColor());
        colorManager.registerAbstractColor(TileEntityShulkerBox.class, shulkerPicker.getColor());
        colorManager.registerAbstractColor(TileEntityHopper.class, hopperPicker.getColor());
        colorManager.registerAbstractColor(TileEntityDropper.class, dropperPicker.getColor());
        colorManager.registerAbstractColor(TileEntityFurnace.class, furnacePicker.getColor());
        colorManager.registerAbstractColor(TileEntityBed.class, bedPicker.getColor());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        renderTileEntities();
    }

    public void renderTileEntities() {
        MixinInterface.mc.world.loadedTileEntityList.stream().filter(tileEntity -> MixinInterface.mc.player.getDistanceSq(tileEntity.getPos()) <= Math.pow(range.getValue(), 2)).forEach(tileEntity -> {
            if (colorManager.abstractColorRegistry.containsKey(tileEntity)) {
                switch (mode.getValue()) {
                    case 0:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, 0, 0, colorManager.abstractColorRegistry.get(tileEntity), RenderBuilder.RenderMode.Claw);
                        break;
                    case 1:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, 0, 0, colorManager.abstractColorRegistry.get(tileEntity), RenderBuilder.RenderMode.Outline);
                        break;
                    case 2:
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, 0, 0, colorManager.abstractColorRegistry.get(tileEntity), RenderBuilder.RenderMode.Fill);
                        break;
                    case 3:
                        GlStateManager.glLineWidth((float) lineWidth.getValue());
                        RenderUtil.drawBoxBlockPos(tileEntity.getPos(), 0, 0, 0, colorManager.abstractColorRegistry.get(tileEntity), RenderBuilder.RenderMode.Both);
                        break;
                }
            }
        });
    }
}