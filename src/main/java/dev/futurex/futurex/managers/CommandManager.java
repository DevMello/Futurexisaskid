package dev.futurex.futurex.managers;

import com.google.common.collect.Lists;
import dev.futurex.futurex.command.Command;
import dev.futurex.futurex.command.commands.*;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class CommandManager {
    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    static List<Command> commands = Lists.newArrayList(
            new Toggle(),
            new Dupe(),
            new Friend(),
            new HClip(),
            new VClip(),
            new GoTo(),
            new Panic(),
            new Config(),
            new Cancel(),
            new Peek(),
            new Drawn(),
            new Client(),
            new Enemy(),
            new Commands(),
            new Modules(),
            new Spawn()
    );

    public static List<Command> getCommands() {
        return commands;
    }

    public static Command getCommandByUsage(String name) {
        return commands.stream().filter(command -> command.getUsage().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Command getCommandByClass(Class<?> clazz) {
        return commands.stream().filter(command -> command.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public static List<Command> predictCommands(String command) {
        return commands.stream().filter(predictCommand -> predictCommand.getUsage().startsWith(command)).collect(Collectors.toList());
    }
}
