package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.module.modules.client.ClientFont;
import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.util.render.FontUtil;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linustouchtips
 * @since 01/26/2021
 */

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Inject(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value="HEAD"), cancellable = true)
    public void renderStringHook(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> info) {
        if (ModuleManager.getModuleByName("Font").isEnabled() && ClientFont.override.getValue())
            info.setReturnValue(FontUtil.getString(text, x, y, color));
    }
}
