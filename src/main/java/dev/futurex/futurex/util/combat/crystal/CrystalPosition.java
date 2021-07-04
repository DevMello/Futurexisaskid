package dev.futurex.futurex.util.combat.crystal;

import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 02/01/2021
 */

public class CrystalPosition {

    BlockPos crystalPosition;
    double targetDamage;
    double selfDamage;
    public boolean frozen;
    public boolean placing;
    public boolean breaking;

    public CrystalPosition(BlockPos crystalPosition, double targetDamage, double selfDamage) {
        this.crystalPosition = crystalPosition;
        this.targetDamage = targetDamage;
        this.selfDamage = selfDamage;
        this.frozen = false;
        this.breaking = false;
        this.placing = false;
    }

    public BlockPos getCrystalPosition() {
        return this.crystalPosition;
    }

    public double getTargetDamage() {
        return this.targetDamage;
    }

    public double getSelfDamage() {
        return this.selfDamage;
    }
}
