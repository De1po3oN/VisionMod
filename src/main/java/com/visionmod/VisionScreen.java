
package com.visionmod;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class VisionScreen extends Screen {

    private static final int BG = 0xEB101722;
    private static final int ROW = 0xD9212C3B;
    private static final int HOVER = 0xDD34485D;
    private static final int CYAN = 0xFF67E8F9;
    private static final int WHITE = 0xFFF0F6FF;
    private static final int MUTED = 0xFF9BAABD;
    private static final int GREEN = 0xFF65F0B2;

    private static final String[] MODULES = {
        "FullBright",
        "Ore Highlight",
        "Clear Water",
        "Clear Lava",
        "Player Hearts",
        "Particles"
    };

    private static final int[] PALETTE = {
        0xFF67E8F9,
        0xFFFF657D,
        0xFF65F0B2,
        0xFFFFD166,
        0xFFB48CFF,
        0xFFFF78CB,
        0xFFFFA65C,
        0xFFF0F6FF
    };

    private static final String[] COLOR_NAMES = {
        "Cyan", "Red", "Green", "Yellow",
        "Purple", "Pink", "Orange", "White"
    };

    private enum Page {
        MAIN, ORE_COLORS
    }

    private Page page = Page.MAIN;
    private int selectedOre = 0;
    private long animationStart = System.nanoTime();

    public VisionScreen() {
        super(Component.literal("Vision Mod"));
    }

    private int panelWidth() {
        return page == Page.MAIN ? 244 : 306;
    }

    private int panelHeight() {
        return page == Page.MAIN ? 218 : 252;
    }

    private int left() {
        return (this.width - panelWidth()) / 2;
    }

    private float progress() {
        float t = (System.nanoTime() - animationStart)
            / 230_000_000.0F;

        t = Math.max(0.0F, Math.min(1.0F, t));

        // Smooth ease-out animation.
        float q = 1.0F - t;
        return 1.0F - q * q * q;
    }

    private int top() {
        int finalY = Math.max(
            8, this.height - panelHeight() - 46
        );

        int startY = this.height - 42;

        return Math.round(
            startY + (finalY - startY) * progress()
        );
    }

    private void changePage(Page newPage) {
        page = newPage;
        animationStart = System.nanoTime();
    }

    @Override
    public void renderBackground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        // Keep the game visible behind the menu.
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.render(graphics, mouseX, mouseY, delta);

        int x = left();
        int y = top();
        int w = panelWidth();
        int h = panelHeight();

        // Soft shadow.
        graphics.fill(
            x - 3, y + 3,
            x + w + 3, y + h + 5,
            0x55000000
        );

        // Main panel and cyan top border.
        graphics.fill(x, y, x + w, y + h, BG);
        graphics.fill(
            x, y, x + w, y + 2, CYAN
        );

        graphics.drawString(
            this.font,
            page == Page.MAIN
                ? "VISION MOD"
                : "ORE COLORS",
            x + 12, y + 12, WHITE
        );

        graphics.drawString(
            this.font,
            page == Page.MAIN
                ? "FABRIC 1.21.11"
                : "SELECT ORE + COLOR",
            x + w - (page == Page.MAIN ? 96 : 128),
            y + 12,
            MUTED
        );

        graphics.fill(
            x + 10, y + 30,
            x + w - 10, y + 31,
            0xFF354555
        );

        if (page == Page.MAIN) {
            drawModules(graphics, mouseX, mouseY, x, y, w);
        } else {
            drawOreColors(graphics, mouseX, mouseY, x, y, w);
        }
    }

    private void drawModules(
            GuiGraphics g,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int w
    ) {
        for (int i = 0; i < MODULES.length; i++) {
            int rowY = y + 37 + i * 25;

            boolean hovered = inside(
                mouseX, mouseY,
                x + 9, rowY, w - 18, 22
            );

            g.fill(
                x + 9, rowY,
                x + w - 9, rowY + 22,
                hovered ? HOVER : ROW
            );

            boolean active = active(i);
            int accent = active ? CYAN : 0xFF526070;

            g.fill(
                x + 9, rowY + 3,
                x + 12, rowY + 19,
                accent
            );

            g.drawString(
                this.font,
                MODULES[i],
                x + 20, rowY + 7, WHITE
            );

            g.drawString(
                this.font,
                active ? "ON" : "OFF",
                x + w - 40,
                rowY + 7,
                active ? GREEN : MUTED
            );
        }

        g.drawCenteredString(
            this.font,
            "RMB: Ore Colors / Particles",
            x + w / 2,
            y + 194,
            MUTED
        );
    }

    private void drawOreColors(
            GuiGraphics g,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int w
    ) {
        // 10 ore categories in two columns.
        for (int i = 0; i < OreColors.NAMES.length; i++) {
            int col = i / 5;
            int row = i % 5;

            int bx = x + 10 + col * 144;
            int by = y + 39 + row * 27;

            boolean selected = selectedOre == i;
            boolean hovered = inside(
                mouseX, mouseY, bx, by, 139, 24
            );

            g.fill(
                bx, by, bx + 139, by + 24,
                selected
                    ? 0xFF285264
                    : (hovered ? HOVER : ROW)
            );

            int color = OreColors.getColor(i);

            g.fill(
                bx + 6, by + 7,
                bx + 16, by + 17,
                color
            );

            g.drawString(
                this.font,
                OreColors.NAMES[i],
                bx + 22, by + 8,
                selected ? CYAN : WHITE
            );
        }

        g.drawString(
            this.font,
            "COLOR:",
            x + 12, y + 185,
            MUTED
        );

        for (int i = 0; i < PALETTE.length; i++) {
            int bx = x + 12 + i * 35;
            int by = y + 200;

            int color = PALETTE[i];

            g.fill(
                bx, by, bx + 29, by + 26,
                0xFF182536
            );

            g.fill(
                bx + 3, by + 3,
                bx + 26, by + 23,
                color
            );

            if (OreColors.getColor(selectedOre) == color) {
                g.renderOutline(
                    bx, by, 29, 26,
                    0xFFFFFFFF
                );
            }
        }

        g.drawCenteredString(
            this.font,
            "BACK",
            x + w / 2,
            y + 235,
            CYAN
        );
    }

    private boolean active(int id) {
        return switch (id) {
            case 0 -> VisionModClient.fullBright;
            case 1 -> VisionModClient.oreHighlight;
            case 2 -> VisionModClient.clearWater;
            case 3 -> VisionModClient.clearLava;
            case 4 -> VisionModClient.playerHearts;
            case 5 -> VisionModClient.particles;
            default -> false;
        };
    }

    private void toggle(int id) {
        switch (id) {
            case 0 -> VisionModClient.fullBright =
                !VisionModClient.fullBright;
            case 1 -> VisionModClient.oreHighlight =
                !VisionModClient.oreHighlight;
            case 2 -> VisionModClient.clearWater =
                !VisionModClient.clearWater;
            case 3 -> VisionModClient.clearLava =
                !VisionModClient.clearLava;
            case 4 -> VisionModClient.playerHearts =
                !VisionModClient.playerHearts;
            case 5 -> VisionModClient.particles =
                !VisionModClient.particles;
            default -> {
            }
        }
    }

    private static boolean inside(
            double mx,
            double my,
            int x,
            int y,
            int w,
            int h
    ) {
        return mx >= x && mx < x + w
            && my >= y && my < y + h;
    }

    @Override
    public boolean mouseClicked(
            MouseButtonEvent click,
            boolean doubled
    ) {
        int x = left();
        int y = top();
        int w = panelWidth();

        double mx = click.x();
        double my = click.y();

        boolean leftClick =
            click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT;

        boolean rightClick =
            click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT;

        if (page == Page.MAIN) {
            for (int i = 0; i < MODULES.length; i++) {
                int by = y + 37 + i * 25;

                if (inside(
                    mx, my, x + 9, by, w - 18, 22
                )) {
                    if (leftClick) {
                        toggle(i);
                        return true;
                    }

                    if (rightClick && i == 1) {
                        changePage(Page.ORE_COLORS);
                        return true;
                    }

                    if (rightClick && i == 5) {
                        this.minecraft.setScreen(
                            new ParticleSettingsScreen()
                        );
                        return true;
                    }
                }
            }
        } else {
            // Select the ore category.
            for (int i = 0; i < OreColors.NAMES.length; i++) {
                int col = i / 5;
                int row = i % 5;

                int bx = x + 10 + col * 144;
                int by = y + 39 + row * 27;

                if (leftClick &&
                    inside(mx, my, bx, by, 139, 24)) {
                    selectedOre = i;
                    return true;
                }
            }

            // Select a color.
            for (int i = 0; i < PALETTE.length; i++) {
                int bx = x + 12 + i * 35;

                if (leftClick &&
                    inside(mx, my, bx, y + 200, 29, 26)) {
                    OreColors.setColor(
                        selectedOre, PALETTE[i]
                    );
                    return true;
                }
            }

            // Back to the main menu.
            if (leftClick &&
                inside(
                    mx, my,
                    x + 95, y + 230,
                    w - 190, 20
                )) {
                changePage(Page.MAIN);
                return true;
            }
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
