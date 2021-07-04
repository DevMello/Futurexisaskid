package dev.futurex.futurex.util.world;

import dev.futurex.futurex.mixin.MixinInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class EntityUtil implements MixinInterface {

    public static List<Class<?>> getPassives() {
        return new ArrayList<>(Arrays.asList(EntityPigZombie.class, EntitySquid.class, EntityIronGolem.class, EntityWolf.class, EntityEnderman.class, EntityChicken.class, EntityCow.class, EntitySheep.class, EntityRabbit.class, EntityPig.class, EntityBat.class, EntityHorse.class, EntitySkeletonHorse.class, EntitySnowman.class));
    }

    public static List<Class<?>> getHostiles() {
        return new ArrayList<>(Arrays.asList(EntitySpider.class, EntitySkeleton.class, EntityZombie.class, EntityShulker.class, EntityBlaze.class, EntityCreeper.class, EntityCaveSpider.class, EntityBlaze.class, EntityGhast.class, EntityZombieVillager.class, EntityWitch.class, EntityVex.class, EntitySlime.class, EntityEvoker.class, EntitySpellcasterIllager.class, EntityIllusionIllager.class, EntityWitherSkeleton.class, EntityWither.class));
    }

    public static List<Class<?>> getVehicles() {

        return new ArrayList<>(Arrays.asList(EntityBoat.class, EntityMinecart.class));
    }

    public static boolean isPassive(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf) entity).isAngry())
            return false;

        if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)
            return true;

        return entity instanceof EntityIronGolem && ((EntityIronGolem) entity).getRevengeTarget() == null;
    }

    public static boolean isVehicle(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isHostileMob(Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity)) || entity instanceof EntitySpider;
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()))
                return true;
        }

        return false;
    }

    public static Vec3d interpolateEntity(Entity entity, float n) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n);
    }

    public static Vec3d interpolateEntityTime(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) time);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }

    public static double calculateDistanceWithPartialTicks(double originalPos, double finalPos, float renderPartialTicks) {
        return finalPos + (originalPos - finalPos) * mc.getRenderPartialTicks();
    }

    public static Vec3d interpolateEntityByTicks(Entity entity, float renderPartialTicks) {
        return new Vec3d (calculateDistanceWithPartialTicks(entity.posX, entity.lastTickPosX, renderPartialTicks) - mc.getRenderManager().renderPosX, calculateDistanceWithPartialTicks(entity.posY, entity.lastTickPosY, renderPartialTicks) - mc.getRenderManager().renderPosY, calculateDistanceWithPartialTicks(entity.posZ, entity.lastTickPosZ, renderPartialTicks) - mc.getRenderManager().renderPosZ);
    }

    public static double getDistance(double x, double y, double z, double finalX, double finalY, double finalZ) {
        double interpolationX = x - finalX;
        double interpolationY = y - finalY;
        double interpolationZ = z - finalZ;
        return MathHelper.sqrt(interpolationX * interpolationX + interpolationY * interpolationY + interpolationZ * interpolationZ);
    }
}
