package dev.futurex.futurex.module.modules.render.esp.modes;

import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.util.render.ESPUtil;
import dev.futurex.futurex.util.render.RenderUtil;
import dev.futurex.futurex.util.world.EntityUtil;
import dev.futurex.futurex.module.modules.render.ESP;
import dev.futurex.futurex.module.modules.render.esp.ESPMode;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 01/06/2021
 */

public class Outline extends ESPMode {
    public Outline() {
        isMixin = true;
    }

    @Override
    public void drawESPMixin(ModelBase mainModel, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn == mc.player.getRidingEntity())
            return;

        RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        if (!RenderUtil.camera.isBoundingBoxInFrustum(entitylivingbaseIn.getEntityBoundingBox()))
            return;

        if (ESP.colorManager.abstractColorRegistry.containsKey(entitylivingbaseIn.getClass()) && (entitylivingbaseIn instanceof EntityPlayer && !(entitylivingbaseIn instanceof EntityPlayerSP) && ESP.players.getValue() || (EntityUtil.isPassive(entitylivingbaseIn) && ESP.animals.getValue()) || (EntityUtil.isHostileMob(entitylivingbaseIn) && ESP.mobs.getValue()) || (EntityUtil.isVehicle(entitylivingbaseIn) && ESP.vehicles.getValue()) || (entitylivingbaseIn instanceof EntityEnderCrystal && ESP.crystals.getValue()))) {
            ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.abstractColorRegistry.get(entitylivingbaseIn.getClass()));
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderOne((float) ESP.lineWidth.getValue());
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderTwo();
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderThree();
            ESPUtil.renderFour();
            ESPUtil.setColor(FriendManager.isFriend(entitylivingbaseIn.getName()) ? ESP.colorManager.colorRegistry.get("Friend") : ESP.colorManager.abstractColorRegistry.get(entitylivingbaseIn.getClass()));
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            ESPUtil.renderFive();
            ESPUtil.setColor(Color.WHITE);
        }
    }

    @Override
    public void drawESPCrystal(ModelBase modelEnderCrystal, ModelBase modelEnderCrystalNoBase, EntityEnderCrystal entityEnderCrystalIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callback, ResourceLocation texture) {
        RenderUtil.camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

        if (!RenderUtil.camera.isBoundingBoxInFrustum(entityEnderCrystalIn.getEntityBoundingBox()))
            return;

        if (ESP.colorManager.abstractColorRegistry.containsKey(entityEnderCrystalIn.getClass())) {
            float rotation = entityEnderCrystalIn.innerRotation + partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            mc.renderManager.renderEngine.bindTexture(texture);

            float rotationRounded = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f;
            rotationRounded += Math.pow(rotationRounded, 2);

            GL11.glLineWidth((float) (3 + ESP.lineWidth.getValue()));

            if (entityEnderCrystalIn.shouldShowBottom())
                modelEnderCrystal.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderOne((float) (3 + ESP.lineWidth.getValue()));

            if (entityEnderCrystalIn.shouldShowBottom())
                modelEnderCrystal.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderTwo();

            if (entityEnderCrystalIn.shouldShowBottom())
                modelEnderCrystal.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderThree();
            ESPUtil.renderFour();

            ESPUtil.setColor(ESP.colorManager.abstractColorRegistry.get(entityEnderCrystalIn.getClass()));

            if (entityEnderCrystalIn.shouldShowBottom())
                modelEnderCrystal.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);
            else
                modelEnderCrystalNoBase.render(entityEnderCrystalIn, 0.0f, rotation * 3.0f, rotationRounded * 0.2f, 0.0f, 0.0f, 0.0625f);

            ESPUtil.renderFive();
            GlStateManager.popMatrix();
        }
    }
}
