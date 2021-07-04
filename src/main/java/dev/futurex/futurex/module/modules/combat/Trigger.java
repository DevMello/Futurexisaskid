package dev.futurex.futurex.module.modules.combat;

import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.util.player.PlayerUtil;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Trigger extends Module {
    public Trigger() {
        super("Trigger", Category.COMBAT, "Attacks entities in your crosshair");
    }

    public static Checkbox cooldown = new Checkbox("Cooldown", true);
    public static Checkbox sync = new Checkbox("TPS Sync", false);
    public static Checkbox packet = new Checkbox("Packet Swing", true);
    public static Checkbox players = new Checkbox("Players", true);
    public static Checkbox animals = new Checkbox("Animals", false);
    public static Checkbox mobs = new Checkbox("Mobs", false);

    @Override
    public void setup() {
        addSetting(cooldown);
        addSetting(sync);
        addSetting(packet);
        addSetting(players);
        addSetting(animals);
        addSetting(mobs);
    }

    @Override
    public void onUpdate() {
        if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && (mc.objectMouseOver.entityHit instanceof EntityPlayer && players.getValue() && (!FriendManager.isFriend(mc.objectMouseOver.entityHit.getName()) && FriendManager.isFriendModuleEnabled())) || (mc.objectMouseOver.entityHit instanceof EntityAnimal && animals.getValue()) || (mc.objectMouseOver.entityHit instanceof EntityMob && mobs.getValue()))
            PlayerUtil.attackEntity(mc.objectMouseOver.entityHit, packet.getValue(), cooldown.getValue(), sync.getValue());
    }
}
