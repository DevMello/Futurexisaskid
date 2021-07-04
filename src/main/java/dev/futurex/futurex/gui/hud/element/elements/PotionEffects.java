package dev.futurex.futurex.gui.hud.element.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.managers.TickManager;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class PotionEffects extends HUDElement {
    public PotionEffects() {
        super("PotionEffects", 400, 400, Category.MISC, AnchorPoint.BottomRight);
    }

    int count;

    @Override
    public void renderElement() {
        DecimalFormat minuteFormatter = new DecimalFormat("0");
        DecimalFormat secondsFormatter = new DecimalFormat("00");

        count = 0;
        try {
            mc.player.getActivePotionEffects().forEach(effect -> {
                int screenWidth = new ScaledResolution(mc).getScaledWidth();

                String name = I18n.format(effect.getPotion().getName());

                int amplifier = effect.getAmplifier() + 1;
                double duration = effect.getDuration() / TickManager.TPS[0];
                double timeSeconds = duration % 60;
                double timeMinutes = duration / 60;
                double timeMinutesRounded = timeMinutes % 60;
                
                String minutes = minuteFormatter.format(timeMinutesRounded);
                String seconds = secondsFormatter.format(timeSeconds);
                String potionString = name + " " + amplifier + ChatFormatting.WHITE + " " + minutes + ":" + seconds;

                if (this.x < (screenWidth / 2))
                    FontUtil.drawString(potionString, this.x, this.y + (count * 10), effect.getPotion().getLiquidColor());
                else
                    FontUtil.drawString(potionString, this.x - (int) FontUtil.getStringWidth(potionString), this.y + (count * 10), effect.getPotion().getLiquidColor());

                count++;

                if (this.x < (screenWidth / 2))
                    width = FutureX.fontManager.getCustomFont().getStringWidth(potionString);
                else
                    width = -FutureX.fontManager.getCustomFont().getStringWidth(potionString);

                height = (mc.fontRenderer.FONT_HEIGHT + 3) * count;
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
