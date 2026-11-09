
package com.visionmod;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import org.lwjgl.glfw.GLFW;

public final class VisionScreen extends Screen {

    private Button particlesButton;

    public VisionScreen() {
        super(Component.literal("Vision Mod"));
    }

    @Override
    protected void init() {

        int width = 190;
        int height = 20;
        int spacing = 24;

        int x = (this.width - width) / 2;
        int y = Math.max(35, (this.height - 190) / 2);

        addToggle("FullBright", VisionModClient.fullBright,
            x, y, width, height,
            button -> {
                VisionModClient.fullBright =
                    !VisionModClient.fullBright;
                button.setMessage(label(
                    "FullBright", VisionModClient.fullBright
                ));
            });

        addToggle("Ore Highlight", VisionModClient.oreHighlight,
            x, y + spacing, width, height,
            button -> {
                VisionModClient.oreHighlight =
                    !VisionModClient.oreHighlight;
                button.setMessage(label(
                    "Ore Highlight", VisionModClient.oreHighlight
                ));
            });

        addToggle("Clear Water", VisionModClient.clearWater,
            x, y + spacing * 2, width, height,
            button -> {
                VisionModClient.clearWater =
                    !VisionModClient.clearWater;
                button.setMessage(label(
                    "Clear Water", VisionModClient.clearWater
                ));
            });

        addToggle("Clear Lava", VisionModClient.clearLava,
            x, y + spacing * 3, width, height,
            button -> {
                VisionModClient.clearLava =
                    !VisionModClient.clearLava;
                button.setMessage(label(
                    "Clear Lava", VisionModClient.clearLava
                ));
            });

        addToggle("Player Hearts", VisionModClient.playerHearts,
            x, y + spacing * 4, width, height,
            button -> {
                VisionModClient.playerHearts =
                    !VisionModClient.playerHearts;
                button.setMessage(label(
                    "Player Hearts", VisionModClient.playerHearts
                ));
            });

        particlesButton = this.addRenderableWidget(
            Button.builder(
                label("Particles", VisionModClient.particles),
                button -> {
                    VisionModClient.particles =
                        !VisionModClient.particles;

                    button.setMessage(label(
                        "Particles", VisionModClient.particles
                    ));
                }
            ).bounds(
                x, y + spacing * 5, width, height
            ).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Close"),
                button -> onClose()
            ).bounds(
                x, y + spacing * 6 + 5,
                width, height
            ).build()
        );
    }

    private void addToggle(
            String name,
            boolean value,
            int x,
            int y,
            int width,
            int height,
            Button.OnPress action
    ) {
        this.addRenderableWidget(
            Button.builder(
                label(name, value),
                action
            ).bounds(x, y, width, height).build()
        );
    }

    private static Component label(
            String name, boolean value
    ) {
        return Component.literal(
            name + (value ? "  [ON]" : "  [OFF]")
        );
    }

    @Override
    public boolean mouseClicked(
            MouseButtonEvent click,
            boolean doubled
    ) {

        if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT
                && particlesButton != null
                && particlesButton.isMouseOver(
                    click.x(), click.y()
                )) {

            this.minecraft.setScreen(
                new ParticleSettingsScreen()
            );

            return true;
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.render(graphics, mouseX, mouseY, delta);

        int y = Math.max(35, (this.height - 190) / 2);

        graphics.drawCenteredString(
            this.font,
            "VISION MOD",
            this.width / 2,
            y - 22,
            0xFFFFFFFF
        );

        graphics.drawCenteredString(
            this.font,
            "Right-click Particles for settings",
            this.width / 2,
            y + 174,
            0xFFAAAAAA
        );

        graphics.drawCenteredString(
            this.font,
            VisionModClient.enabled
                ? "Modules enabled"
                : "All modules disabled by End",
            this.width / 2,
            y + 187,
            VisionModClient.enabled
                ? 0xFF55FF55
                : 0xFFFF5555
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
