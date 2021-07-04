package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/25/2020
 */

public class Drawn extends Command {
    public Drawn() {
        super("drawn", "[module name]", "Hides or draws a module on the arraylist");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            for (Module module : ModuleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(args[1]) && !module.isDrawn()) {
                    MessageUtil.addOutput(TextFormatting.AQUA + module.getName() + TextFormatting.WHITE + " is now " + TextFormatting.GREEN + "DRAWN");
                    module.setDrawn(true);
                }

                else if (module.getName().equalsIgnoreCase(args[1]) && module.isDrawn()) {
                    MessageUtil.addOutput(TextFormatting.AQUA + module.getName() + TextFormatting.WHITE + " is now " + TextFormatting.RED + "HIDDEN");
                    module.setDrawn(false);
                }
            }
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
