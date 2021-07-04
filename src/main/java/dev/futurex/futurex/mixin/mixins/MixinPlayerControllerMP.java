package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.module.modules.player.Reach;
import dev.futurex.futurex.module.modules.player.SpeedMine;
import dev.futurex.futurex.module.modules.player.Swing;
import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.mixin.MixinInterface;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(value = PlayerControllerMP.class/*, priority = 999*/)
public class MixinPlayerControllerMP implements MixinInterface {

    @Inject(method = "resetBlockRemoving", at = @At(value = "HEAD"), cancellable = true)
    private void resetBlock(CallbackInfo ci) {
        if (ModuleManager.getModuleByName("Swing").isEnabled() && Swing.noReset.getValue() && mc.world != null)
            ci.cancel();
    }

    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir){
        if (ModuleManager.getModuleByName("SpeedMine").isEnabled() && SpeedMine.mode.getValue() == 0)
            cir.setReturnValue(false);
    }

    @Inject(method = "getBlockReachDistance", at = @At("RETURN"), cancellable = true)
    private void getReachDistanceHook(final CallbackInfoReturnable<Float> distance) {
        if (ModuleManager.getModuleByName("Reach").isEnabled())
            distance.setReturnValue((float) Reach.distance.getValue());
    }
}
