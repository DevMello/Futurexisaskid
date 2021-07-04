package dev.futurex.futurex.module.modules.misc;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.keybind.Keybind;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.world.WorldUtil;
import net.minecraft.init.Items;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class AutoDisconnect extends Module {
    public AutoDisconnect() {
        super("AutoDisconnect", Category.MISC, "Automatically logs you out when you're low on health");
    }

    public static Slider health = new Slider("Health", 0.0D, 7.0D, 36.0D, 0);
    public static Checkbox noTotems = new Checkbox("No Totems", false);
    public static Checkbox visualRange = new Checkbox("Player in Range", false);
    public static Keybind disconnectKey = new Keybind("Disconnect Key", -2);

    @Override
    public void setup() {
        addSetting(health);
        addSetting(noTotems);
        addSetting(visualRange);
        addSetting(disconnectKey);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.getHealth() <= health.getValue())
            WorldUtil.disconnectFromWorld(this);

        if (InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING) == 0 && noTotems.getValue())
            WorldUtil.disconnectFromWorld(this);

        if (WorldUtil.getNearbyPlayers(20).size() > 0 && visualRange.getValue())
            WorldUtil.disconnectFromWorld(this);

        if (Keyboard.isKeyDown(disconnectKey.getKey()))
            WorldUtil.disconnectFromWorld(this);
    }

    @Override
    public String getHUDData() {
        return String.valueOf(health.getValue());
    }
}
