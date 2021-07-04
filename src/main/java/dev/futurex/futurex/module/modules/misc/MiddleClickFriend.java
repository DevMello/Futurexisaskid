package dev.futurex.futurex.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.util.client.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

/**
 * @author linustouchtips & olliem5
 * @since 11/30/2020
 */

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", Category.MISC, "Adds players to your friends list when you middle click them");
    }

    boolean hasClicked = false;

    public void onUpdate() {
        if (nullCheck())
            return;

        if (!Mouse.isButtonDown(2)) {
            hasClicked = false;
            return;
        }

        if (!hasClicked) {
            hasClicked = true;

            final RayTraceResult result = mc.objectMouseOver;

            if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY || !(result.entityHit instanceof EntityPlayer))
                return;

            if (FriendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                FriendManager.removeFriend(mc.objectMouseOver.entityHit.getName());
                MessageUtil.sendClientMessage(ChatFormatting.RED + "Removed " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " from friends list");
            }

            else {
                FriendManager.addFriend(mc.objectMouseOver.entityHit.getName());
                MessageUtil.sendClientMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " to friends list");
            }
        }
    }
}
