package dev.futurex.futurex.module.modules.combat;

import dev.futurex.futurex.event.events.packet.PacketReceiveEvent;
import dev.futurex.futurex.event.events.packet.PacketSendEvent;
import dev.futurex.futurex.managers.*;
import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.Notification.Type;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.keybind.SubKeybind;
import dev.futurex.futurex.setting.mode.SubMode;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.util.client.MathUtil;
import dev.futurex.futurex.util.combat.EnemyUtil;
import dev.futurex.futurex.util.combat.crystal.Crystal;
import dev.futurex.futurex.util.combat.crystal.CrystalPosition;
import dev.futurex.futurex.util.combat.crystal.CrystalUtil;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.player.PlayerUtil;
import dev.futurex.futurex.util.player.rotation.Rotation;
import dev.futurex.futurex.util.player.rotation.Rotation.RotationMode;
import dev.futurex.futurex.util.player.rotation.RotationPriority;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.render.builder.RenderBuilder.RenderMode;
import dev.futurex.futurex.util.world.RaytraceUtil;
import dev.futurex.futurex.util.world.Timer;
import dev.futurex.futurex.util.world.WorldUtil;
import dev.futurex.futurex.util.world.hole.HoleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class AutoCrystal extends Module {
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Automatically places and explodes crystals");
    }

    public static Checkbox explode = new Checkbox("Break", true);
    public static SubMode breakMode = new SubMode(explode, "Mode", "All", "Smart");
    public static SubMode breakHand = new SubMode(explode, "BreakHand", "OffHand", "MainHand", "Both", "MultiSwing");
    public static SubSlider breakRange = new SubSlider(explode, "Break Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider breakDelay = new SubSlider(explode, "Break Delay", 0.0D, 80.0D, 200.0D, 0);
    public static SubSlider minBreakDamage = new SubSlider(explode, "Break Damage", 0.0D, 4.0D, 36.0D, 0);
    public static SubSlider breakAttempts = new SubSlider(explode, "Attacks", 0.0D, 1.0D, 5.0D, 0);
    public static SubMode sync = new SubMode(explode, "Sync", "None", "Instant", "Attack", "Sound", "Unsafe");
    public static SubCheckbox packetBreak = new SubCheckbox(explode, "Packet Break", true);
    public static SubCheckbox walls = new SubCheckbox(explode, "Through Walls", true);
    public static SubCheckbox inhibit = new SubCheckbox(explode, "Inhibit", true);
    public static SubCheckbox antiWeakness = new SubCheckbox(explode, "Anti-Weakness", false);

    public static Checkbox place = new Checkbox("Place", true);
    public static SubSlider placeRange = new SubSlider(place, "Place Range", 0.0D, 5.0D, 7.0D, 1);
    public static SubSlider enemyRange = new SubSlider(place, "Enemy Range", 0.0D, 10.0D, 15.0D, 1);
    public static SubSlider wallRange = new SubSlider(place, "Walls Range", 0.0D, 2.2D, 7.0D, 1);
    public static SubSlider placeDelay = new SubSlider(place, "Place Delay", 0.0D, 0.0D, 500.0D, 0);
    public static SubSlider randomDelay = new SubSlider(place, "Random Delay", 0.0D, 0.0D, 200.0D, 0);
    public static SubSlider minDamage = new SubSlider(place, "Minimum Damage", 0.0D, 7.0D, 36.0D, 0);
    public static SubSlider maxLocalDamage = new SubSlider(place, "Maximum Local Damage", 0.0D, 8.0D, 36.0D, 0);
    public static SubSlider placeAttempts = new SubSlider(explode, "Attempts", 0.0D, 1.0D, 5.0D, 0);
    public static SubMode autoSwitch = new SubMode(place, "Switch", "None", "Normal", "Packet");
    public static SubSlider switchDelay = new SubSlider(place, "Switch Delay", 0.0D, 0.0D, 50.0D, 0);
    public static SubMode rayTrace = new SubMode(place, "Ray-Trace", "Normal", "Quill-Trace", "None");
    public static SubCheckbox packetPlace = new SubCheckbox(place, "Packet Place", true);
    public static SubCheckbox boundary = new SubCheckbox(place, "Boundaries", false);
    public static SubCheckbox extend = new SubCheckbox(place, "Extension", false);
    public static SubCheckbox standby = new SubCheckbox(place, "Standby", false);
    public static SubCheckbox multiPlace = new SubCheckbox(place, "MultiPlace", false);

    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static SubMode rotateDuring = new SubMode(rotate, "When", "Break", "Place", "Both");
    public static SubMode rotateMode = new SubMode(rotate, "Type", "None", "Packet", "Legit");
    public static SubMode limiter = new SubMode(rotate, "Limiter", "None", "Narrow", "Upcoming");
    public static SubSlider maxAngle = new SubSlider(rotate, "Maximum Angle", 0.0D, 180.0D, 360.0D, 0);
    public static SubSlider minAngle = new SubSlider(rotate, "Minimum Angle", 0.0D, 180.0D, 360.0D, 0);
    public static SubCheckbox accurate = new SubCheckbox(rotate, "Accurate", false);
    public static SubCheckbox randomRotate = new SubCheckbox(rotate, "Random", true);
    public static SubCheckbox outBorder = new SubCheckbox(rotate, "OutBorder", false);
    public static SubCheckbox motion = new SubCheckbox(rotate, "Motion", false);
    public static SubCheckbox visible = new SubCheckbox(rotate, "Frustrum Visible", false);
    public static SubCheckbox rubberband = new SubCheckbox(rotate, "Rubberband", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubMode pauseMode = new SubMode(pause, "Pause Mode", "Break", "Place", "Both");
    public static SubSlider pauseHealth = new SubSlider(pause, "Pause Health", 0.0D, 7.0D, 36.0D, 0);
    public static SubCheckbox friendProtect = new SubCheckbox(pause, "Friend Protect", false);
    public static SubCheckbox whenMining = new SubCheckbox(pause, "Mining", false);
    public static SubCheckbox whenEating = new SubCheckbox(pause, "Eating", false);
    public static SubCheckbox whenOffline = new SubCheckbox(pause, "Offline", true);
    public static SubCheckbox closePlacements = new SubCheckbox(pause, "Close Placements", false);

    public static Checkbox override = new Checkbox("Override", true);
    public static SubSlider threshold = new SubSlider(override, "Threshold", 0.0D, 0.0D, 4.0D, 0);
    public static SubSlider overrideHealth = new SubSlider(override, "Override Health", 0.0D, 16.0D, 36.0D, 0);
    public static SubCheckbox armorBreaker = new SubCheckbox(override, "Armor Breaker", false);
    public static SubSlider armorScale = new SubSlider(override, "Armor Scale", 0.0D, 15.0D, 100.0D, 0);
    public static SubCheckbox overrideHole = new SubCheckbox(override, "HoleCampers", false);
    public static SubCheckbox aggressive = new SubCheckbox(override, "Aggressive", false);
    public static SubKeybind forceOverride = new SubKeybind(override, "Force Override", Keyboard.KEY_O);

    public static Checkbox calculations = new Checkbox("Calculations", true);
    public static SubMode timing = new SubMode(calculations, "Timing", "Linear", "Synchronous", "Tick");
    public static SubMode tick = new SubMode(calculations, "Tick", "Client", "Server", "Taiwan");
    public static SubMode heuristic = new SubMode(calculations, "Heuristic", "Damage", "MiniMax", "Atomic");
    public static SubSlider offset = new SubSlider(calculations, "Offset", 0.0D, 1.0D, 1.0D, 2);
    public static SubCheckbox serverConfirm = new SubCheckbox(calculations, "Server Confirm", true);
    public static SubCheckbox verifyPlace = new SubCheckbox(calculations, "Verify Placements", false);

    public static Checkbox logic = new Checkbox("Logic", true);
    public static SubMode logicMode = new SubMode(logic, "Crystal Logic", "Break -> Place", "Place -> Break");
    public static SubMode blockCalc = new SubMode(logic, "Block Logic", "Normal", "1.13+");
    public static SubMode enemyLogic = new SubMode(logic, "Enemy Logic", "Closest", "LowestHealth", "LowestArmor");

    public static Checkbox renderCrystal = new Checkbox("Render", true);
    public static ColorPicker colorPicker = new ColorPicker(renderCrystal, "Color Picker", new Color(250, 0, 250, 50));
    public static SubMode renderMode = new SubMode(renderCrystal, "Render Mode", "Fill", "Outline", "Both", "Claw");
    public static SubCheckbox renderDamage = new SubCheckbox(renderCrystal, "Render Damage", true);

    @Override
    public void setup() {
        addSetting(explode);
        addSetting(place);
        addSetting(rotate);
        addSetting(override);
        addSetting(pause);
        addSetting(calculations);
        addSetting(logic);
        addSetting(renderCrystal);
    }

    public static Timer breakTimer = new Timer();
    public static Timer placeTimer = new Timer();
    public static EntityPlayer crystalTarget = null;
    public static Rotation crystalRotation = null;
    public static Crystal crystal = null;
    public static CrystalPosition crystalPosition = new CrystalPosition(BlockPos.ORIGIN, 0, 0);

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();
        CrystalManager.resetCount();
        CrystalManager.resetLists();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        CrystalManager.resetCount();
        CrystalManager.resetLists();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            if (whenOffline.getValue())
                this.disable();

            return;
        }

        crystalTarget = WorldUtil.getTarget(enemyRange.getValue(), enemyLogic.getValue());

        if (tick.getValue() == 0)
            autoCrystal();
    }

    @Override
    public void onFastUpdate() {
        if (nullCheck())
            return;

        if (tick.getValue() == 2)
            autoCrystal();
    }

    @Override
    public void onServerUpdate() {
        if (nullCheck())
            return;

        if (tick.getValue() == 1)
            autoCrystal();
    }

    public void autoCrystal() {
        switch (logicMode.getValue()) {
            case 0:
                breakCrystal();
                placeCrystal();
                break;
            case 1:
                placeCrystal();
                breakCrystal();
                break;
        }
    }

    public void breakCrystal() {
        if (handleBreakPause() && (pauseMode.getValue() == 0 || pauseMode.getValue() == 2))
            return;

        crystal = new Crystal((EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(CrystalUtil::attackCheck).min(Comparator.comparing(crystal -> mc.player.getDistance(crystal))).orElse(null), 0, 0);
        if (crystal.getCrystal() != null && explode.getValue() && crystalTarget != null) {
            if (crystal.getCrystal().getDistance(mc.player) > breakRange.getValue())
                return;

            if (!RaytraceUtil.raytraceEntity(crystal.getCrystal()) && !walls.getValue())
                return;

            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS))
                InventoryUtil.switchToSlot(Items.DIAMOND_SWORD);

            if (breakTimer.passed(CrystalManager.skipTick ? 200 : (long) breakDelay.getValue(), Timer.Format.System)) {
                if (rotateDuring.getValue() == 0 || rotateDuring.getValue() == 2)
                    handleRotations(true);

                for (int i = 0; i < breakAttempts.getValue(); i++) {
                    CrystalUtil.attackCrystal(crystal.getCrystal(), packetBreak.getValue());
                    CrystalUtil.swingArm(breakHand.getValue());
                    CrystalManager.updateSwings();
                }

                if (sync.getValue() == 1)
                    crystal.getCrystal().setDead();

                breakTimer.reset();
            }

            if (!serverConfirm.getValue())
                CrystalManager.placedCrystals.removeIf(removePosition -> removePosition.getCrystalPosition().getDistance((int) crystal.getCrystal().posX, (int) crystal.getCrystal().posY, (int) crystal.getCrystal().posZ) <= 6);
        }
    }

    public void placeCrystal() {
        if (handlePlacePause() && (pauseMode.getValue() == 1 || pauseMode.getValue() == 2))
            return;

        List<CrystalPosition> crystalPositions = new ArrayList<>();
        CrystalPosition tempPosition;

        for (BlockPos calculatedPosition : CrystalUtil.crystalBlocks(mc.player, placeRange.getValue(), extend.getValue(), !multiPlace.getValue(), blockCalc.getValue())) {
            if (handleRaytrace(calculatedPosition))
                continue;

            if (verifyPlace.getValue() && mc.player.getDistanceSq(calculatedPosition) > Math.pow(breakRange.getValue(), 2))
                continue;

            double calculatedTargetDamage = CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + offset.getValue(), calculatedPosition.getZ() + 0.5, crystalTarget);
            double calculatedSelfDamage = mc.player.capabilities.isCreativeMode ? 0 : CrystalUtil.calculateDamage(calculatedPosition.getX() + 0.5, calculatedPosition.getY() + offset.getValue(), calculatedPosition.getZ() + 0.5, mc.player);

            if (calculatedTargetDamage < (handleMinDamage(calculatedPosition) ? 2 : minDamage.getValue()))
                continue;

            if (calculatedSelfDamage > maxLocalDamage.getValue())
                continue;

            crystalPositions.add(new CrystalPosition(calculatedPosition, calculatedTargetDamage, calculatedSelfDamage));
        }

        tempPosition = crystalPositions.stream().max(Comparator.comparing(idealCrystalPosition -> CrystalUtil.getHeuristic(idealCrystalPosition, heuristic.getValue()))).orElse(null);

        if (tempPosition == null) {
            crystalTarget = null;
            crystalPosition = null;
            crystalRotation.restoreRotation();
            return;
        }

        crystalPosition = tempPosition;

        switch (autoSwitch.getValue()) {
            case 1:
                InventoryUtil.switchToSlot(Items.END_CRYSTAL);
                break;
            case 2:
                InventoryUtil.switchToSlotGhost(Items.END_CRYSTAL);
                break;
        }

        if (handleDelay() && place.getValue() && InventoryUtil.getHeldItem(Items.END_CRYSTAL) && crystalPosition.getCrystalPosition() != BlockPos.ORIGIN) {
            if (rotateDuring.getValue() == 1 || rotateDuring.getValue() == 2)
                handleRotations(false);

            for (int i = 0; i < placeAttempts.getValue(); i++) {
                CrystalUtil.placeCrystal(crystalPosition.getCrystalPosition(), CrystalUtil.getEnumFacing(boundary.getValue(), crystalPosition.getCrystalPosition()), packetPlace.getValue());
                CrystalManager.updatePlacements();
            }

            placeTimer.reset();
        }

        if (timing.getValue() == 1) {
            for (Entity crystal : mc.world.loadedEntityList.stream().filter(CrystalUtil::attackCheck).collect(Collectors.toList())) {
                if (crystalPosition.getCrystalPosition().getDistance((int) crystal.posX, (int) crystal.posY, (int) crystal.posZ) < 2) {
                    CrystalUtil.attackCrystal((EntityEnderCrystal) crystal, packetBreak.getValue());
                    CrystalUtil.swingArm(breakHand.getValue());
                    CrystalManager.updateSwings();
                }
            }
        }
    }

    public boolean handleDelay() {
        Random random = new Random();
        return standby.getValue() ? CrystalManager.cleared : placeTimer.passed((long) (placeDelay.getValue() + random.nextInt((int) randomDelay.getValue() + 1)), Timer.Format.System);
    }
    
    public boolean handleBreakPause() {
        if (ModuleManager.getModuleByName("Surround").isEnabled() && !Surround.hasPlaced || ModuleManager.getModuleByName("AutoTrap").isEnabled() && !AutoTrap.hasPlaced || ModuleManager.getModuleByName("SelfTrap").isEnabled() && !SelfTrap.hasPlaced || ModuleManager.getModuleByName("HoleFill").isEnabled() && HoleFill.processing)
            return true;
        else if (crystalTarget == null)
            return true;

        else if (friendProtect.getValue()) {
            for (EntityPlayer friend : Objects.requireNonNull(WorldUtil.getNearbyFriends(placeRange.getValue()))) {
                if (EnemyUtil.getHealth(friend) - (CrystalUtil.calculateDamage(crystal.crystal.posX + 0.5, crystal.crystal.posY + 1, crystal.crystal.posZ + 0.5, friend)) <= pauseHealth.getValue())
                    return true;
            }
        }

        else if (PlayerUtil.getHealth() < pauseHealth.getValue())
            return true;
        else if (PlayerUtil.isEating() && whenEating.getValue() || PlayerUtil.isMining() && whenMining.getValue())
            return true;
        else if (closePlacements.getValue() && mc.player.getDistance(crystal.crystal) < 1.5)
            return true;
        else if (!SwitchManager.switchReady((int) switchDelay.getValue()))
            return true;
        else if (CrystalManager.swings > 50 && inhibit.getValue()) {
            CrystalManager.updateTicks(true);

            NotificationManager.addNotification(new Notification("AutoCrystal Frozen! Pausing for 1 tick!", Notification.Type.Warning));

            crystal = null;
            if (sync.getValue() == 4)
                crystal.getCrystal().setDead();

            return false;
        }

        return false;
    }

    public boolean handlePlacePause() {
        if (ModuleManager.getModuleByName("Surround").isEnabled() && !Surround.hasPlaced || ModuleManager.getModuleByName("AutoTrap").isEnabled() && !AutoTrap.hasPlaced || ModuleManager.getModuleByName("SelfTrap").isEnabled() && !SelfTrap.hasPlaced || ModuleManager.getModuleByName("HoleFill").isEnabled() && HoleFill.processing)
            return true;
        else if (crystalTarget == null)
            return true;

        else if (friendProtect.getValue()) {
            for (EntityPlayer friend : Objects.requireNonNull(WorldUtil.getNearbyFriends(placeRange.getValue()))) {
                if (EnemyUtil.getHealth(friend) - (CrystalUtil.calculateDamage(crystalPosition.getCrystalPosition().x + 0.5, crystalPosition.getCrystalPosition().y + offset.getValue(), crystalPosition.getCrystalPosition().z + 0.5, friend)) <= pauseHealth.getValue())
                    return true;
            }
        }

        else if (PlayerUtil.getHealth() < pauseHealth.getValue())
            return true;
        else
            return PlayerUtil.isEating() && whenEating.getValue() || PlayerUtil.isMining() && whenMining.getValue();

        return false;
    }

    public boolean handleRaytrace(BlockPos calculatedPosition) {
        if (mc.player.getDistanceSq(calculatedPosition) > Math.pow(wallRange.getValue(), 2)) {
            switch (rayTrace.getValue()) {
                case 0:
                    return !RaytraceUtil.raytraceBlock(calculatedPosition, offset.getValue());
                case 1:
                    return !RaytraceUtil.raytraceQuill(calculatedPosition, offset.getValue());
                case 2:
                    return true;
            }
        }

        return false;
    }

    public boolean handleMinDamage(BlockPos calculatedPosition) {
        if (((GearManager.lastTickPop && aggressive.getValue()) || (CrystalUtil.calculateDamage(calculatedPosition.x + 0.5, calculatedPosition.y + offset.getValue(), calculatedPosition.z + 0.5, crystalTarget) * threshold.getValue()) > EnemyUtil.getHealth(crystalTarget)) && threshold.getValue() != 0)
            return true;

        if (crystalTarget != null && HoleUtil.isInHole(crystalTarget)) {
            if (EnemyUtil.getHealth(crystalTarget) < overrideHealth.getValue())
                return true;
            else if (EnemyUtil.getArmor(crystalTarget, armorBreaker.getValue(), armorScale.getValue()))
                return true;
            else if (overrideHole.getValue())
                return true;
            else
                return Keyboard.isKeyDown(forceOverride.getKey());
        }

        return false;
    }

    public void handleRotations(boolean target) {
        if (crystalTarget != null && crystalPosition.getCrystalPosition() != null && crystal.getCrystal() != null) {
            switch (rotateMode.getValue()) {
                case 0:
                    crystalRotation = new Rotation(0, 0, RotationMode.None, RotationPriority.Lowest);
                    break;
                case 1:
                    crystalRotation = new Rotation((target ? (accurate.getValue() ? RotationUtil.searchCenter(crystal.getCrystal().boundingBox, outBorder.getValue(), randomRotate.getValue(), motion.getValue(), visible.getValue()).getRotation() : RotationUtil.getAngles(crystal.getCrystal())) : (accurate.getValue() ? RotationUtil.searchCenter(crystalPosition.getCrystalPosition()).getRotation() : RotationUtil.getPositionAngles(crystalPosition.getCrystalPosition())))[0], (target ? (accurate.getValue() ? RotationUtil.searchCenter(crystal.getCrystal().boundingBox, outBorder.getValue(), randomRotate.getValue(), motion.getValue(), visible.getValue()).getRotation() : RotationUtil.getAngles(crystal.getCrystal())) : (accurate.getValue() ? RotationUtil.searchCenter(crystalPosition.getCrystalPosition()).getRotation() : RotationUtil.getPositionAngles(crystalPosition.getCrystalPosition())))[1], RotationMode.Packet, RotationPriority.Highest);
                    break;
                case 2:
                    crystalRotation = new Rotation((target ? (accurate.getValue() ? RotationUtil.searchCenter(crystal.getCrystal().boundingBox, outBorder.getValue(), randomRotate.getValue(), motion.getValue(), visible.getValue()).getRotation() : RotationUtil.getAngles(crystal.getCrystal())) : (accurate.getValue() ? RotationUtil.searchCenter(crystalPosition.getCrystalPosition()).getRotation() : RotationUtil.getPositionAngles(crystalPosition.getCrystalPosition())))[0], (target ? (accurate.getValue() ? RotationUtil.searchCenter(crystal.getCrystal().boundingBox, outBorder.getValue(), randomRotate.getValue(), motion.getValue(), visible.getValue()).getRotation() : RotationUtil.getAngles(crystal.getCrystal())) : (accurate.getValue() ? RotationUtil.searchCenter(crystalPosition.getCrystalPosition()).getRotation() : RotationUtil.getPositionAngles(crystalPosition.getCrystalPosition())))[1], RotationMode.Legit, RotationPriority.Highest);
                    break;
            }
        }

        if (limiter.getValue() != 0)
            crystalRotation = RotationUtil.rotationStep(RotationManager.serverRotation, crystalRotation, (float) (((randomRotate.getValue() ? Math.random() : 1) * (maxAngle.getValue() - minAngle.getValue())) + minAngle.getValue()), limiter.getValue());

        RotationManager.rotationQueue.add(crystalRotation);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        try {
            if (renderCrystal.getValue() && crystalPosition.getCrystalPosition() != BlockPos.ORIGIN && crystalPosition != null && crystalPosition.getCrystalPosition() != null && crystalTarget != null) {
                switch (renderMode.getValue()) {
                    case 0:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Fill);
                        break;
                    case 1:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Outline);
                        break;
                    case 2:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Both);
                        break;
                    case 3:
                        RenderUtil.drawBoxBlockPos(crystalPosition.getCrystalPosition(), 0, 0, 0, colorPicker.getColor(), RenderMode.Claw);
                        break;
                }

                if (renderDamage.getValue())
                    RenderUtil.drawNametagFromBlockPos(crystalPosition.getCrystalPosition(), 0.5f, String.valueOf(MathUtil.roundAvoid(crystalPosition.getTargetDamage() / 2.16667, 1)));
            }
        } catch (Exception ignored) {

        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            if (((SPacketSoundEffect) event.getPacket()).getCategory() == SoundCategory.BLOCKS && ((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity crystal : mc.world.loadedEntityList) {
                    if (crystal instanceof EntityEnderCrystal)
                        if (crystal.getDistance(((SPacketSoundEffect) event.getPacket()).posX, ((SPacketSoundEffect) event.getPacket()).posY, ((SPacketSoundEffect) event.getPacket()).posZ) <= 6) {
                            if (sync.getValue() == 3)
                                crystal.setDead();

                            if (serverConfirm.getValue())
                                CrystalManager.placedCrystals.removeIf(calculatedPosition -> calculatedPosition.getCrystalPosition().getDistance((int) ((SPacketSoundEffect) event.getPacket()).getX(), (int)  ((SPacketSoundEffect) event.getPacket()).getY(), (int) ((SPacketSoundEffect) event.getPacket()).getZ()) <= 6);
                        }
                }
            }
        }

        if (event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && timing.getValue() == 0 && explode.getValue()) {
            if (mc.player.getDistance(((SPacketSpawnObject) event.getPacket()).getX(), ((SPacketSpawnObject) event.getPacket()).getY(), ((SPacketSpawnObject) event.getPacket()).getZ()) > breakRange.getValue())
                return;

            CrystalUtil.attackCrystal(((SPacketSpawnObject) event.getPacket()).getEntityID());
        }

        if (event.getPacket() instanceof SPacketPlayerPosLook && rubberband.getValue()) {
            NotificationManager.addNotification(new Notification("Rubberband detected! Reset Rotations!", Type.Warning));
            crystalRotation.restoreRotation();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            if (sync.getValue() == 2)
                Objects.requireNonNull(((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world)).setDead();
        }
    }

    @Override
    public String getHUDData() {
        return crystalTarget != null ? " " + crystalTarget.getName() : " None";
    }
}