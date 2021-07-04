package dev.futurex.futurex.util.combat.crystal;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.modules.combat.AutoCrystal;
import dev.futurex.futurex.util.world.BlockUtil;
import dev.futurex.futurex.util.world.BlockUtil.BlockResistance;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class CrystalUtil implements MixinInterface {

    public static List<Entity> crystalEntities() {
        return mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).collect(Collectors.toList());
    }

    public static void attackCrystal(EntityEnderCrystal crystal, boolean packet) {
        if (packet)
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
        else
            mc.playerController.attackEntity(mc.player, crystal);
    }

    public static void attackCrystal(int entityId) {
        CPacketUseEntity sequentialCrystal = new CPacketUseEntity();
        sequentialCrystal.entityId = entityId;
        sequentialCrystal.action = CPacketUseEntity.Action.ATTACK;
        mc.player.connection.sendPacket(sequentialCrystal);
    }

    public static boolean attackCheck(Entity crystal) {
        if (!(crystal instanceof EntityEnderCrystal))
            return false;

        return AutoCrystal.breakMode.getValue() == 0;
    }

    public static void swingArm(int mode) {
        switch (mode) {
            case 0:
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case 1:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            case 2:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case 3:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
        }
    }

    public static void placeCrystal(BlockPos placePos, EnumFacing enumFacing, boolean packet) {
        if (packet)
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, enumFacing, mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
        else
            mc.playerController.processRightClickBlock(mc.player, mc.world, placePos, enumFacing, new Vec3d(0, 0, 0), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

    public static EnumFacing getEnumFacing(boolean rayTrace, BlockPos placePosition) {
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(placePosition.getX() + 0.5, placePosition.getY() - 0.5, placePosition.getZ() + 0.5));

        if (placePosition.getY() == 255)
            return EnumFacing.DOWN;

        if (rayTrace) {
            return (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        }

        return EnumFacing.UP;
    }

    public static List<BlockPos> crystalBlocks(EntityPlayer entityPlayer, double placeRange, boolean prediction, boolean antiSurround, int blockCalc) {
        return BlockUtil.getNearbyBlocks(entityPlayer, placeRange - 1, prediction).stream().filter(blockPos -> canPlaceCrystal(blockPos, antiSurround, blockCalc == 1)).collect(Collectors.toList());
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean antiSurround, boolean thirteen) {
        try {
            if (BlockUtil.getBlockResistance(blockPos) != BlockResistance.Unbreakable && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
                return false;

            if (!thirteen && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) != BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) != BlockResistance.Blank)
                return false;

            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 1, 0)))) {
                if (entity.isDead || antiSurround && entity instanceof EntityEnderCrystal)
                    continue;

                return false;
            }

            if (!thirteen) {
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 2, 0)))) {
                    if (entity.isDead || antiSurround && entity instanceof EntityEnderCrystal)
                        continue;

                    return false;
                }
            }
        } catch (Exception ignored) {
            return false;
        }

        return true;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        try {
            double factor = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0f) * entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
            float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0 * 7.0 * 12.0f + 1.0);
            double damage = 1.0;

            if (entity instanceof EntityLivingBase)
                damage = getBlastReduction((EntityLivingBase) entity, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));

            return (float) damage;
        } catch (Exception ignored) {

        }

        return 0;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

            if (entity.isPotionActive(MobEffects.RESISTANCE))
                damage -= damage / 4.0f;

            return damage;
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public static double getHeuristic(CrystalPosition crystalPosition, int mode) {
        switch (mode) {
            case 0:
                return crystalPosition.getTargetDamage();
            case 1:
                return crystalPosition.getTargetDamage() - crystalPosition.getSelfDamage();
            case 2:
                return crystalPosition.getTargetDamage() - (crystalPosition.getSelfDamage() + mc.player.getDistance(crystalPosition.getCrystalPosition().x, crystalPosition.getCrystalPosition().y, crystalPosition.getCrystalPosition().z));
        }

        return 0;
    }
}