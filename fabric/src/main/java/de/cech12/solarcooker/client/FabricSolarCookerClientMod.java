package de.cech12.solarcooker.client;

import de.cech12.solarcooker.Constants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FabricSolarCookerClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(Constants.SOLAR_COOKER_MENU_TYPE.get(), SolarCookerScreen::new);

        BlockEntityRenderers.register(Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SolarCookerBlockEntityRenderer::new);
        ModelLayerRegistry.registerModelLayer(SolarCookerBlockEntityRenderer.MODEL_LAYER_LOCATION, SolarCookerModel::createBodyLayer);
    }

}
