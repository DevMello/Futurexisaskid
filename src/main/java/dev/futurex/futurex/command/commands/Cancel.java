package dev.futurex.futurex.command.commands;

import baritone.api.BaritoneAPI;
import dev.futurex.futurex.command.Command;
import dev.futurex.futurex.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Cancel extends Command {
    public Cancel() {
        super("cancel", "", "Cancels current baritone pathing goal");
    }

    @Override
    public void onCommand(String[] args) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
        MessageUtil.addOutput("Canceled all baritone process!");
    }
}
