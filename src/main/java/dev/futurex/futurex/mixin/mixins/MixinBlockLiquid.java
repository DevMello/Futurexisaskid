package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.managers.ModuleManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {
    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void canCollideCheck(IBlockState blockState, boolean liquidLevel, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(ModuleManager.getModuleByName("LiquidInteract").isEnabled() || (liquidLevel && blockState.getValue(BlockLiquid.LEVEL) == 0));
    }
}
