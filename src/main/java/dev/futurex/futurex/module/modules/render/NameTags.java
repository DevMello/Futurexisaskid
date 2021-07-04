package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.managers.GearManager;
import dev.futurex.futurex.managers.social.enemy.EnemyManager;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.client.MathUtil;
import dev.futurex.futurex.util.combat.EnemyUtil;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.world.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class NameTags extends Module {
    public NameTags() {
        super("NameTags", Category.RENDER, "Draws useful information at player's heads");
    }

    public static dev.futurex.futurex.setting.checkbox.Checkbox health = new dev.futurex.futurex.setting.checkbox.Checkbox("Health", true);
    public static dev.futurex.futurex.setting.checkbox.Checkbox ping = new dev.futurex.futurex.setting.checkbox.Checkbox("Ping", true);
    public static dev.futurex.futurex.setting.checkbox.Checkbox gamemode = new dev.futurex.futurex.setting.checkbox.Checkbox("GameMode", false);
    public static dev.futurex.futurex.setting.checkbox.Checkbox totemPops = new dev.futurex.futurex.setting.checkbox.Checkbox("Totem Pops", false);
    public static dev.futurex.futurex.setting.checkbox.Checkbox armor = new dev.futurex.futurex.setting.checkbox.Checkbox("Armor", true);
    public static dev.futurex.futurex.setting.checkbox.Checkbox durability = new dev.futurex.futurex.setting.checkbox.Checkbox("Durability", true);

    public static dev.futurex.futurex.setting.checkbox.Checkbox mainhand = new dev.futurex.futurex.setting.checkbox.Checkbox("Mainhand", true);
    public static SubCheckbox itemName = new SubCheckbox(mainhand, "Item Name", false);

    public static dev.futurex.futurex.setting.checkbox.Checkbox offhand = new dev.futurex.futurex.setting.checkbox.Checkbox("Offhand", true);
    public static dev.futurex.futurex.setting.checkbox.Checkbox enchants = new dev.futurex.futurex.setting.checkbox.Checkbox("Enchants", true);
    public static dev.futurex.futurex.setting.checkbox.Checkbox exp = new dev.futurex.futurex.setting.checkbox.Checkbox("EXP", false);

    public static dev.futurex.futurex.setting.checkbox.Checkbox onlyInViewFrustrum = new dev.futurex.futurex.setting.checkbox.Checkbox("View Frustrum", false);

    public static Slider scale = new Slider("Scale", 0.0D, 2.0D, 10.0D, 1);
    public static dev.futurex.futurex.setting.checkbox.Checkbox scaleByDistance = new dev.futurex.futurex.setting.checkbox.Checkbox("Scale By Distance", false);

    public static dev.futurex.futurex.setting.checkbox.Checkbox background = new Checkbox("Background", true);
    public static ColorPicker colorPicker = new ColorPicker(background, "Color Picker", new Color(0, 0, 0, 70));

    @Override
    public void setup() {
        addSetting(health);
        addSetting(ping);
        addSetting(gamemode);
        addSetting(totemPops);
        addSetting(armor);
        addSetting(durability);
        addSetting(mainhand);
        addSetting(offhand);
        addSetting(enchants);
        addSetting(exp);
        addSetting(onlyInViewFrustrum);
        addSetting(scale);
        addSetting(scaleByDistance);
        addSetting(background);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (nullCheck() || MixinInterface.mc.renderEngine == null || MixinInterface.mc.getRenderManager() == null || MixinInterface.mc.getRenderManager().options == null)
            return;

        List<EntityPlayer> nametagEntities = new ArrayList<>();

        MixinInterface.mc.world.playerEntities.stream().filter(entity -> entity instanceof EntityPlayer && EntityUtil.isLiving(entity) && entity != MixinInterface.mc.getRenderViewEntity()).forEach(entityPlayer -> {
            RenderUtil.camera.setPosition(MixinInterface.mc.getRenderViewEntity().posX, MixinInterface.mc.getRenderViewEntity().posY, MixinInterface.mc.getRenderViewEntity().posZ);

            if (!RenderUtil.camera.isBoundingBoxInFrustum(entityPlayer.boundingBox) && onlyInViewFrustrum.getValue())
                return;

            nametagEntities.add(entityPlayer);
        });

        nametagEntities.stream().forEach(entityPlayer -> {
            Entity viewEntity = MixinInterface.mc.getRenderViewEntity();
            Vec3d nametagPosition = EntityUtil.interpolateEntityByTicks(entityPlayer, event.getPartialTicks());

            double x = nametagPosition.x;
            double distance = nametagPosition.y + 0.65;
            double z = nametagPosition.z;

            double y = distance + (entityPlayer.isSneaking() ? 0.0 : 0.08);

            nametagPosition = EntityUtil.interpolateEntityByTicks(viewEntity, event.getPartialTicks());

            double posX = viewEntity.posX;
            double posY = viewEntity.posY;
            double posZ = viewEntity.posZ;

            viewEntity.posX = nametagPosition.x;
            viewEntity.posY = nametagPosition.y;
            viewEntity.posZ = nametagPosition.z;

            double distanceScale = scale.getValue();

            if (distance > 0.0 && scaleByDistance.getValue())
                distanceScale = 2 + (scale.getValue() / 10) * distance;

            String nameTag = generateNameTag(entityPlayer);
            float width = FontUtil.getStringWidth(nameTag) / 2;
            float height = MixinInterface.mc.fontRenderer.FONT_HEIGHT;

            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            RenderUtil.drawNametag(nameTag, x, y, z, width, height, distanceScale, background.getValue());
            GlStateManager.pushMatrix();

            Iterator<ItemStack> armorStack = entityPlayer.getArmorInventoryList().iterator();
            ArrayList<ItemStack> stacks = new ArrayList<>();

            if (offhand.getValue())
                stacks.add(entityPlayer.getHeldItemOffhand());

            while (armorStack.hasNext()) {
                ItemStack stack = armorStack.next();
                if (!stack.isEmpty() && armor.getValue())
                    stacks.add(stack);
            }

            if (mainhand.getValue())
                stacks.add(entityPlayer.getHeldItemMainhand());

            Collections.reverse(stacks);

            int offset = stacks.size();
            int offsetScaled = -(8 * offset);

            if (exp.getValue()) {
                renderItemStack(entityPlayer, Items.EXPERIENCE_BOTTLE.getDefaultInstance(), offsetScaled, -32, 0, true);
                offset++;
                offsetScaled += 16;
            }

            if (itemName.getValue())
                renderItemName(entityPlayer.getHeldItemMainhand(), (int) -width / 2, -78);

            for (ItemStack stack : stacks) {
                renderItemStack(entityPlayer, stack, offsetScaled, -32, 0, false);
                renderItemEnchantments(stack, offsetScaled, -62);
                offsetScaled += 16;
            }

            GlStateManager.popMatrix();

            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.disablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.popMatrix();

            MixinInterface.mc.getRenderViewEntity().posX = posX;
            MixinInterface.mc.getRenderViewEntity().posY = posY;
            MixinInterface.mc.getRenderViewEntity().posZ = posZ;
        });
    }

    public String getEnchantName(Enchantment enchantment, int translated) {
        if (enchants.getValue()) {
            if (enchantment.getTranslatedName(translated).contains("Vanish"))
                return TextFormatting.RED + "Van";
            if (enchantment.getTranslatedName(translated).contains("Bind"))
                return TextFormatting.RED + "Bind";

            String substring = enchantment.getTranslatedName(translated);
            int translation = (translated > 1) ? 2 : 3;
            if (substring.length() > translation)
                substring = substring.substring(0, translation);

            StringBuilder builder = new StringBuilder();
            String rawString = substring;
            String finalString = builder.insert(0, rawString.substring(0, 1).toUpperCase()).append(substring.substring(1)).toString();

            if (translated > 1)
                finalString = new StringBuilder().insert(0, finalString).append(translated).toString();

            return finalString;
        }

        return "";
    }

    public void renderItemName(ItemStack itemStack, int x, int y) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        FontUtil.drawString(itemStack.getDisplayName(), (float) (x * 2), (float) y, -1);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }

    public void renderItemEnchantments(ItemStack itemStack, int x, int y) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        Iterator<Enchantment> iterator2;
        Iterator<Enchantment> iterator = iterator2 = EnchantmentHelper.getEnchantments(itemStack).keySet().iterator();

        float durabilityScaled = ((float) (itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float) itemStack.getMaxDamage()) * 100.0f;

        int color = 0x1FFF00;

        if (durabilityScaled > 30 && durabilityScaled < 70)
            color = 0xFFFF00;
        else if (durabilityScaled <= 30)
            color = 0xFF0000;

        if (durability.getValue() && (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemTool)) {
            GlStateManager.disableDepth();
            FontUtil.drawString(new StringBuilder().insert(0, ((int) (durabilityScaled))).append('%').toString(), (float) (x * 2), (float) y - 8, color);
            GlStateManager.enableDepth();
        }

        if (enchants.getValue()) {
            while (iterator.hasNext()) {
                Enchantment enchantment;
                if ((enchantment = iterator2.next()) == null)
                    iterator = iterator2;

                else {
                    FontUtil.drawString(getEnchantName(enchantment, EnchantmentHelper.getEnchantmentLevel(enchantment, itemStack)), (float) (x * 2), (float) y, -1);

                    y += 8;
                    iterator = iterator2;
                }
            }
        }

        if (itemStack.getItem().equals(Items.GOLDEN_APPLE) && itemStack.hasEffect())
            FontUtil.drawString(TextFormatting.DARK_RED + "God", (float) (x * 2), (float) y, -1);

        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }

    public void renderItemStack(EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int scaled, boolean custom) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        MixinInterface.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        int scaledFinal = (scaled > 4) ? ((scaled - 4) * 8 / 2) : 0;
        MixinInterface.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y + scaledFinal);

        if (custom)
            MixinInterface.mc.fontRenderer.drawStringWithShadow(GearManager.expMap.get(entityPlayer) == null ? String.valueOf(0) : String.valueOf(GearManager.expMap.get(entityPlayer)), (float) (x + 17 - MixinInterface.mc.fontRenderer.getStringWidth(GearManager.expMap.get(entityPlayer) == null ? String.valueOf(0) : String.valueOf(GearManager.expMap.get(entityPlayer)))), (float) (y + 9), 16777215);
        else
            MixinInterface.mc.getRenderItem().renderItemOverlays(MixinInterface.mc.fontRenderer, itemStack, x, y + scaledFinal);

        MixinInterface.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    public String generateNameTag(EntityPlayer entityPlayer) {
        try {
            return generateName(entityPlayer) + generateGamemode(entityPlayer) + TextFormatting.RESET + generateTotemPops(entityPlayer) + getPingText(entityPlayer, MixinInterface.mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime()) + generatePing(entityPlayer) + getHealthText(EnemyUtil.getHealth(entityPlayer)) + generateHealth(entityPlayer);
        } catch (Exception e) {

        }

        return "";
    }

    public String generateName(EntityPlayer entityPlayer) {
        if (FriendManager.isFriend(entityPlayer.getName()))
            return TextFormatting.AQUA + entityPlayer.getName() + TextFormatting.RESET;
        else if (EnemyManager.isEnemy(entityPlayer.getName()))
            return TextFormatting.DARK_RED + entityPlayer.getName() + TextFormatting.RESET;
        else if (entityPlayer.isSneaking())
            return TextFormatting.GOLD + entityPlayer.getName() + TextFormatting.RESET;
        else
            return entityPlayer.getName();
    }

    public String generateHealth(EntityPlayer entityPlayer) {
        return health.getValue() ? " " + MathUtil.roundAvoid(EnemyUtil.getHealth(entityPlayer), 1) : "";
    }

    public String generateGamemode(EntityPlayer entityPlayer) {
        if (gamemode.getValue()) {
            if (entityPlayer.isCreative())
                return (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : "") + " [C]";
            else if (entityPlayer.isSpectator())
                return (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : "") + " [I]";
            else
                return (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : "") + " [S]";
        }

        else
            return "";
    }

    public String generatePing(EntityPlayer entityPlayer) {
        if (!MixinInterface.mc.isSingleplayer())
            return ping.getValue() ? " " + MixinInterface.mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime() + "ms" : "";
        else
            return ping.getValue() ? " -1 ms" : "";
    }

    public String generateTotemPops(EntityPlayer entityPlayer) {
        if (totemPops.getValue()) {
            if (GearManager.totemMap.containsKey(entityPlayer.getName()))
                return (FriendManager.isFriend(entityPlayer.getName()) ? TextFormatting.AQUA : TextFormatting.RESET) + " " + GearManager.totemMap.get(entityPlayer.getName());
            else
                return " 0";
        }

        else
            return "";
    }

    public TextFormatting getPingText(EntityPlayer entityPlayer, float ping) {
        if (FriendManager.isFriend(entityPlayer.getName()))
            return TextFormatting.AQUA;
        else if (ping <= 20)
            return TextFormatting.DARK_GREEN;
        else if (ping <= 50)
            return TextFormatting.GREEN;
        else if (ping <= 90)
            return TextFormatting.YELLOW;
        else if (ping <= 130)
            return TextFormatting.GOLD;
        else
            return TextFormatting.RED;
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