package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.gui.main.GUIScreen;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.mode.Mode;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author novola & olliem5 & linustouchtips
 * @since 12/03/2020
 */

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Category.MOVEMENT, "Allows you to move at normal speeds when using an item");
    }

    public static Mode mode = new Mode("Mode", "Normal", "2b2t");
    public static Checkbox inventoryMove = new Checkbox("Inventory Move", true);
    public static SubCheckbox guiMove = new SubCheckbox(inventoryMove, "Custom GUI's", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(inventoryMove);
    }

    boolean sneaking;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == 1) {
            Item item = MixinInterface.mc.player.getActiveItemStack().getItem();
            if (sneaking && ((!MixinInterface.mc.player.isHandActive() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion) || (!(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion)))) {
                MixinInterface.mc.player.connection.sendPacket(new CPacketEntityAction(MixinInterface.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                sneaking = false;
            }
        }

        if (MixinInterface.mc.currentScreen != null && !(MixinInterface.mc.currentScreen instanceof GuiChat) && inventoryMove.getValue()) {
            if (MixinInterface.mc.currentScreen instanceof GUIScreen && !guiMove.getValue())
                return;

            if (Keyboard.isKeyDown(200))
                MixinInterface.mc.player.rotationPitch -= 5;

            if (Keyboard.isKeyDown(208))
                MixinInterface.mc.player.rotationPitch += 5;

            if (Keyboard.isKeyDown(205))
                MixinInterface.mc.player.rotationYaw += 5;

            if (Keyboard.isKeyDown(203))
                MixinInterface.mc.player.rotationYaw -= 5;

            if (MixinInterface.mc.player.rotationPitch > 90)
                MixinInterface.mc.player.rotationPitch = 90;

            if (MixinInterface.mc.player.rotationPitch < -90)
                MixinInterface.mc.player.rotationPitch = -90;
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        if (mode.getValue() == 1) {
            if (!sneaking) {
                MixinInterface.mc.player.connection.sendPacket(new CPacketEntityAction(MixinInterface.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                sneaking = true;
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        if (mode.getValue() == 0) {
            if (MixinInterface.mc.player.isHandActive() && !MixinInterface.mc.player.isRiding()) {
                event.getMovementInput().moveStrafe *= 5;
                event.getMovementInput().moveForward *= 5;
            }
        }
    }
}
