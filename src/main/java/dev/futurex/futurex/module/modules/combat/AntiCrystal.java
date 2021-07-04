package dev.futurex.futurex.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.Notification.Type;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import dev.futurex.futurex.util.world.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author olliem5 & linustouchtips
 * @since 7/01/20
 */

public class AntiCrystal extends Module {
    public AntiCrystal() {
        super("AntiCrystal", Category.COMBAT, "Minimises crystal damage with pressure plates");
    }

    public static Slider placeRange = new Slider("Place Range", 0.0, 5.5, 10.0, 1);
    public static Slider placeDelay = new Slider("Place Delay", 0, 2, 20, 1);

    public static Checkbox renderPlacement = new Checkbox("Render Placement", true);
    public static ColorPicker colorPicker = new ColorPicker(renderPlacement, "Color Picker",  new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(placeRange);
        addSetting(placeDelay);
        addSetting(renderPlacement);
    }

    int woodenPressurePlateSlot;
    int heavyWeightedPressurePlateSlot;
    int lightWeightedPressurePlateSlot;
    int stonePressurePlateSlot;

    Timer placeTimer = new Timer();
    BlockPos renderBlock;

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        handlePressurePlates(true);
    }

    public void onUpdate() {
        if (nullCheck()) 
            return;

        EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(Objects::nonNull).filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> mc.player.getDistance(entity) <= placeRange.getValue()).filter(entity -> !mc.world.getBlockState(entity.getPosition()).getBlock().equals(Blocks.WOODEN_PRESSURE_PLATE)).min(Comparator.comparing(entity -> mc.player.getDistance(entity))).orElse(null);

        if (entityEnderCrystal != null) {
            handlePressurePlates(false);

            if (placeTimer.passed((long) (placeDelay.getValue() * 100), Timer.Format.System)) {
                if (InventoryUtil.getHeldItem(Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE)) || InventoryUtil.getHeldItem(Item.getItemFromBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)) || InventoryUtil.getHeldItem(Item.getItemFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)) || InventoryUtil.getHeldItem(Item.getItemFromBlock(Blocks.STONE_PRESSURE_PLATE))) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(entityEnderCrystal.getPosition(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                    renderBlock = entityEnderCrystal.getPosition();
                }

                placeTimer.reset();
            }
        }
    }

    void handlePressurePlates(boolean search) {
        if (search) {
            woodenPressurePlateSlot = InventoryUtil.getBlockInHotbar(Blocks.WOODEN_PRESSURE_PLATE);
            heavyWeightedPressurePlateSlot = InventoryUtil.getBlockInHotbar(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
            lightWeightedPressurePlateSlot = InventoryUtil.getBlockInHotbar(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
            stonePressurePlateSlot = InventoryUtil.getBlockInHotbar(Blocks.STONE_PRESSURE_PLATE);

            if (woodenPressurePlateSlot == -1 && heavyWeightedPressurePlateSlot == -1 && lightWeightedPressurePlateSlot == -1 && stonePressurePlateSlot == -1) {
                NotificationManager.addNotification(new Notification("No Pressure Plate, " + ChatFormatting.RED + "Disabling!", Type.Info));
                this.disable();
            }
        }

        else {
            if (woodenPressurePlateSlot != -1)
                InventoryUtil.switchToSlot(Blocks.WOODEN_PRESSURE_PLATE);
            else if (heavyWeightedPressurePlateSlot != -1)
                InventoryUtil.switchToSlot(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
            else if (lightWeightedPressurePlateSlot != -1)
                InventoryUtil.switchToSlot(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
            else if (stonePressurePlateSlot != -1)
                InventoryUtil.switchToSlot(Blocks.STONE_PRESSURE_PLATE);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderBlock != null && renderPlacement.getValue())
            RenderUtil.drawBoxBlockPos(renderBlock, -0.9, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }
}
