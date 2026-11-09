package com.visionmod;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** First build: keybind + gamma toggle. Other rendering effects are not implemented yet. */
public final class VisionModClient implements ClientModInitializer {
    private static boolean enabled;
    private static Double oldGamma;
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("visionmod", "controls"));
    private static final KeyMapping TOGGLE = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.visionmod.toggle", InputConstants.Type.KEYSYM,
                    InputConstants.KEY_END, CATEGORY));

    @Override public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE.consumeClick()) {
                if (client.player == null) continue;
                enabled = !enabled;
                if (enabled) {
                    oldGamma = client.options.gamma().get();
                    client.options.gamma().set(16.0);
                } else if (oldGamma != null) {
                    client.options.gamma().set(oldGamma);
                    oldGamma = null;
                }
                client.player.displayClientMessage(Component.literal(
                        enabled ? "§aVision Mod: ON" : "§cVision Mod: OFF"), true);
            }
        });
    }
}
