package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.module.modules.client.Colors;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.managers.ModuleManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/23/2020
 */

public class TabGUI extends HUDElement {
    public TabGUI() {
        super("TabGUI", 2, 6, Category.MISC, AnchorPoint.None);

        MinecraftForge.EVENT_BUS.register(this);

        removeBackground();
    }

    int currentHovered = 0;
    int currentModuleHovered = 0;
    boolean hoveringModules = false;

    Module.Category[] categories = new Module.Category[] {
            Module.Category.CLIENT, Module.Category.COMBAT, Module.Category.MOVEMENT, Module.Category.MISC, Module.Category.PLAYER, Module.Category.RENDER, Module.Category.BOT
    };

    @Override
    public void renderElement() {
        int count = 0;
        int moduleCount = 0;

        GuiScreen.drawRect(this.x, this.y, this.x + 85, this.y + 111, new Color(0, 0, 0, 90).getRGB());

        resetHover();

        if (hoveringModules) {
            GuiScreen.drawRect(this.x + 87, this.y + (moduleCount * 15), this.x + 167, this.y + 2 + (count * 15) + (ModuleManager.getModulesInCategory(categories[currentHovered]).size() * 15), new Color(0, 0, 0, 90).getRGB());
            GuiScreen.drawRect(this.x + 89, this.y + 2 + (currentModuleHovered * 15), this.x + 163, this.y + 2 + ((currentModuleHovered + 1) * 15), ThemeColor.COLOR);
        }

        GuiScreen.drawRect(this.x + 2, this.y + 2 + (currentHovered * 15), this.x + 83, this.y + 2 + ((currentHovered + 1) * 15), new Color(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), 110).getRGB());

        if (hoveringModules)
            for (Module module : ModuleManager.getModulesInCategory(categories[currentHovered])) {
                FontUtil.drawString(module.getName(), this.x + 93, this.y + 6 + (moduleCount * 15), module.isEnabled() ? ThemeColor.BRIGHT : -1);

                moduleCount++;
            }

        for (Module.Category category : categories) {
            FontUtil.drawString(category.getName(), this.x + 5, this.y + 6 + (count * 15), -1);

            count++;
        }

        width = 87;
        height = 107;
    }

    public void resetHover() {
        if (currentHovered < 0)
            currentHovered = 0;

        if (currentHovered > 6)
            currentHovered = 6;

        if (currentModuleHovered < 0)
            currentModuleHovered = 0;

        if (currentModuleHovered > ModuleManager.getModulesInCategory(categories[currentHovered]).size() - 1)
            currentModuleHovered = ModuleManager.getModulesInCategory(categories[currentHovered]).size() - 1;
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            if (hoveringModules)
                currentModuleHovered--;
            else
                currentHovered--;

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            if (hoveringModules)
                currentModuleHovered++;
            else
                currentHovered++;

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !hoveringModules)
            hoveringModules = true;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            hoveringModules = false;
            currentModuleHovered = 0;
        }

        try {
            if ((Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) && hoveringModules)
                ModuleManager.getModulesInCategory(categories[currentHovered]).get(currentModuleHovered).toggle();
        } catch (Exception e) {

        }
    }
}