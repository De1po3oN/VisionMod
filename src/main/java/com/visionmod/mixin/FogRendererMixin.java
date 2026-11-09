
package com.visionmod.mixin;

import com.visionmod.VisionModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    private static boolean visionmod$inLiquid() {
        if (!VisionModClient.enabled) {
            return false;
        }

        Minecraft client = Minecraft.getInstance();

        return client.player != null &&
            (client.player.isInWater() || client.player.isInLava());
    }

    @ModifyVariable(
        method = "updateBuffer",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0,
        require = 0
    )
    private float visionmod$environmentStart(float original) {
        return visionmod$inLiquid() ? 0.0F : original;
    }

    @ModifyVariable(
        method = "updateBuffer",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 1,
        require = 0
    )
    private float visionmod$environmentEnd(float original) {
        return visionmod$inLiquid() ? 512.0F : original;
    }

    @ModifyVariable(
        method = "updateBuffer",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 2,
        require = 0
    )
    private float visionmod$renderStart(float original) {
        return visionmod$inLiquid() ? 0.0F : original;
    }

    @ModifyVariable(
        method = "updateBuffer",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 3,
        require = 0
    )
    private float visionmod$renderEnd(float original) {
        return visionmod$inLiquid() ? 512.0F : original;
    }
}

