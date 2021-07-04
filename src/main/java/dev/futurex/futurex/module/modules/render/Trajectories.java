package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ingros devs, ported in and cleaned up by linustouchtips
 * @since 11/29/2020
 */

public class Trajectories extends Module {
    public Trajectories() {
        super("Trajectories", Category.RENDER, "Shows the flight path of throwable objects");
    }

    public static dev.futurex.futurex.setting.checkbox.Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(250, 0, 250));

    @Override
    public void setup() {
        addSetting(color);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderEvent) {
        if (nullCheck())
            return;

        double renderPosX = MixinInterface.mc.player.lastTickPosX + (MixinInterface.mc.player.posX - MixinInterface.mc.player.lastTickPosX) * renderEvent.getPartialTicks();
        double renderPosY = MixinInterface.mc.player.lastTickPosY + (MixinInterface.mc.player.posY - MixinInterface.mc.player.lastTickPosY) * renderEvent.getPartialTicks();
        double renderPosZ = MixinInterface.mc.player.lastTickPosZ + (MixinInterface.mc.player.posZ - MixinInterface.mc.player.lastTickPosZ) * renderEvent.getPartialTicks();
        MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND);

        if (MixinInterface.mc.gameSettings.thirdPersonView != 0)
            return;

        if (!(MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle) || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemLingeringPotion || MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSplashPotion)
            return;

        GL11.glPushMatrix();
        Item item = MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
        double posX = renderPosX - MathHelper.cos(MixinInterface.mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        double posY = renderPosY + MixinInterface.mc.player.getEyeHeight() - 0.1000000014901161;
        double posZ = renderPosZ - MathHelper.sin(MixinInterface.mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        double motionX = -MathHelper.sin(MixinInterface.mc.player.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(MixinInterface.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionY = -MathHelper.sin(MixinInterface.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionZ = MathHelper.cos(MixinInterface.mc.player.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(MixinInterface.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        int useCount = 72000 - MixinInterface.mc.player.getItemInUseCount();
        float power = useCount / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;

        if (power > 1.0f)
            power = 1.0f;

        float distance = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        float pow = (item instanceof ItemBow) ? (power * 2.0f) : ((item instanceof ItemFishingRod) ? 1.25f : ((MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.9f : 1.0f));
        motionX *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
        motionY *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
        motionZ *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        MixinInterface.mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(2);

        GlStateManager.color(colorPicker.getColor().getRed() / 255f, colorPicker.getColor().getGreen() / 255f, colorPicker.getColor().getBlue() / 255f, colorPicker.getColor().getAlpha() / 255f);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        float size = (float) ((item instanceof ItemBow) ? 0.3 : 0.25);
        boolean hasLanded = false;
        Entity landingOnEntity = null;
        RayTraceResult landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            Vec3d present = new Vec3d(posX, posY, posZ);
            Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult possibleLandingStrip = MixinInterface.mc.world.rayTraceBlocks(present, future, false, true, false);

            if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != RayTraceResult.Type.MISS) {
                landingPosition = possibleLandingStrip;
                hasLanded = true;
            }

            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
            List<Entity> entities = this.getEntitiesWithinAABB(arrowBox.offset(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0));
            for (Entity entity : entities) {
                if (entity.canBeCollidedWith() && entity != MixinInterface.mc.player) {
                    float var7 = 0.3f;
                    AxisAlignedBB var8 = entity.getEntityBoundingBox().expand(var7, var7, var7);
                    RayTraceResult possibleEntityLanding = var8.calculateIntercept(present, future);

                    if (possibleEntityLanding == null)
                        continue;

                    hasLanded = true;
                    landingOnEntity = entity;
                    landingPosition = possibleEntityLanding;
                }
            }

            if (landingOnEntity != null)
                GlStateManager.color(colorPicker.getColor().getRed() / 255f, colorPicker.getColor().getGreen() / 255f, colorPicker.getColor().getBlue() / 255f, colorPicker.getColor().getAlpha() / 255f);

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float motionAdjustment = 0.99f;
            motionX *= motionAdjustment;
            motionY *= motionAdjustment;
            motionZ *= motionAdjustment;
            motionY -= ((item instanceof ItemBow) ? 0.05 : 0.03);
            drawLine3D(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
        }

        if (landingPosition != null && landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
            int side = landingPosition.sideHit.getIndex();

            if (side == 2)
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            else if (side == 3)
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            else if (side == 4)
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            else if (side == 5)
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);

            Cylinder c = new Cylinder();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            c.setDrawStyle(100011);

            if (landingOnEntity != null) {
                GlStateManager.color(colorPicker.getColor().getRed() / 255f, colorPicker.getColor().getGreen() / 255f, colorPicker.getColor().getBlue() / 255f, colorPicker.getColor().getAlpha() / 255f);
                GL11.glLineWidth(2.5f);
                c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                GL11.glLineWidth(0.1f);
                GlStateManager.color(colorPicker.getColor().getRed() / 255f, colorPicker.getColor().getGreen() / 255f, colorPicker.getColor().getBlue() / 255f, colorPicker.getColor().getAlpha() / 255f);
            }

            c.draw(0.6f, 0.3f, 0.0f, 4, 1);
        }

        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
    }

    public void drawLine3D(double var1, double var2, double var3) {
        GL11.glVertex3d(var1, var2, var3);
    }

    List<Entity> getEntitiesWithinAABB(AxisAlignedBB bb) {
        ArrayList<Entity> list = new ArrayList<>();
        int chunkMinX = MathHelper.floor((bb.minX - 2.0) / 16.0);
        int chunkMaxX = MathHelper.floor((bb.maxX + 2.0) / 16.0);
        int chunkMinZ = MathHelper.floor((bb.minZ - 2.0) / 16.0);
        int chunkMaxZ = MathHelper.floor((bb.maxZ + 2.0) / 16.0);

        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (MixinInterface.mc.world.getChunkProvider().getLoadedChunk(x, z) != null)
                    MixinInterface.mc.world.getChunk(x, z).getEntitiesWithinAABBForEntity(MixinInterface.mc.player, bb, list, null);
            }
        }

        return list;
    }
}