package dev.futurex.futurex.setting.color;

import dev.futurex.futurex.setting.SubSetting;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.keybind.Keybind;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.Setting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 01/03/2021
 */

public class ColorPicker extends SubSetting {

    private final String name;
    private final Setting parent;
    private Color color;
    private boolean opened;

    public ColorPicker(Setting parent, String name, Color color) {
        this.name = name;
        this.parent = parent;
        this.color = color;
        this.opened = false;

        if (parent instanceof dev.futurex.futurex.setting.checkbox.Checkbox) {
            dev.futurex.futurex.setting.checkbox.Checkbox p = (Checkbox) parent;
            p.addSub(this);
        }

        else if (parent instanceof Mode) {
            Mode p = (Mode) parent;
            p.addSub(this);
        }

        else if (parent instanceof Slider) {
            Slider p = (Slider) parent;
            p.addSub(this);
        }

        else if (parent instanceof Keybind) {
            Keybind p = (Keybind) parent;
            p.addSub(this);
        }
    }

    public Setting getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public int getRed() {
        return this.color.getRed();
    }

    public int getGreen() {
        return this.color.getGreen();
    }

    public int getBlue() {
        return this.color.getBlue();
    }

    public int getAlpha() {
        return this.color.getAlpha();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void toggleState() {
        this.opened = !this.opened;
    }

    public boolean isOpened() {
        return this.opened;
    }
}