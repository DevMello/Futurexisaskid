package dev.futurex.futurex.util.world;

import com.mojang.authlib.GameProfile;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.combat.EnemyUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/26/2020
 */

public class WorldUtil implements MixinInterface {

    public static void createFakePlayer(@Nullable String name, boolean copyInventory, boolean copyAngles, boolean health, boolean player, int entityID) {
        EntityOtherPlayerMP entity = player ? new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile()) : new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), name));
        entity.copyLocationAndAnglesFrom(mc.player);

        if (copyInventory)
            entity.inventory.copyInventory(mc.player.inventory);

        if (copyAngles) {
            entity.rotationYaw = mc.player.rotationYaw;
            entity.rotationYawHead = mc.player.rotationYawHead;
        }

        if (health)
            entity.setHealth(mc.player.getHealth() + mc.player.getAbsorptionAmount());

        mc.world.addEntityToWorld(entityID, entity);
    }

    public static EntityPlayer getClosestPlayer(double range) {
        EntityPlayer closestPlayer = null;

        for (EntityPlayer nearbyPlayer : mc.world.playerEntities) {
            if (nearbyPlayer == mc.player)
                continue;

            if (EnemyUtil.getHealth(nearbyPlayer) < 0)
                continue;

            if (mc.player.getDistance(nearbyPlayer) > range)
                continue;

            if (FriendManager.isFriend(nearbyPlayer.getName()) && FriendManager.isFriendModuleEnabled())
                continue;

            closestPlayer = nearbyPlayer;
        }

        return closestPlayer;
    }

    public static EntityPlayer getTarget(double range, int mode) {
        EntityPlayer targetPlayer = null;
        List<EntityPlayer> selectionPlayers = new ArrayList<>();

        for (EntityPlayer nearbyPlayer : mc.world.playerEntities) {
            if (nearbyPlayer == mc.player)
                continue;

            if (EnemyUtil.getHealth(nearbyPlayer) < 0)
                continue;

            if (mc.player.getDistance(nearbyPlayer) > range)
                continue;

            if (FriendManager.isFriend(nearbyPlayer.getName()) && FriendManager.isFriendModuleEnabled())
                continue;

            selectionPlayers.add(nearbyPlayer);
        }

        switch (mode) {
            case 0:
                targetPlayer = selectionPlayers.stream().min(Comparator.comparing(target -> mc.player.getDistance(target))).orElse(null);
                break;
            case 1:
                targetPlayer = selectionPlayers.stream().min(Comparator.comparing(EnemyUtil::getHealth)).orElse(null);
                break;
            case 2:
                targetPlayer = selectionPlayers.stream().min(Comparator.comparing(EnemyUtil::getArmor)).orElse(null);
                break;
        }

        return targetPlayer;
    }

    public static List<EntityPlayer> getNearbyPlayers(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        List<EntityPlayer> nearbyPlayers = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !(EnemyUtil.getHealth(entityPlayer) < 0)).collect(Collectors.toList());

        nearbyPlayers.removeIf(closestPlayer -> FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled());

        return nearbyPlayers;
    }

    public static List<EntityPlayer> getNearbyTargets(double range, int mode) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        List<EntityPlayer> nearbyTargets = null;

        switch (mode) {
            case 0:
                nearbyTargets = getNearbyPlayers(range);
                break;
            case 1:
                nearbyTargets = getNearbyPlayers(range).stream().sorted(Comparator.comparing(EnemyUtil::getHealth)).collect(Collectors.toList());
                break;
            case 2:
                nearbyTargets = getNearbyPlayers(range).stream().sorted(Comparator.comparing(EnemyUtil::getArmor)).collect(Collectors.toList());
                break;
        }

        nearbyTargets.removeIf(closestPlayer -> FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled());

        return nearbyTargets;
    }

    public static List<EntityPlayer> getNearbyFriends(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        return mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).filter(entityPlayer -> FriendManager.isFriend(entityPlayer.getName())).collect(Collectors.toList());
    }

    public static void disconnectFromWorld(Module module) {
        module.disable();
        mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
    }
}
