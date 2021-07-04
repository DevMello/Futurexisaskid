package dev.futurex.futurex.module.modules.combat;

import dev.futurex.futurex.managers.RotationManager;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.player.rotation.Rotation;
import dev.futurex.futurex.util.player.rotation.RotationPriority;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.util.world.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class AimBot extends Module {
    public AimBot() {
        super("AimBot", Category.COMBAT, "Automatically rotates to nearby entities");
    }

    public static Mode mode = new Mode("Rotate","Legit", "Packet", "None");
    public static Slider range = new Slider("Range", 0.0D, 8.0D, 20.0D, 0);
    public static Checkbox onlyBow = new Checkbox("Bow Only", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(onlyBow);
    }

    EntityPlayer aimTarget = null;
    Rotation aimbotRotation = null;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!(InventoryUtil.getHeldItem(Items.BOW)) && !mc.player.isHandActive() && !(mc.player.getItemInUseMaxCount() >= 3) && onlyBow.getValue())
            return;

        aimTarget = WorldUtil.getClosestPlayer(range.getValue());

        if (aimTarget != null && (!FriendManager.isFriend(aimTarget.getName()) && FriendManager.isFriendModuleEnabled())) {
            switch (mode.getValue()) {
                case 0:
                    aimbotRotation = new Rotation(RotationUtil.getAngles(aimTarget)[0], RotationUtil.getAngles(aimTarget)[1], Rotation.RotationMode.Legit, RotationPriority.Normal);
                    break;
                case 1:
                    aimbotRotation = new Rotation(RotationUtil.getAngles(aimTarget)[0], RotationUtil.getAngles(aimTarget)[1], Rotation.RotationMode.Packet, RotationPriority.Normal);
                    break;
            }

            RotationManager.rotationQueue.add(aimbotRotation);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
