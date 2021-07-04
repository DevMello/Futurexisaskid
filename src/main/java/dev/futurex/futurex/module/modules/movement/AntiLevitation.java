package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import net.minecraft.potion.Potion;

import java.util.Objects;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class AntiLevitation extends Module {
    public AntiLevitation() {
        super("AntiLevitation", Category.MOVEMENT, "Removes the levitation potion effect");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
            NotificationManager.addNotification(new Notification("Removing levitation effect!", Notification.Type.Info));
            MixinInterface.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
        }
    }
}
