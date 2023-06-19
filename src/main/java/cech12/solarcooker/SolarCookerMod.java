package cech12.solarcooker;

import cech12.solarcooker.client.SolarCookerBlockEntityRenderer;
import cech12.solarcooker.client.SolarCookerScreen;
import cech12.solarcooker.compat.TOPCompat;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.init.ModBlockEntityTypes;
import cech12.solarcooker.init.ModBlocks;
import cech12.solarcooker.init.ModItems;
import cech12.solarcooker.init.ModMenuTypes;
import cech12.solarcooker.init.ModRecipeTypes;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import static cech12.solarcooker.SolarCookerMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class SolarCookerMod {

    public static final String MOD_ID = "solarcooker";

    public SolarCookerMod() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModRecipeTypes.RECIPE_TYPES.register(eventBus);
        ModRecipeTypes.RECIPE_SERIALIZERS.register(eventBus);
        ModMenuTypes.MENU_TYPES.register(eventBus);
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));
        //The One Probe registration.
        if (ModList.get().isLoaded("theoneprobe")) {
            TOPCompat.register();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register((MenuType<SolarCookerContainer>) ModMenuTypes.SOLAR_COOKER.get(), SolarCookerScreen::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.SOLAR_COOKER.get(), SolarCookerBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.SOLAR_COOKER);
            event.accept(ModItems.REFLECTOR);
            event.accept(ModItems.SHINING_DIAMOND_BLOCK);
        }
    }
}
