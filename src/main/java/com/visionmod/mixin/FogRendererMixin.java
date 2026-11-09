
package com.visionmod.mixin;

import com.visionmod.VisionModClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @ModifyArgs(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"
        ),
        require = 0
    )
    private void visionmod$removeLiquidFog(Args args) {

        if (!VisionModClient.enabled) {
            return;
        }

        Minecraft client = Minecraft.getInstance();

        if (client == null || client.gameRenderer == null) {
            return;
        }

        Camera camera = client.gameRenderer.getMainCamera();

        if (camera == null) {
            return;
        }

        FogType fluid = camera.getFluidInCamera();

        if (fluid != FogType.WATER && fluid != FogType.LAVA) {
            return;
        }

        float far = 1000000.0F;

        args.set(3, far);
        args.set(4, far + 1000.0F);

        args.set(5, far);
        args.set(6, far + 1000.0F);
    }
}
