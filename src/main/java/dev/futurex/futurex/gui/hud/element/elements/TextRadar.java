package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.util.client.MathUtil;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.combat.EnemyUtil;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.util.world.WorldUtil;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class TextRadar extends HUDElement {
    public TextRadar() {
        super("TextRadar", 2, 80, Category.MISC, AnchorPoint.TopLeft);
    }

    int count;

    @Override
    public void renderElement() {
        count = 0;
        int screenWidth = new ScaledResolution(mc).getScaledWidth();
        WorldUtil.getNearbyPlayers(20).forEach(entityPlayer -> {
            int screenWidthScaled = new ScaledResolution(mc).getScaledWidth();
            float modWidth = FontUtil.getStringWidth(getHealthText(EnemyUtil.getHealth(entityPlayer)) + String.valueOf(EnemyUtil.getHealth(entityPlayer)) + (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : TextFormatting.RESET) + entityPlayer.getName() + TextFormatting.WHITE + mc.player.getDistance(entityPlayer));
            String modText = getHealthText(EnemyUtil.getHealth(entityPlayer)) + String.valueOf(MathUtil.roundAvoid(EnemyUtil.getHealth(entityPlayer), 1)) +  " " + (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : TextFormatting.RESET) + entityPlayer.getName() + TextFormatting.WHITE + " " + MathUtil.roundAvoid(mc.player.getDistance(entityPlayer), 1);

            if (this.x < (screenWidthScaled / 2))
                FontUtil.drawString(modText, this.x - 2, this.y + (10 * count), ThemeColor.BRIGHT);
            else
                FontUtil.drawString(modText, this.x, this.y + (10 * count), ThemeColor.BRIGHT);

            count++;

            if (this.x < (screenWidth / 2))
                width = (int) (modWidth + 5);
            else
                width = (int) (modWidth - 5);
        });

        height = ((mc.fontRenderer.FONT_HEIGHT + 1) * count);
    }

    public TextFormatting getHealthText(float health) {
        if (health <= 4)
            return TextFormatting.RED;
        else if (health <= 8)
            return TextFormatting.GOLD;
        else if (health <= 12)
            return TextFormatting.YELLOW;
        else if (health <= 16)
            return TextFormatting.DARK_GREEN;
        else
            return TextFormatting.GREEN;
    }
}
