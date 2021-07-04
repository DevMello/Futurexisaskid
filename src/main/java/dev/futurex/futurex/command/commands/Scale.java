package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Scale extends Command {

    public Scale() {
        super("scale", "[scale]", "Allows you to change gui scale");
    }

    public static float scale = 1.0f;

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            scale = Float.parseFloat(args[1]);
            MessageUtil.addOutput("Set GUI scale to " + args[1] + "!");
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
