package dev.futurex.futurex.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.Notification.Type;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import dev.futurex.futurex.util.world.BlockUtil;
import dev.futurex.futurex.util.world.BlockUtil.BlockResistance;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author linustouchtips & olliem5
 * @since 11/28/2020
 * more or less a paste of autotrap
 */

public class SelfTrap extends Module {
    public SelfTrap() {
        super("SelfTrap", Category.COMBAT, "Automatically traps yourself");
    }

    public static Mode mode = new Mode("Mode", "Head", "Anti-FacePlace", "Full");
    public static Slider delay = new Slider("Delay", 0.0D, 3.0D, 6.0D, 0);
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);
    public static Mode autoSwitch = new Mode("Switch", "SwitchBack", "Normal", "Packet", "None");
    public static Checkbox raytrace = new Checkbox("Raytrace", true);
    public static Checkbox packet = new Checkbox("Packet", false);
    public static Checkbox swingArm = new Checkbox("Swing Arm", true);
    public static Checkbox antiGlitch = new Checkbox("Anti-Glitch", false);

    public static Checkbox rotate = new Checkbox("Rotate", false);
    public static SubCheckbox strict = new SubCheckbox(rotate, "Strict", false);

    public static Checkbox disable = new Checkbox("Disables", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",  new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(blocksPerTick);
        addSetting(raytrace);
        addSetting(packet);
        addSetting(swingArm);
        addSetting(antiGlitch);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    int obsidianSlot;
    BlockPos placeBlock;
    public static boolean hasPlaced;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();

        obsidianSlot = InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);

        if (obsidianSlot == -1) {
            NotificationManager.addNotification(new Notification("No Obsidian, " + ChatFormatting.RED + "Disabling!", Type.Info));
            this.disable();
        }

        else
            hasPlaced = false;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (hasPlaced && disable.getValue())
            this.disable();

        int blocksPlaced = 0;
        int previousSlot = mc.player.inventory.currentItem;

        switch (autoSwitch.getValue()) {
            case 0:
            case 1:
                InventoryUtil.switchToSlot(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));
                break;
            case 2:
                InventoryUtil.switchToSlotGhost(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));
                break;
        }

        for (Vec3d autoTrapBox : getTrap()) {
            if (BlockUtil.getBlockResistance(new BlockPos(autoTrapBox.add(mc.player.getPositionVector()))).equals(BlockResistance.Blank)) {
                InventoryUtil.switchToSlot(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));

                if (obsidianSlot != -1)
                    BlockUtil.placeBlock(new BlockPos(autoTrapBox.add(mc.player.getPositionVector())), rotate.getValue(), strict.getValue(), raytrace.getValue(), packet.getValue(), swingArm.getValue(), antiGlitch.getValue());

                placeBlock = new BlockPos(autoTrapBox.add(mc.player.getPositionVector()));
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue())
                    return;
            }
        }

        if (autoSwitch.getValue() == 0)
            InventoryUtil.switchToSlot(previousSlot);

        if (blocksPlaced == 0)
            hasPlaced = true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (placeBlock != null)
            RenderUtil.drawBoxBlockPos(placeBlock, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }

    public List<Vec3d> getTrap() {
        switch (mode.getValue()) {
            case 0:
                return headTrap;
            case 1:
                return faceTrap;
            case 2:
                return fullTrap;
        }

        return headTrap;
    }

    List<Vec3d> headTrap = new ArrayList<>(Collections.singletonList(
            new Vec3d(0, 2, 0)
    ));

    List<Vec3d> faceTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, 1, -1),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0)
    ));

    List<Vec3d> fullTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, -1),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, 0, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0),
            new Vec3d(0, 2, -1),
            new Vec3d(0, 2, 1),
            new Vec3d(0, 2, 0)
    ));
}