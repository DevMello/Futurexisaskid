package dev.futurex.futurex.module.modules.client;

import dev.futurex.futurex.module.Module;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Capes extends Module {
    public Capes() {
        super("Capes", Category.CLIENT, "Adds a custom cape to the player");
        this.setDrawn(false);
    }
}