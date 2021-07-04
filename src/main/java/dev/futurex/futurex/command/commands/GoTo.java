package dev.futurex.futurex.command.commands;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class GoTo extends Command {
    public GoTo() {
        super("goto", "[x] [y] [z]", "Sets a baritone xyz goal path");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(Integer.parseInt(args[1]), Integer.parseInt(args[3])));
            MessageUtil.addOutput("Started pathing to " + args[1] + ", " + args[2] + ", " + args[3] + "!");
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
