package dev.futurex.futurex.module.modules.render.esp.modes;

import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.util.world.EntityUtil;
import dev.futurex.futurex.module.modules.render.ESP;
import dev.futurex.futurex.module.modules.render.esp.ESPMode;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class Glow extends ESPMode {
    public Glow() {
        isRender = true;
    }

    @Override
    public void drawESP() {
        for (Entity entitylivingbaseIn : mc.world.loadedEntityList) {
            if ((entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue())))
                entitylivingbaseIn.setGlowing(true);

            if (ESP.mode.getValue() != 1)
                entitylivingbaseIn.setGlowing(false);

            if (!ModuleManager.getModuleByName("ESP").isEnabled())
                entitylivingbaseIn.setGlowing(false);
        }
    }
}
