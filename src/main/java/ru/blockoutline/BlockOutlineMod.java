package ru.blockoutline;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BlockOutlineMod implements ClientModInitializer {
    public static KeyBinding openSettingsKey;

    @Override
    public void onInitializeClient() {
        OutlineConfig.load();

        // Клавишу можно переназначить в Настройки -> Управление -> "Обводка блоков"
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.blockoutline.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.blockoutline"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.openScreen(new OutlineConfigScreen(null));
                }
            }
        });
    }
}
