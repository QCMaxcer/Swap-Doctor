package cn.swapdoctor.client;

import cn.swapdoctor.config.HudPosition;
import cn.swapdoctor.config.SwapDoctorConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/** In-game editor for the two normalized HUD positions. */
public final class SwapDoctorHudEditorScreen extends Screen {
    private static final int DATA_WIDTH = 190;
    private static final int DATA_HEIGHT = 18;
    private final Screen parent;
    private final SwapDoctorConfig working;
    private Target dragging;
    private double dragOffsetX;
    private double dragOffsetY;

    private enum Target { DATA, SCALE }

    public SwapDoctorHudEditorScreen(Screen parent) {
        super(Component.translatable("screen.swapdoctor.editor.title"));
        this.parent = parent;
        this.working = SwapDoctorClient.config().copy();
    }

    @Override
    protected void init() {
        int buttonY = height - 28;
        addRenderableWidget(Button.builder(Component.translatable("button.swapdoctor.save"), button -> saveAndClose())
                .bounds(width / 2 - 154, buttonY, 98, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.swapdoctor.cancel"), button -> cancelAndClose())
                .bounds(width / 2 - 50, buttonY, 98, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.swapdoctor.reset"), button -> resetLayout())
                .bounds(width / 2 + 54, buttonY, 98, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, 16, 0xFFFFFFFF);
        graphics.drawCenteredString(font, Component.translatable("screen.swapdoctor.editor.hint"), width / 2, 32, 0xFFB7C1D2);

        int dataX = HudLayout.screenX(working.hudPosition(), width);
        int dataY = HudLayout.screenY(working.hudPosition(), height);
        graphics.fill(dataX, dataY, dataX + DATA_WIDTH, dataY + DATA_HEIGHT, 0xB0101420);
        graphics.drawString(font, Component.translatable("swapdoctor.editor.preview_data"), dataX + 6, dataY + 5, 0xFF55FF72, false);

        int scaleX = HudLayout.screenX(working.scalePosition(), width);
        int scaleY = HudLayout.screenY(working.scalePosition(), height);
        int scaleWidth = working.scaleWidth;
        float lengthMultiplier = working.scaleLengthMultiplier;
        float thicknessMultiplier = working.scaleThicknessMultiplier;
        int scaleHeight = SwapDoctorScaleHud.height(lengthMultiplier);
        int baseline = SwapDoctorScaleHud.baselineY(scaleY, lengthMultiplier);
        String minLabel = SwapDoctorScaleHud.formatScaleValue(-working.maxDelayMs);
        String maxLabel = SwapDoctorScaleHud.formatScaleValue(working.maxDelayMs);
        int left = SwapDoctorScaleHud.scaleLineLeft(scaleX, scaleWidth, font.width(minLabel), working.showScaleValues);
        int right = SwapDoctorScaleHud.scaleLineRight(scaleX, scaleWidth, font.width(maxLabel), working.showScaleValues);
        int strokeWidth = SwapDoctorScaleHud.strokeWidth(thicknessMultiplier);
        int baselineTop = baseline - strokeWidth / 2;
        graphics.fill(left, baselineTop, right + 1, baselineTop + strokeWidth, 0xFF647083);
        for (int i = 0; i < SwapDoctorScaleHud.TICK_COUNT; i++) {
            int tickX = SwapDoctorScaleHud.tickX(i, left, right);
            graphics.fill(tickX, SwapDoctorScaleHud.tickTop(baseline, i, lengthMultiplier),
                    tickX + strokeWidth,
                    SwapDoctorScaleHud.tickBottomExclusive(baseline, i, lengthMultiplier), 0xFFD9DFEA);
        }
        int pointerX = (left + right) / 2;
        int pointerWidth = SwapDoctorScaleHud.pointerWidth(thicknessMultiplier);
        int pointerStart = SwapDoctorScaleHud.pointerLeft(pointerX, thicknessMultiplier);
        graphics.fill(pointerStart, SwapDoctorScaleHud.pointerTop(baseline, lengthMultiplier),
                pointerStart + pointerWidth,
                SwapDoctorScaleHud.pointerBottomExclusive(baseline, lengthMultiplier), 0xFF55FF72);
        if (working.showScaleValues) {
            int labelY = baseline - font.lineHeight / 2;
            graphics.drawString(font, minLabel, scaleX, labelY, SwapDoctorScaleHud.SCALE_COLOR, false);
            graphics.drawString(font, maxLabel, scaleX + scaleWidth - font.width(maxLabel), labelY,
                    SwapDoctorScaleHud.SCALE_COLOR, false);
        }

        int outline = dragging == Target.DATA ? 0xFF55FF72 : 0xFF506079;
        drawOutline(graphics, dataX, dataY, DATA_WIDTH, DATA_HEIGHT, outline);
        outline = dragging == Target.SCALE ? 0xFF55FF72 : 0xFF506079;
        drawOutline(graphics, scaleX, scaleY, scaleWidth, scaleHeight, outline);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (super.mouseClicked(event, doubleClick)) return true;
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;
        Target target = targetAt(mouseX, mouseY);
        if (target == null) return false;
        dragging = target;
        HudPosition position = target == Target.DATA ? working.hudPosition() : working.scalePosition();
        int x = HudLayout.screenX(position, width);
        int y = HudLayout.screenY(position, height);
        dragOffsetX = mouseX - x;
        dragOffsetY = mouseY - y;
        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        double mouseX = event.x();
        double mouseY = event.y();
        if (dragging == null) return super.mouseDragged(event, dragX, dragY);
        int hudWidth = dragging == Target.DATA ? DATA_WIDTH : working.scaleWidth;
        int hudHeight = dragging == Target.DATA ? DATA_HEIGHT : SwapDoctorScaleHud.height(working.scaleLengthMultiplier);
        float normalizedX = HudLayout.clampX((float) ((mouseX - dragOffsetX) / width), hudWidth, width);
        float normalizedY = HudLayout.clampY((float) ((mouseY - dragOffsetY) / height), hudHeight, height);
        if (dragging == Target.DATA) working.setHudPosition(new HudPosition(normalizedX, normalizedY));
        else working.setScalePosition(new HudPosition(normalizedX, normalizedY));
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) dragging = null;
        return super.mouseReleased(event);
    }

    @Override
    public void onClose() {
        cancelAndClose();
    }

    private Target targetAt(double mouseX, double mouseY) {
        int dataX = HudLayout.screenX(working.hudPosition(), width);
        int dataY = HudLayout.screenY(working.hudPosition(), height);
        if (mouseX >= dataX && mouseX <= dataX + DATA_WIDTH && mouseY >= dataY && mouseY <= dataY + DATA_HEIGHT) {
            return Target.DATA;
        }
        int scaleX = HudLayout.screenX(working.scalePosition(), width);
        int scaleY = HudLayout.screenY(working.scalePosition(), height);
        if (mouseX >= scaleX && mouseX <= scaleX + working.scaleWidth && mouseY >= scaleY
                && mouseY <= scaleY + SwapDoctorScaleHud.height(working.scaleLengthMultiplier)) return Target.SCALE;
        return null;
    }

    private void saveAndClose() {
        SwapDoctorClient.setHudPosition(working.hudPosition());
        SwapDoctorClient.setScalePosition(working.scalePosition());
        minecraft.setScreen(parent);
    }

    private void cancelAndClose() {
        minecraft.setScreen(parent);
    }

    private void resetLayout() {
        SwapDoctorConfig defaults = new SwapDoctorConfig();
        working.setHudPosition(defaults.hudPosition());
        working.setScalePosition(defaults.scalePosition());
    }

    private static void drawOutline(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + 1, color);
        graphics.fill(x, y + height - 1, x + width, y + height, color);
        graphics.fill(x, y, x + 1, y + height, color);
        graphics.fill(x + width - 1, y, x + width, y + height, color);
    }
}
