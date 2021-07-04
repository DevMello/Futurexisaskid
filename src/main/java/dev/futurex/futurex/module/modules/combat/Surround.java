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
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.player.PlayerUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder;
import dev.futurex.futurex.util.world.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linustouchtips & olliem5
 * @since 12/11/2020
 */

public class Surround extends Module {
    public Surround() {
        super("Surround", Category.COMBAT, "Surrounds your feet with obsidian");
    }

    public static Mode mode = new Mode("Mode", "Standard", "Full", "Anti-City");
    public static Mode disable = new Mode("Disable", "Off-Ground", "Completion", "Never");
    public static Mode centerPlayer = new Mode("Center", "Teleport", "NCP", "None");
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);
    public static Mode autoSwitch = new Mode("Switch", "SwitchBack", "Normal", "Packet", "None");

    public static Checkbox timeout = new Checkbox("Timeout", true);
    public static SubSlider timeoutTick = new SubSlider(timeout, "Timeout Ticks", 1.0D, 15.0D, 20.0D, 1);

    public static Checkbox raytrace = new Checkbox("Raytrace", true);
    public static Checkbox packet = new Checkbox("Packet", false);
    public static Checkbox swingArm = new Checkbox("Swing Arm", true);
    public static Checkbox antiGlitch = new Checkbox("Anti-Glitch", false);

    public static Checkbox rotate = new Checkbox("Rotate", false);
    public static SubCheckbox strict = new SubCheckbox(rotate, "Strict", true);

    public static Checkbox onlyObsidian = new Checkbox("Only Obsidian", true);
    public static Checkbox antiChainPop = new Checkbox("Anti-ChainPop", true);
    public static Checkbox chorusSave = new Checkbox("Chorus Save", false);

    public static Checkbox renderSurround = new Checkbox("Render", true);
    public static ColorPicker colorPicker = new ColorPicker(renderSurround, "Color Picker", new Color(0, 255, 0, 55));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(disable);
        addSetting(blocksPerTick);
        addSetting(timeout);
        addSetting(raytrace);
        addSetting(packet);
        addSetting(swingArm);
        addSetting(antiGlitch);
        addSetting(rotate);
        addSetting(centerPlayer);
        addSetting(onlyObsidian);
        addSetting(antiChainPop);
        addSetting(chorusSave);
        addSetting(renderSurround);
    }

    int obsidianSlot;
    public static boolean hasPlaced;
    Vec3d center = Vec3d.ZERO;
    int blocksPlaced = 0;
    BlockPos renderBlock;

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

        else {
            hasPlaced = false;
            center = PlayerUtil.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);

            switch (centerPlayer.getValue()) {
                case 0:
                    mc.player.motionX = 0;
                    mc.player.motionZ = 0;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(center.x, center.y, center.z, true));
                    mc.player.setPosition(center.x, center.y, center.z);
                    break;
                case 1:
                    mc.player.motionX = (center.x - mc.player.posX) / 2;
                    mc.player.motionZ = (center.z - mc.player.posZ) / 2;
                    break;
            }
        }
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        switch (disable.getValue()) {
            case 0:
                if (!mc.player.onGround)
                    this.disable();
                break;
            case 1:
                if (hasPlaced)
                    this.disable();
                break;
            case 2:
                if (timeout.getValue() && mode.getValue() != 2)
                    if (mc.player.ticksExisted % timeoutTick.getValue() == 0)
                        this.disable();
                break;
        }

        surroundPlayer();

        if (blocksPlaced == 0)
            hasPlaced = true;
    }

    public void surroundPlayer() {
        int previousSlot = mc.player.inventory.currentItem;

        switch (autoSwitch.getValue()) {
            case 0:
            case 1:
                InventoryUtil.switchToSlot(onlyObsidian.getValue() ? InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN) : InventoryUtil.getAnyBlockInHotbar());
                break;
            case 2:
                InventoryUtil.switchToSlotGhost(onlyObsidian.getValue() ? InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN) : InventoryUtil.getAnyBlockInHotbar());
                break;
        }

        for (Vec3d placePositions : getSurround()) {
            if (BlockUtil.getBlockResistance(new BlockPos(placePositions.add(mc.player.getPositionVector()))).equals(BlockUtil.BlockResistance.Blank)) {
                if (obsidianSlot != -1)
                    BlockUtil.placeBlock(new BlockPos(placePositions.add(mc.player.getPositionVector())), rotate.getValue(), strict.getValue(), raytrace.getValue(), packet.getValue(), swingArm.getValue(), antiGlitch.getValue());

                renderBlock = new BlockPos(placePositions.add(mc.player.getPositionVector()));
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue() && disable.getValue() != 2)
                    return;
            }
        }

        if (autoSwitch.getValue() == 0)
            InventoryUtil.switchToSlot(previousSlot);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderSurround.getValue() && renderBlock != null)
            RenderUtil.drawBoxBlockPos(renderBlock, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }

    List<Vec3d> getSurround() {
        switch (mode.getValue()) {
            case 0:
                return standardSurround;
            case 1:
                return fullSurround;
            case 2:
                return antiCitySurround;
        }

        return standardSurround;
    }

    List<Vec3d> standardSurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, 0, 0),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(0, 0, -1)
    ));

    List<Vec3d> fullSurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, -1, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, -1)
    ));

    List<Vec3d> antiCitySurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, 0, 0),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(0, 0, -1),
            new Vec3d(2, 0, 0),
            new Vec3d(-2, 0, 0),
            new Vec3d(0, 0, 2),
            new Vec3d(0, 0, -2),
            new Vec3d(3, 0, 0),
            new Vec3d(-3, 0, 0),
            new Vec3d(0, 0, 3),
            new Vec3d(0, 0, -3)
    ));

    @Override
    public String getHUDData() {
        return " " + centerPlayer.getMode(centerPlayer.getValue());
    }
}
