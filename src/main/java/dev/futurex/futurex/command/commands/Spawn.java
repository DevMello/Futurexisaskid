package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.util.world.WorldUtil;
import dev.futurex.futurex.command.Command;

public class Spawn extends Command {
    public Spawn() {
        super("spawn", "[name]", "Spawns a fakeplayer");
    }

    int offset = 6641;

    @Override
    public void onCommand(String[] args) {
        offset++;

        if (args.length >= 1) {
            WorldUtil.createFakePlayer(args[1], true, true, true, false, offset);
            MessageUtil.addOutput("Spawned fakeplayer with name " + args[1] + "!");
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
