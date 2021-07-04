package dev.futurex.futurex.util.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.mixin.MixinInterface;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;

/**
 * @author olliem5
 * @since 12/17/2020
 */

public class DiscordPresence implements MixinInterface {
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    public static DiscordRichPresence rp = new DiscordRichPresence();
    private static String details;
    private static String state;

    public static void startup() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("842487217473323088", handlers, true, "");
        rp.startTimestamp = System.currentTimeMillis() / 1000L;
        rp.largeImageKey = "futurex";
        rp.largeImageText = FutureX.NAME + " " + FutureX.VERSION;
        rpc.Discord_UpdatePresence(rp);

        new Thread(() -> {
           while (!Thread.currentThread().isInterrupted()) {
                try {
                    details = "Main Menu";
                    state = mc.player.getName();

                    if (mc.isIntegratedServerRunning())
                        details = "Singleplayer | " + mc.getIntegratedServer().getWorldName();

                    else if (mc.currentScreen instanceof GuiMultiplayer)
                        details = "Multiplayer Menu";

                    else if (mc.getCurrentServerData() != null)
                        details = mc.getCurrentServerData().serverIP.toLowerCase();

                    else if (mc.currentScreen instanceof GuiWorldSelection)
                        details = "Singleplayer Menu";

                    rp.details = details;
                    rp.state = state;
                    rpc.Discord_UpdatePresence(rp);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }, "RPC-Callback-Handler").start();
    }

    public static void shutdown() {
        rpc.Discord_Shutdown();
    }
}
