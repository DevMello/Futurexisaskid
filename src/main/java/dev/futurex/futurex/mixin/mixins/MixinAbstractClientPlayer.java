package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.module.modules.render.ESP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow
    @Nullable
    public abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir){
        if (ModuleManager.getModuleByName("Capes").isEnabled() && FutureX.capeManager.hasCape(getPlayerInfo().getGameProfile().getId()))
            cir.setReturnValue(new ResourceLocation("futurex:futurex_cape.png"));
    }

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        if (ModuleManager.getModuleByName("ESP").isEnabled() && ESP.mode.getValue() == 6)
            cir.setReturnValue(new ResourceLocation("futurex:texturechams.png"));
    }
}
