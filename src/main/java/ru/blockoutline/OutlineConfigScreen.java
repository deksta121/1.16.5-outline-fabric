package ru.blockoutline;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.IntConsumer;

public class OutlineConfigScreen extends Screen {
    private static final int W = 224;

    private final Screen parent;
    private ColorSlider redSlider;
    private ColorSlider greenSlider;
    private ColorSlider blueSlider;
    private int top;

    public OutlineConfigScreen(Screen parent) {
        super(new TranslatableText("blockoutline.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - W / 2;
        top = Math.max(8, this.height / 2 - 104);

        int y = top + 44;
        redSlider = new ColorSlider(x, y, W, 20, "blockoutline.red", OutlineConfig.red, v -> OutlineConfig.red = v);
        greenSlider = new ColorSlider(x, y + 22, W, 20, "blockoutline.green", OutlineConfig.green, v -> OutlineConfig.green = v);
        blueSlider = new ColorSlider(x, y + 44, W, 20, "blockoutline.blue", OutlineConfig.blue, v -> OutlineConfig.blue = v);
        ColorSlider alphaSlider = new ColorSlider(x, y + 66, W, 20, "blockoutline.alpha", OutlineConfig.alpha, v -> OutlineConfig.alpha = v);
        this.addButton(redSlider);
        this.addButton(greenSlider);
        this.addButton(blueSlider);
        this.addButton(alphaSlider);

        int bw = 53;
        int gap = 4;
        int py = y + 94;
        addPreset(x, py, bw, "red", 255, 0, 0);
        addPreset(x + (bw + gap), py, bw, "green", 0, 255, 0);
        addPreset(x + (bw + gap) * 2, py, bw, "blue", 0, 0, 255);
        addPreset(x + (bw + gap) * 3, py, bw, "yellow", 255, 255, 0);
        py += 22;
        addPreset(x, py, bw, "white", 255, 255, 255);
        addPreset(x + (bw + gap), py, bw, "cyan", 0, 255, 255);
        addPreset(x + (bw + gap) * 2, py, bw, "pink", 255, 105, 180);
        addPreset(x + (bw + gap) * 3, py, bw, "black", 0, 0, 0);

        this.addButton(new ButtonWidget(x, py + 30, W, 20,
                new TranslatableText("blockoutline.done"), b -> this.onClose()));
    }

    private void addPreset(int x, int y, int w, String name, int r, int g, int b) {
        this.addButton(new ButtonWidget(x, y, w, 20,
                new TranslatableText("blockoutline.preset." + name), btn -> setColor(r, g, b)));
    }

    private void setColor(int r, int g, int b) {
        OutlineConfig.red = r;
        OutlineConfig.green = g;
        OutlineConfig.blue = b;
        redSlider.setInt(r);
        greenSlider.setInt(g);
        blueSlider.setInt(b);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, top, 0xFFFFFF);

        // Предпросмотр: серый прямоугольник с цветной рамкой
        int bx = this.width / 2 - 40;
        int by = top + 14;
        fill(matrices, bx, by, bx + 80, by + 22, OutlineConfig.argb());
        fill(matrices, bx + 2, by + 2, bx + 78, by + 20, 0xFF555555);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.client.openScreen(parent);
    }

    @Override
    public void removed() {
        OutlineConfig.save();
    }

    private static class ColorSlider extends SliderWidget {
        private final String key;
        private final IntConsumer onChange;

        ColorSlider(int x, int y, int width, int height, String key, int initial, IntConsumer onChange) {
            super(x, y, width, height, LiteralText.EMPTY, initial / 255.0);
            this.key = key;
            this.onChange = onChange;
            updateMessage();
        }

        private int getInt() {
            return (int) Math.round(this.value * 255.0);
        }

        void setInt(int v) {
            this.value = v / 255.0;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(new TranslatableText(key, getInt()));
        }

        @Override
        protected void applyValue() {
            onChange.accept(getInt());
        }
    }
}
