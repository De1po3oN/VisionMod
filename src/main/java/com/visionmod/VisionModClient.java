
package com.visionmod;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

public final class VisionModClient implements ClientModInitializer {

    // Общий переключатель End
    public static boolean enabled = true;

    // Отдельные функции
    public static boolean fullBright = true;
    public static boolean oreHighlight = true;
    public static boolean clearWater = true;
    public static boolean clearLava = true;
    public static boolean playerHearts = true;

    public static boolean isFullBrightEnabled() {
        return enabled && fullBright;
    }

    public static boolean isOreEnabled() {
        return enabled && oreHighlight;
    }

    public static boolean isWaterEnabled() {
        return enabled && clearWater;
    }

    public static boolean isLavaEnabled() {
        return enabled && clearLava;
    }

    public static boolean isHeartsEnabled() {
        return enabled && playerHearts;
    }

    @Override
    public void onInitializeClient() {

        OreHighlight.init();

        KeyMapping.Category category =
            KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(
                    "visionmod", "general"
                )
            );

        // END: включение / выключение всех функций
        KeyMapping toggleAll =
            KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                    "key.visionmod.toggle",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_END,
                    category
                )
            );

        // RIGHT SHIFT: открыть меню
        KeyMapping openMenu =
            KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                    "key.visionmod.menu",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_RIGHT_SHIFT,
                    category
                )
            );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (toggleAll.consumeClick()) {
                enabled = !enabled;

                if (client.player != null) {
                    client.player.displayClientMessage(
                        Component.literal(
                            enabled
                                ? "§aVision Mod: ON"
                                : "§cVision Mod: OFF"
                        ),
                        true
                    );
                }
            }

            while (openMenu.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new VisionScreen());
                }
            }
        });
    }
}
