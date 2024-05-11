package de.cech12.solarcooker;

import de.cech12.solarcooker.client.SolarCookerBlockEntityRenderer;
import de.cech12.solarcooker.client.SolarCookerScreen;
import de.cech12.solarcooker.compat.TOPCompat;
import de.cech12.solarcooker.init.ModBlockEntityTypes;
import de.cech12.solarcooker.init.ModBlocks;
import de.cech12.solarcooker.init.ModItems;
import de.cech12.solarcooker.init.ModMenuTypes;
import de.cech12.solarcooker.init.ModRecipeTypes;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class NeoForgeSolarCookerMod {

    public NeoForgeSolarCookerMod(IEventBus eventBus) {
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModRecipeTypes.RECIPE_TYPES.register(eventBus);
        ModRecipeTypes.RECIPE_SERIALIZERS.register(eventBus);
        ModMenuTypes.MENU_TYPES.register(eventBus);
        //Config
        CommonLoader.init();
        //The One Probe registration.
        if (ModList.get().isLoaded("theoneprobe")) {
            TOPCompat.register();
        }
    }

    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register((MenuType<SolarCookerContainer>) Constants.SOLAR_COOKER_MENU_TYPE.get(), SolarCookerScreen::new);
        BlockEntityRenderers.register(Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SolarCookerBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SidedInvWrapper::new);
    }

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(Constants.SOLAR_COOKER_ITEM.get());
            event.accept(Constants.REFLECTOR_ITEM.get());
            event.accept(Constants.SHINING_DIAMOND_BLOCK_ITEM.get());
        }
    }
}
