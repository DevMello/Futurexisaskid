package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;

public class Modules extends Command {
    public Modules() {
        super("modules", "", "Lists all modules");
    }

    @Override
    public void onCommand(String[] args) {
        for (Module module : ModuleManager.getModules()) {
            MessageUtil.addOutput(module.getName() + " - " + module.getDescription());
        }
    }
}
