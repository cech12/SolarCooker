package de.cech12.solarcooker;

import de.cech12.solarcooker.client.SolarCookerBlockEntityRenderer;
import de.cech12.solarcooker.client.SolarCookerScreen;
import de.cech12.solarcooker.init.ModBlockEntityTypes;
import de.cech12.solarcooker.init.ModBlocks;
import de.cech12.solarcooker.init.ModItems;
import de.cech12.solarcooker.init.ModMenuTypes;
import de.cech12.solarcooker.init.ModRecipeTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ForgeSolarCookerMod {

    public ForgeSolarCookerMod(FMLJavaModLoadingContext context) {
        final BusGroup eventBus = context.getModBusGroup();
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModRecipeTypes.RECIPE_TYPES.register(eventBus);
        ModRecipeTypes.RECIPE_SERIALIZERS.register(eventBus);
        ModMenuTypes.MENU_TYPES.register(eventBus);
        //Config
        CommonLoader.init();
    }

    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register(Constants.SOLAR_COOKER_MENU_TYPE.get(), SolarCookerScreen::new);
        BlockEntityRenderers.register(Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SolarCookerBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(Constants.SOLAR_COOKER_ITEM);
            event.accept(Constants.REFLECTOR_ITEM);
            event.accept(Constants.SHINING_DIAMOND_BLOCK_ITEM);
        }
    }
}
