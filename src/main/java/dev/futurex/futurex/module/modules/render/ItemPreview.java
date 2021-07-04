package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.module.modules.client.Colors;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.minecraft.client.renderer.ItemRenderer.RES_MAP_BACKGROUND;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class ItemPreview extends Module {
    public ItemPreview() {
        super("ItemPreview", Category.RENDER, "Renders better tooltips");
    }

    public static dev.futurex.futurex.setting.checkbox.Checkbox shulkers = new dev.futurex.futurex.setting.checkbox.Checkbox("Shulkers", true);
    public static dev.futurex.futurex.setting.checkbox.Checkbox maps = new Checkbox("Maps", true);

    @Override
    public void setup() {
        addSetting(shulkers);
        addSetting(maps);
    }

    public static void tooltipShulker(ItemStack itemStack, int x, int y, CallbackInfo callbackInfo) {
        if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("BlockEntityTag", 10)) {
            if (itemStack.getTagCompound().getCompoundTag("BlockEntityTag").hasKey("Items", 9)) {
                callbackInfo.cancel();

                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(itemStack.getTagCompound().getCompoundTag("BlockEntityTag"), nonnulllist);
                GlStateManager.enableBlend();
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();

                int x1 = x + 12;
                int y1 = y - 12;
                int height = 57;
                int width = (int) Math.max(144, FontUtil.getStringWidth(itemStack.getDisplayName())+3);

                MixinInterface.mc.getRenderItem().zLevel = 300.0F;
                GuiScreen.drawRect(x1 - 4, y1 - 9, x1 + width + 1, y1 + 5, new Color(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), 164).getRGB());
                GuiScreen.drawRect(x1 - 4, y1 + 5, x1 + width + 1, y1 + height + 3, new Color(0, 0, 0, 144).getRGB());
                FontUtil.drawString(itemStack.getDisplayName(), x1 - 2, y1 - 6, new Color(255, 255, 255).getRGB());

                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                RenderHelper.enableGUIStandardItemLighting();

                for (int i = 0; i < nonnulllist.size(); i++) {
                    int iX = x + (i % 9) * 16 + 11;
                    int iY = y + (i / 9) * 16 - 11 + 8;
                    ItemStack stack = nonnulllist.get(i);

                    MixinInterface.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, iX, iY);
                    MixinInterface.mc.getRenderItem().renderItemOverlayIntoGUI(MixinInterface.mc.fontRenderer, stack, iX, iY, null);
                }

                RenderHelper.disableStandardItemLighting();
                MixinInterface.mc.getRenderItem().zLevel = 0.0F;

                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableRescaleNormal();
            }
        }
    }

    public static void tooltipMap(ItemStack itemStack, int x, int y, CallbackInfo info) {
        if (!itemStack.isEmpty && itemStack.getItem() instanceof ItemMap) {
            MapData mapData = ((ItemMap) itemStack.getItem()).getMapData(itemStack, MixinInterface.mc.world);

            if (mapData != null) {
                info.cancel();

                GlStateManager.pushMatrix();
                GlStateManager.color(1f, 1f, 1f);
                GlStateManager.translate(x + 6, y + 6, 0.0);
                GlStateManager.scale(1f, 1f, 0f);
                MixinInterface.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
                RenderHelper.disableStandardItemLighting();
                GL11.glDepthRange(0, 0.01);
                RenderUtil.bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                RenderUtil.bufferbuilder.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
                RenderUtil.bufferbuilder.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
                RenderUtil.bufferbuilder.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
                RenderUtil.bufferbuilder.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
                RenderUtil.tessellator.draw();
                GL11.glDepthRange(0, 1.0);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.disableDepth();
                GL11.glDepthRange(0, 0.01);
                MixinInterface.mc.entityRenderer.getMapItemRenderer().renderMap(mapData, false);
                GL11.glDepthRange(0, 1.0);
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }
        }
    }
}