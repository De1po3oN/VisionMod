
package com.visionmod;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class VisionScreen extends Screen {

    public VisionScreen() {
        super(Component.literal("Vision Mod"));
    }

    @Override
    protected void init() {

        int buttonWidth = 180;
        int buttonHeight = 20;
        int spacing = 24;

        int x = (this.width - buttonWidth) / 2;
        int y = Math.max(35, (this.height - 180) / 2);

        // FullBright
        addToggle(
            "FullBright",
            VisionModClient.fullBright,
            x, y, buttonWidth, buttonHeight,
            button -> {
                VisionModClient.fullBright =
                    !VisionModClient.fullBright;

                button.setMessage(label(
                    "FullBright",
                    VisionModClient.fullBright
                ));
            }
        );

        // Ore Highlight
        addToggle(
            "Ore Highlight",
            VisionModClient.oreHighlight,
            x, y + spacing, buttonWidth, buttonHeight,
            button -> {
                VisionModClient.oreHighlight =
                    !VisionModClient.oreHighlight;

                button.setMessage(label(
                    "Ore Highlight",
                    VisionModClient.oreHighlight
                ));
            }
        );

        // Clear Water
        addToggle(
            "Clear Water",
            VisionModClient.clearWater,
            x, y + spacing * 2,
            buttonWidth, buttonHeight,
            button -> {
                VisionModClient.clearWater =
                    !VisionModClient.clearWater;

                button.setMessage(label(
                    "Clear Water",
                    VisionModClient.clearWater
                ));
            }
        );

        // Clear Lava
        addToggle(
            "Clear Lava",
            VisionModClient.clearLava,
            x, y + spacing * 3,
            buttonWidth, buttonHeight,
            button -> {
                VisionModClient.clearLava =
                    !VisionModClient.clearLava;

                button.setMessage(label(
                    "Clear Lava",
                    VisionModClient.clearLava
                ));
            }
        );

        // Player Hearts
        addToggle(
            "Player Hearts",
            VisionModClient.playerHearts,
            x, y + spacing * 4,
            buttonWidth, buttonHeight,
            button -> {
                VisionModClient.playerHearts =
                    !VisionModClient.playerHearts;

                button.setMessage(label(
                    "Player Hearts",
                    VisionModClient.playerHearts
                ));
            }
        );

        // Кнопка закрытия
        this.addRenderableWidget(
            Button.builder(
                Component.literal("Close"),
                button -> this.onClose()
            ).bounds(
                x,
                y + spacing * 5 + 8,
                buttonWidth,
                buttonHeight
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
            ).bounds(
                x, y, width, height
            ).build()
        );
    }

    private static Component label(
            String name,
            boolean value
    ) {
        return Component.literal(
            name + (value ? "  §aON" : "  §cOFF")
        );
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(
            this.font,
            "VISION MOD",
            this.width / 2,
            Math.max(12, (this.height - 180) / 2 - 20),
            0xFFFFFF
        );

        graphics.drawCenteredString(
            this.font,
            VisionModClient.enabled
                ? "Modules active"
                : "All modules disabled by End",
            this.width / 2,
            this.height - 20,
            VisionModClient.enabled
                ? 0x55FF55
                : 0xFF5555
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
