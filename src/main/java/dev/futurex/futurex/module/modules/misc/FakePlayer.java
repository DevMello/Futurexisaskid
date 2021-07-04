package dev.futurex.futurex.module.modules.misc;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.util.client.MessageUtil;
import dev.futurex.futurex.util.world.WorldUtil;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.MISC, "Creates a fake motionless player");
    }

    public static Mode mode = new Mode("Mode", "Single", "Multi");
    public static Mode name = new Mode("Name", "SirNotMeme", "popbob", "Fit", "xcc2", "S8N", "Joe");
    public static Checkbox inventory = new Checkbox("Copy Inventory", true);
    public static Checkbox angles = new Checkbox("Copy Angles", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(name);
        addSetting(inventory);
        addSetting(angles);
    }

    @Override
    public void onUpdate() {
        if (mc.world == null)
            this.disable();
    }

    public void onEnable() {
        if (nullCheck())
            return;

        if (mode.getValue() == 0) {
            WorldUtil.createFakePlayer(name.getMode(name.getValue()), inventory.getValue(), angles.getValue(), true, false, -6640);
        }
        
        MessageUtil.sendClientMessage("Spawning fake player!");
    }

    @Override
    public void onDisable() {
        mc.world.removeEntityFromWorld(-6640);
    }
}