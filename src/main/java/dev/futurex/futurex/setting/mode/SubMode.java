package dev.futurex.futurex.setting.mode;

import dev.futurex.futurex.setting.Setting;
import dev.futurex.futurex.setting.SubSetting;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.keybind.Keybind;
import dev.futurex.futurex.setting.slider.Slider;

/**
 * @author bon
 * @since 11/16/20
 */

public class SubMode extends SubSetting {
	
	private final Setting parent;
	private final String name;
	private final String[] modes;
	private int mode;

	public SubMode(Setting parent, String name, String... modes) {
		this.parent = parent;
		this.name = name;
		this.modes = modes;
		
		if (parent instanceof Checkbox) {
			Checkbox p = (Checkbox) parent;
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
	
	public String getName() {
		return this.name;
	}
	
	public Setting getParent() {
		return this.parent;
	}
	
	public String getMode(int modeIndex) {
		return this.modes[modeIndex];
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public int getValue() {
		return this.mode;
	}
	
	public int nextMode() {
		return this.mode + 1 >= this.modes.length ? 0 : this.mode + 1;
	}
}
