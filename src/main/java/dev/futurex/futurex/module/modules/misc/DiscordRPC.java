package dev.futurex.futurex.module.modules.misc;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.client.DiscordPresence;
import dev.futurex.futurex.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("DiscordRPC", Category.MISC, "Displays a custom Discord Rich Presence");
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        MessageUtil.sendClientMessage("Discord Rich Presence started!");
        DiscordPresence.startup();
    }

    @Override
    public void onDisable() {
        MessageUtil.sendClientMessage("Discord Rich Presence shutdown!");
        DiscordPresence.shutdown();
    }
}
