package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.module.modules.render.NoRender;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void renderBossHealth(CallbackInfo info) {
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.bossBar.getValue())
            info.cancel();
    }
}
