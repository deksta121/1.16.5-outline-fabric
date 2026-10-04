package ru.blockoutline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Настройки цвета обводки. Хранятся в config/blockoutline.json.
 * Значения каналов: 0..255.
 */
public class OutlineConfig {
    public static int red = 255;
    public static int green = 0;
    public static int blue = 255;
    public static int alpha = 255;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static class Data {
        int red = 255;
        int green = 0;
        int blue = 255;
        int alpha = 255;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("blockoutline.json");
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    public static void load() {
        Path p = path();
        if (!Files.exists(p)) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            Data d = GSON.fromJson(reader, Data.class);
            if (d != null) {
                red = clamp(d.red);
                green = clamp(d.green);
                blue = clamp(d.blue);
                alpha = clamp(d.alpha);
            }
        } catch (Exception e) {
            System.err.println("[BlockOutline] Не удалось прочитать конфиг: " + e);
        }
    }

    public static void save() {
        Data d = new Data();
        d.red = red;
        d.green = green;
        d.blue = blue;
        d.alpha = alpha;
        try (Writer writer = Files.newBufferedWriter(path(), StandardCharsets.UTF_8)) {
            GSON.toJson(d, writer);
        } catch (IOException e) {
            System.err.println("[BlockOutline] Не удалось сохранить конфиг: " + e);
        }
    }

    public static int argb() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
