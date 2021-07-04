package dev.futurex.futurex.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.modules.misc.EnableMessage;
import dev.futurex.futurex.setting.Setting;
import dev.futurex.futurex.util.client.MessageUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bon & linustouchtips
 * @since 11/13/20
 */

public abstract class Module implements MixinInterface {

	private final String name;
	private final Category category;
	private final String description;
	private final KeyBinding key;

	private boolean enabled;
	private boolean opened;
	private boolean drawn;
	public boolean isKeyDown = false;
	private boolean isBinding;
	public float remainingAnimation = 0.0f;

	public List<Setting> settingsList = new ArrayList<>();

	public Module(String name, Category category, @Nullable String description) {
		this.name = name;
		this.category = category;
		this.description = description;
		this.enabled = false;
		this.opened = false;
		this.drawn = true;

		this.key = new KeyBinding(name, Keyboard.KEY_NONE, FutureX.NAME);
		ClientRegistry.registerKeyBinding(this.key);

		this.setup();
	}

	public void setup() {

	}

	public void addSetting(Setting s) {
		settingsList.add(s);
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void onEnable() {
		remainingAnimation = 0.0f;

		if (ModuleManager.getModuleByClass(EnableMessage.class).isEnabled() && !this.name.equalsIgnoreCase("ClickGUI"))
			MessageUtil.sendClientMessage(this.name + ChatFormatting.GREEN + " ENABLED");

		MinecraftForge.EVENT_BUS.register(this);
	}

	public void onDisable() {
		remainingAnimation = 0.0f;
		mc.timer.tickLength = 50;

		for (Entity entity : mc.world.loadedEntityList) {
			if (entity.isGlowing())
				entity.setGlowing(false);
		}

		if (ModuleManager.getModuleByClass(EnableMessage.class).isEnabled() && !this.name.equalsIgnoreCase("ClickGUI"))
			MessageUtil.sendClientMessage(this.name + ChatFormatting.RED + " DISABLED");

		MinecraftForge.EVENT_BUS.unregister(this);
	}

	public void onToggle() {
		remainingAnimation = 0.0f;
	}

	public void onUpdate() {

	}

	public void onFastUpdate() {

	}

	public void onServerUpdate() {

	}

	public void onValueChange() {

	}

	public void toggle() {
		this.enabled = !this.enabled;
		try {
			if (this.isEnabled())
				this.onEnable();

			else
				this.onDisable();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void enable() {
		if (!this.isEnabled()) {
			this.enabled = true;
			try {
				this.onEnable();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void disable() {
		if (this.isEnabled()) {
			this.enabled = false;
			try {
				this.onDisable();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean nullCheck() {
		return (mc.player == null || mc.world == null);
	}

	public String getName() {
		return this.name;
	}

	public Category getCategory() {
		return this.category;
	}

	public String getDescription() {
		return this.description;
	}

	public KeyBinding getKeybind() {
		return this.key;
	}

	public String getHUDData() {
		return "";
	}

	public boolean hasSettings() {
		return this.settingsList.size() > 0;
	}

	public List<Setting> getSettings(){
		return this.settingsList;
	}

	public void toggleState() {
		this.opened = !this.opened;
	}

	public boolean isOpened() {
		return this.opened;
	}

	public boolean isBinding() {
		return this.isBinding;
	}

	public boolean isDrawn() {
		return this.drawn;
	}

	public void setBinding(boolean b) {
		this.isBinding = b;
	}

	public void setDrawn(boolean in) {
		this.drawn = in;
	}

	public enum Category {
		CLIENT("Client", new Color(234, 71, 71)),
		COMBAT("Combat", new Color(56, 103, 224)),
		PLAYER("Player", new Color(37, 205, 84)),
		MISC("Miscellaneous", new Color(122, 61, 217)),
		MOVEMENT("Movement", new Color(217, 49, 103)),
		RENDER("Render", new Color(231, 164, 73)),
		BOT("Bot", new Color(208, 68, 195));

		String name;
		Color color;

		Category(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String getName() {
			return this.name;
		}

		public Color getColor() {
			return this.color;
		}
	}
}