package de.cech12.solarcooker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class FabricSolarCookerClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(Constants.SOLAR_COOKER_MENU_TYPE.get(), SolarCookerScreen::new);

        BlockEntityRenderers.register(Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SolarCookerBlockEntityRenderer::new);

        BuiltinItemRendererRegistry.INSTANCE.register(Constants.SOLAR_COOKER_ITEM.get(), new BuiltinItemRendererRegistry.DynamicItemRenderer() {
            private SolarCookerBlockEntity blockEntity;

            @Override
            public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
                if (blockEntity == null) {
                    blockEntity = new SolarCookerBlockEntity(BlockPos.ZERO, Constants.SOLAR_COOKER_BLOCK.get().defaultBlockState());
                }
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, matrices, vertexConsumers, light, overlay);
            }
        });
    }

}
