package cech12.solarcooker;

import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.client.SolarCookerScreen;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import static cech12.solarcooker.SolarCookerMod.MOD_ID;
import static cech12.solarcooker.api.inventory.ContainerTypes.SOLAR_COOKER;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class SolarCookerMod {

    public static final String MOD_ID = "solarcooker";

    public SolarCookerMod() {
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(SOLAR_COOKER);
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        RecipeTypes.SOLAR_COOKING = Registry.register(Registry.RECIPE_TYPE,
                RecipeTypes.SOLAR_COOKING_ID,
                new IRecipeType<SolarCookingRecipe>() {});
        ForgeRegistries.RECIPE_SERIALIZERS.register(SolarCookingRecipe.SERIALIZER);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        ScreenManager.registerFactory((ContainerType<SolarCookerContainer>) SOLAR_COOKER, SolarCookerScreen::new);
    }

}
