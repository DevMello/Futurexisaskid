package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.managers.CommandManager;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;

public class Commands extends Command {
    public Commands() {
        super("commands", "", "Lists all available commands");
    }

    @Override
    public void onCommand(String[] args) {
        for (Command command : CommandManager.getCommands()) {
            MessageUtil.addOutput(command.getUsage() + " - " + command.getDescription());
        }
    }
}
