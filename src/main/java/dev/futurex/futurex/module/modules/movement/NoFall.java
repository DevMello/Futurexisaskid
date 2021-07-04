package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.util.player.InventoryUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Category.MOVEMENT, "Prevents fall damage");
    }

    public static Mode mode = new Mode("Mode", "NCP", "AAC", "Bucket");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.fallDistance > 4.0f) {
            switch (mode.getValue()) {
                case 0:
                    MixinInterface.mc.player.fallDistance = 0;
                    MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX +420420, MixinInterface.mc.player.posY, MixinInterface.mc.player.posZ, false));
                    MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 1, MixinInterface.mc.player.posZ, true));
                    break;
                case 1:
                    MixinInterface.mc.player.connection.sendPacket(new CPacketPlayer.Position(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 1, MixinInterface.mc.player.posZ, true));
                    break;
                case 2:
                    InventoryUtil.switchToSlot(Items.WATER_BUCKET);

                    BlockPos bucketPos = null;
                    for (int i = 0; i < MixinInterface.mc.player.posY - 5; i++) {
                        BlockPos belowPos = new BlockPos(MixinInterface.mc.player.posX, i, MixinInterface.mc.player.posZ);

                        if (MixinInterface.mc.world.getBlockState(belowPos).getBlock() instanceof BlockLiquid || MixinInterface.mc.world.isAirBlock(belowPos))
                            continue;

                        bucketPos = belowPos;
                    }

                    MixinInterface.mc.player.rotationPitch = 90.0f;
                    MixinInterface.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bucketPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
