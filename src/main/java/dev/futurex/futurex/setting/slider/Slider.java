package dev.futurex.futurex.setting.slider;

import dev.futurex.futurex.setting.Setting;
import dev.futurex.futurex.setting.SubSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bon
 * @since 11/12/20
 */

public class Slider extends Setting {
	
	private final String name;
	private final double min;
	private double value;
	private final double max;
	private final int scale;
	private boolean opened;
	
	private final List<SubSetting> subs = new ArrayList<>();
	
	public Slider(String name, double min, double value, double max, int scale) {
		this.name = name;
		this.min = min;
		this.value = scale == 0 ? (int) value : value;
		this.max = max;
		this.scale = scale;
		this.opened = false;
	}
	
	public List<SubSetting> getSubSettings(){
		return this.subs;
	}
	
	public boolean hasSubSettings() {
		return this.subs.size() > 0;
	}
	
	public void addSub(SubSetting s) {
		this.subs.add(s);
	}
	
	public int getRoundingScale() {
		return this.scale;
	}
	
	public void toggleState() {
		this.opened = !this.opened;
	}
	
	public boolean isOpened() {
		return this.opened;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public double getMaxValue() {
		return this.max;
	}
	
	public double getMinValue() {
		return this.min;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
}