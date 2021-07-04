package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.managers.HUDElementManager;
import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Panic extends Command {
    public Panic () {
        super("panic",  "", "Toggles off everything enabled");
    }

    @Override
    public void onCommand(String[] args) {
        for (Module m: ModuleManager.getModules()) {
            if (m.isEnabled())
                m.disable();
        }

        MessageUtil.addOutput("All modules toggled off!");

        for (HUDElement hud : HUDElementManager.getComponents()) {
            if (hud.isDrawn())
                hud.toggleElement();
        }

        MessageUtil.addOutput("All HUD elements toggled off!");
    }
}
