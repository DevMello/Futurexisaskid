package dev.futurex.futurex.module.modules.render.esp.modes;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.util.render.shader.FramebufferShader;
import dev.futurex.futurex.util.render.shader.shaders.Glow;
import dev.futurex.futurex.module.modules.render.ESP;
import dev.futurex.futurex.module.modules.render.esp.ESPMode;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;

public class Shader extends ESPMode {

    @Override
    public void drawESPOverlay(RenderWorldLastEvent event) {
        FramebufferShader shader = Glow.Glow_SHADER;

        if (shader == null)
            return;

        shader.startDraw(event.getPartialTicks());

        try {
            for (Entity entity : mc.world.loadedEntityList) {
                mc.getRenderManager().renderEntityStatic(entity, mc.timer.renderPartialTicks, true);
            }
        } catch (Exception l) {
            FutureX.LOGGER.info("cannot render entity");
        }

        float radius = (float) ESP.lineWidth.getValue();

        shader.stopDraw(new Color(255, 255, 255), radius, 1F);
    }
}
