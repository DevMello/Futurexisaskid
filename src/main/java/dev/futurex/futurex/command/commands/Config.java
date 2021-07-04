package dev.futurex.futurex.command.commands;

import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.command.Command;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Config extends Command {
    public Config() {
        super("config", "", "Opens the config folder");
    }

    @Override
    public void onCommand(String[] args) {
        try {
            Desktop.getDesktop().open(new File("futurex"));
            MessageUtil.addOutput("Opened config folder!");
        } catch (IOException ignored) {

        }
    }
}
