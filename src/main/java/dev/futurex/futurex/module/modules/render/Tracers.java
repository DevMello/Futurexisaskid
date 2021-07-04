package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.managers.ColorManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.world.EntityUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", Category.RENDER, "Draws a line to entities");
    }

    public static dev.futurex.futurex.setting.checkbox.Checkbox players = new dev.futurex.futurex.setting.checkbox.Checkbox("Players", true);
    public static ColorPicker playerPicker = new ColorPicker(players, "Player Picker", new Color(215, 46, 46));

    public static dev.futurex.futurex.setting.checkbox.Checkbox animals = new dev.futurex.futurex.setting.checkbox.Checkbox("Animals", true);
    public static ColorPicker animalPicker = new ColorPicker(animals, "Animal Picker", new Color(0, 200, 0));

    public static dev.futurex.futurex.setting.checkbox.Checkbox mobs = new dev.futurex.futurex.setting.checkbox.Checkbox("Mobs", true);
    public static ColorPicker mobsPicker = new ColorPicker(mobs, "Mob Picker", new Color(131, 19, 199));

    public static dev.futurex.futurex.setting.checkbox.Checkbox items = new dev.futurex.futurex.setting.checkbox.Checkbox("Items", true);
    public static ColorPicker itemsPicker = new ColorPicker(items, "Items Picker",  new Color(199, 103, 19));

    public static dev.futurex.futurex.setting.checkbox.Checkbox crystals = new Checkbox("Crystals", true);
    public static ColorPicker crystalPicker = new ColorPicker(crystals, "Crystal Picker", new Color(199, 19, 139));

    public static Slider lineWidth = new Slider("Line Width", 0.0D, 0.75D, 5.0D, 2);

    @Override
    public void setup() {
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
        addSetting(items);
        addSetting(crystals);
        addSetting(lineWidth);
    }

    ColorManager colorManager = new ColorManager();

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (nullCheck())
            return;

        colorManager.registerAbstractColor(EntityOtherPlayerMP.class, playerPicker.getColor());
        colorManager.registerAbstractColorList(EntityUtil.getPassives(), animalPicker.getColor());
        colorManager.registerAbstractColorList(EntityUtil.getHostiles(), mobsPicker.getColor());
        colorManager.registerAbstractColor(EntityItem.class, itemsPicker.getColor());
        colorManager.registerAbstractColor(EntityEnderCrystal.class, crystalPicker.getColor());

        if (MixinInterface.mc.getRenderManager().options == null)
            return;

        MixinInterface.mc.world.loadedEntityList.stream().filter(entity -> MixinInterface.mc.player != entity).forEach(entity -> {
            Vec3d pos = EntityUtil.interpolateEntity(entity, event.getPartialTicks()).subtract(MixinInterface.mc.getRenderManager().renderPosX, MixinInterface.mc.getRenderManager().renderPosY, MixinInterface.mc.getRenderManager().renderPosZ);

            if (colorManager.abstractColorRegistry.containsKey(entity.getClass())) {
                MixinInterface.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

                Vec3d forward = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(MixinInterface.mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(MixinInterface.mc.player.rotationYaw));
                RenderUtil.drawLine3D((float) forward.x, (float) forward.y + MixinInterface.mc.player.getEyeHeight(), (float) forward.z, (float) pos.x, (float) pos.y, (float) pos.z, (float) lineWidth.getValue(), colorManager.abstractColorRegistry.get(entity.getClass()));

                MixinInterface.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            }
        });
    }
}