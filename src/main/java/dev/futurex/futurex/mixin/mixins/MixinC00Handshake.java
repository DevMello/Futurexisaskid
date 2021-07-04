package dev.futurex.futurex.mixin.mixins;

import dev.futurex.futurex.module.modules.misc.FakeGameMode;
import dev.futurex.futurex.managers.ModuleManager;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Mixin(C00Handshake.class)
public class MixinC00Handshake {

    @Shadow private int protocolVersion;
    @Shadow public String ip;
    @Shadow public int port;
    @Shadow private EnumConnectionState requestedState;

    @Inject(method = "writePacketData", at = @At(value = "HEAD"), cancellable = true)
    public void writePacketData(PacketBuffer buf, CallbackInfo info) {
        if (ModuleManager.getModuleByName("FakeGameMode").isEnabled() && FakeGameMode.noHandshake.getValue()) {
            info.cancel();
            buf.writeVarInt(protocolVersion);
            buf.writeString(ip);
            buf.writeShort(port);
            buf.writeVarInt(requestedState.getId());
        }
    }
}
