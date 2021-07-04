package dev.futurex.futurex.module.modules.client;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 01/03/2021
 */

public class ClientFont extends Module {
    public ClientFont() {
        super("Font", Category.CLIENT, "Allows you to customize the client font");
        this.setDrawn(false);
    }

    public static Mode family = new Mode("Family", "Lato", "Ubuntu", "Verdana", "Comfortaa", "Comic Sans");
    public static Slider scale = new Slider("Scale", 0.0D, 40.0D, 60.0D, 1);
    public static Checkbox shadow = new Checkbox("Shadow", true);
    public static Checkbox override = new Checkbox("Override Vanilla", true);

    @Override
    public void setup() {
        addSetting(family);
        addSetting(scale);
        addSetting(shadow);
        addSetting(override);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        scale.setValue(scale.getValue());
    }
}
