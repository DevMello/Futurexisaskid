package dev.futurex.futurex.module.modules.client;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class Social extends Module {
    public Social() {
        super("Social", Category.CLIENT, "Allows social system to function");
        this.enable();
        this.setDrawn(false);
    }

    public static Checkbox friends = new Checkbox("Friends", true);
    public static Checkbox enemies = new Checkbox("Enemies", true);

    @Override
    public void setup() {
        addSetting(friends);
        addSetting(enemies);
    }
}
