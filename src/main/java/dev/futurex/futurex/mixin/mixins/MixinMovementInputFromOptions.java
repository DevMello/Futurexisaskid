package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.modules.movement.NoSlow;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author linustouchtips
 * @since 01/26/2021
 */

@Mixin(value = MovementInputFromOptions.class, priority = 10001)
public class MixinMovementInputFromOptions extends MovementInput implements MixinInterface {

    @Redirect(method = "updatePlayerMoveState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        if (ModuleManager.getModuleByName("NoSlow").isEnabled() && NoSlow.inventoryMove.getValue() && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && mc.player != null)
            return Keyboard.isKeyDown(keyBinding.getKeyCode());

        return keyBinding.isKeyDown();
    }
}
