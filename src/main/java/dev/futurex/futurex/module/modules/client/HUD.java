package dev.futurex.futurex.module.modules.client;

import dev.futurex.futurex.gui.main.HUDScreen;
import dev.futurex.futurex.managers.ScreenManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author bon & linustouchtips
 * @since 12/01/202
 */

public class HUD extends Module {
	public HUD() {
		super("HUD", Category.CLIENT, "The in-game hud editor");
		this.getKeybind().setKeyCode(Keyboard.KEY_PERIOD);
	}

	public static Checkbox allowOverflow = new Checkbox("Allow Overflow", false);
	public static Checkbox colorSync = new Checkbox("Color Sync", true);

	public void setup() {
		addSetting(allowOverflow);
		addSetting(colorSync);
	}
	
	HUDScreen hudEditor = new HUDScreen();

	@Override
	public void onEnable() {
		if (nullCheck())
			return;

		super.onEnable();
		ScreenManager.setScreen(hudEditor);
	}
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Text event) {
		// boost = 0;
	}
}
	

