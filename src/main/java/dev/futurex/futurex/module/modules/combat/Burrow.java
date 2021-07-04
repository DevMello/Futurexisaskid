package dev.futurex.futurex.module.modules.combat;

import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.Notification.Type;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.world.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips & olliem5
 * @since 11/29/2020
 */

public class Burrow extends Module {
    public Burrow() {
        super("Burrow", Category.COMBAT, "Rubberbands you into a block");
    }

    public static Mode mode = new Mode("Mode", "Rubberband", "Teleport", "Clip");
    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static Checkbox instant = new Checkbox("Instant", true);
    public static Checkbox centerPlayer = new Checkbox("Center", false);
    public static Checkbox silent = new Checkbox("Silent", false);
    public static Checkbox onGround = new Checkbox("On Ground", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(rotate);
        addSetting(instant);
        addSetting(centerPlayer);
        addSetting(silent);
        addSetting(onGround);
    }

    BlockPos originalPos;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        NotificationManager.addNotification(new Notification("Attempting to trigger a rubberband!", Type.Info));

        if (silent.getValue())
            mc.timer.tickLength = 1f;

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
        mc.player.setPosition(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ);

        InventoryUtil.switchToSlot(Blocks.OBSIDIAN);
        BlockUtil.placeBlock(originalPos, rotate.getValue(), false, true, false, true, false);

        mc.player.setPosition(mc.player.posX, mc.player.posY - 1.16610926093821D, mc.player.posZ);

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 7D, mc.player.posZ, true));

        this.disable();
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}