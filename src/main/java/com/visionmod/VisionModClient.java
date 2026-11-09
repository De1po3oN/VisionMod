
package com.visionmod;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class VisionModClient implements ClientModInitializer {

    public static volatile boolean enabled = false;

    @Override
    public void onInitializeClient() {
OreHighlight.init();
        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("visionmod", "general")
        );

        KeyMapping toggle = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.visionmod.toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_END,
                category
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggle.consumeClick()) {
                enabled = !enabled;

                if (client.player != null) {
                    client.player.displayClientMessage(
                        Component.literal(
                            "Vision Mod: " + (enabled ? "ON" : "OFF")
                        ),
                        true
                    );
                }
            }
        });
    }
}
