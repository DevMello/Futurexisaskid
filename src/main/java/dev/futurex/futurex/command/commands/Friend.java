package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Friend extends Command {
    public Friend() {
        super("friend", "[add/remove] [player name]", "Adds player to friends list");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            if (args[1].equalsIgnoreCase("add")) {
                if (FriendManager.isFriend(args[2]))
                    MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is already a friend!");

                else if (!FriendManager.isFriend(args[2])) {
                    FriendManager.addFriend(args[2]);
                    MessageUtil.addOutput("Added " + TextFormatting.GREEN + args[2] + TextFormatting.WHITE + " to friends list");
                }
            }

            if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("remove")) {
                if (!FriendManager.isFriend(args[2]))
                    MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is not a friend!");

                else if (FriendManager.isFriend(args[2])) {
                    FriendManager.removeFriend(args[2]);
                    MessageUtil.addOutput("Removed " + TextFormatting.RED + args[2] + TextFormatting.WHITE + " from friends list");
                }
            }
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
