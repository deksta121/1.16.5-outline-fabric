package ru.blockoutline.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.blockoutline.OutlineConfig;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    /**
     * Подменяем цвет (r, g, b, a) в вызове отрисовки обводки блока.
     * Индексы аргументов drawShapeOutline: 6 = red, 7 = green, 8 = blue, 9 = alpha.
     */
    @ModifyArgs(
            method = "drawBlockOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;drawShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V"
            )
    )
    private void blockoutline$recolor(Args args) {
        args.set(6, OutlineConfig.red / 255.0F);
        args.set(7, OutlineConfig.green / 255.0F);
        args.set(8, OutlineConfig.blue / 255.0F);
        args.set(9, OutlineConfig.alpha / 255.0F);
    }
}
