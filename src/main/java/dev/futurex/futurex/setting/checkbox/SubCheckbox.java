package dev.futurex.futurex.setting.checkbox;

import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.Setting;
import dev.futurex.futurex.setting.SubSetting;
import dev.futurex.futurex.setting.keybind.Keybind;

/**
 * @author bon
 * @since 11/16/20
 */

public class SubCheckbox extends SubSetting {

	private final Setting parent;
	private final String name;
	private boolean checked;

	public SubCheckbox(Setting parent, String name, boolean checked) {
		this.parent = parent;
		this.name = name;
		this.checked = checked;
		
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
	
	public boolean getValue() {
		return this.checked;
	}

	public void setChecked(boolean newValue) {
		this.checked = newValue;
	}
	
	public void toggleValue() {
		this.checked = !this.checked;
	}
	
	public Setting getParent() {
		return this.parent;
	}
}
