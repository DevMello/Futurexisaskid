package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.managers.ColorManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.module.modules.render.esp.ESPMode;
import dev.futurex.futurex.module.modules.render.esp.modes.*;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.world.EntityUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/26/2020
 * @updated 01/06/2021
 */

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.RENDER, "Highlights entities");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Glow", "2D", "CS:GO", "Wireframe", "Shader");

    public static Checkbox players = new Checkbox("Players", true);
    public static ColorPicker playerPicker = new ColorPicker(players, "Player Picker", new Color(215, 46, 46));

    public static Checkbox animals = new Checkbox("Animals", true);
    public static ColorPicker animalPicker = new ColorPicker(animals, "Animal Picker", new Color(0, 200, 0));

    public static Checkbox mobs = new Checkbox("Mobs", true);
    public static ColorPicker mobsPicker = new ColorPicker(mobs, "Mob Picker", new Color(131, 19, 199));

    public static Checkbox items = new Checkbox("Items", true);
    public static ColorPicker itemsPicker = new ColorPicker(items, "Item Picker", new Color(199, 196, 19));

    public static Checkbox vehicles = new Checkbox("Vehicles", true);
    public static ColorPicker vehiclePicker = new ColorPicker(vehicles, "Vehicle Picker", new Color(199, 103, 19));

    public static Checkbox crystals = new Checkbox("Crystals", true);
    public static ColorPicker crystalPicker = new ColorPicker(crystals, "Crystal Picker", new Color(199, 19, 139, 255));

    public static Checkbox xqz = new Checkbox("XQZ", false);
    public static ColorPicker xqzPicker = new ColorPicker(xqz, "XQZ Picker", new Color(19, 70, 199, 255));

    public static Slider lineWidth = new Slider("Width", 0.0D, 2.5D, 5.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
        addSetting(items);
        addSetting(vehicles);
        addSetting(crystals);
        addSetting(xqz);
        addSetting(lineWidth);
    }

    public static ESPMode espMode;
    public static ColorManager colorManager = new ColorManager();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        colorManager.registerAbstractColor(EntityOtherPlayerMP.class, playerPicker.getColor());
        colorManager.registerAbstractColorList(EntityUtil.getPassives(), animalPicker.getColor());
        colorManager.registerAbstractColorList(EntityUtil.getHostiles(), mobsPicker.getColor());
        colorManager.registerAbstractColor(EntityItem.class, itemsPicker.getColor());
        colorManager.registerAbstractColorList(EntityUtil.getVehicles(), vehiclePicker.getColor());
        colorManager.registerAbstractColor(EntityEnderCrystal.class, crystalPicker.getColor());
        colorManager.registerColor("XQZ", xqzPicker.getColor());
        colorManager.registerColor("Friend", Color.CYAN);

        switch (mode.getValue()) {
            case 0:
                espMode = new Outline();
                break;
            case 1:
                espMode = new Glow();
                break;
            case 2:
                espMode = new TwoD();
                break;
            case 3:
                espMode = new CSGO();
                break;
            case 4:
                espMode = new Wireframe();
                break;
        }

        if (mode.getValue() != 1)
            return;

        ShaderGroup outlineShaderGroup = MixinInterface.mc.renderGlobal.entityOutlineShader;
        /*TODO: List<Shader> shaders = outlineShaderGroup.listShaders;

        shaders.forEach(shader -> {
            ShaderUniform outlineRadius = shader.getShaderManager().getShaderUniform("Radius");

            if (outlineRadius != null)
                outlineRadius.set((float) (lineWidth.getValue() / 2));
        });*/
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        try {
            espMode.drawESP();
            espMode.drawESPOverlay(eventRender);
        } catch (Exception ignored) {

        }

    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}