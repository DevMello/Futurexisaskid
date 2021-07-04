package dev.futurex.futurex.module.modules.misc;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.managers.config.ConfigManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.module.modules.movement.ElytraFlight;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.world.Timer;
import dev.futurex.futurex.util.world.Timer.Format;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class StashFinder extends Module {
    public StashFinder() {
        super("StashFinder", Category.MISC, "Automatically finds stashes and logs them");
    }

    public static Mode mode = new Mode("Mode", "Find", "Log", "Both");
    public static Checkbox chests = new Checkbox("Chests", true);
    public static Checkbox shulkers = new Checkbox("Shulkers", true);

    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static SubSlider delay = new SubSlider(rotate, "Delay Before Rotate", 0.0D, 120.0D, 600.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(chests);
        addSetting(shulkers);
        addSetting(rotate);
    }

    Timer timer = new Timer();
    private boolean stop = false;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (!mc.player.isElytraFlying()) {
            MessageUtil.sendClientMessage("You are not elytra flying!");
            this.disable();
            return;
        }

        if (!ModuleManager.getModuleByName("ElytraFlight").isEnabled()) {
            MessageUtil.sendClientMessage("Enable the module ElytraFlight!");
            this.toggle();
            return;
        }

        try {
            File stashLog = new File(ConfigManager.config.getAbsolutePath(), "StashFinder.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(stashLog));
            bw.write(mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "Singleplayer");
            bw.write("\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.isElytraFlying()) {
            if (rotate.getValue() && timer.passed((long) (1000 * delay.getValue()), Format.System))
                MessageUtil.sendClientMessage("No stashes found! Rotated you 90 degrees!");
                mc.player.rotationYaw += 90f;

            timer.reset();

            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());

            if (mc.player.posY <= 120) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }

        if (stop && (mode.getValue() == 0 || mode.getValue() == 2)) {
            this.disable();
            ModuleManager.getModuleByName("ElytraFlight").disable();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderEvent) {
        mc.entityRenderer.setupCameraTransform(renderEvent.getPartialTicks(), 0);

        Vec3d forward = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));
        RenderUtil.drawLine3D((float) forward.x, (float) forward.y + mc.player.getEyeHeight(), (float) forward.z, (float) forward.x, (float) forward.y + mc.player.getEyeHeight(), (float) (forward.z + 100), (float) 3, new Color(0, 255, 0));

        mc.entityRenderer.setupCameraTransform(renderEvent.getPartialTicks(), 0);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketChunkData) {
            SPacketChunkData chunkPacket = (SPacketChunkData) event.getPacket();

            int chest = 0;
            int shulker = 0;

            for (NBTTagCompound nbtTagCompound : chunkPacket.getTileEntityTags()) {
                String id = nbtTagCompound.getString("id");

                if (id.equals("minecraft:chest") && chests.getValue())
                    chest++;

                else if (id.equals("minecraft:shulker_box") && shulkers.getValue())
                    shulker++;
            }

            notifyPlayer(chunkPacket.getChunkX() * 16, chunkPacket.getChunkZ() * 16, chest, shulker);
        }
    }

    public void notifyPlayer(int x, int y, int chest, int shulker) {
        if (mode.getValue() == 0)
            stop = true;

        mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));

        if (chest > 0)
            MessageUtil.sendClientMessage("Found " + chest + " chests at " + x + " " + y + "!");

        if (shulker > 0)
            MessageUtil.sendClientMessage("Found " + shulker + " shulkers at " + x + " " + y + "!");

        if (mode.getValue() == 1 || mode.getValue() == 2) {
            try {
                File stashLog = new File(ConfigManager.config.getAbsolutePath(), "StashFinder.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(stashLog));
                bw.write("x:" + x);
                bw.write("\r\n");
                bw.write("y:" + y);
                bw.write("\r\n");
                bw.write("Chests:" + chest);
                bw.write("\r\n");
                bw.write("Shulkers:" + shulker);
                bw.write("\r\n");
                bw.write("\r\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}