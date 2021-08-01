package cech12.solarcooker;

import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.api.blockentity.SolarCookerBlockEntities;
import cech12.solarcooker.client.SolarCookerScreen;
import cech12.solarcooker.client.SolarCookerBlockEntityRenderer;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import cech12.solarcooker.inventory.SolarCookerContainer;
import cech12.solarcooker.tileentity.SolarCookerBlockEntity;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Registry;
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
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().register(SOLAR_COOKER);
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        RecipeTypes.SOLAR_COOKING = Registry.register(Registry.RECIPE_TYPE,
                RecipeTypes.SOLAR_COOKING_ID,
                new RecipeType<SolarCookingRecipe>() {});
        ForgeRegistries.RECIPE_SERIALIZERS.register(SolarCookingRecipe.SERIALIZER);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register((MenuType<SolarCookerContainer>) SOLAR_COOKER, SolarCookerScreen::new);
        BlockEntityRenderers.register((BlockEntityType<SolarCookerBlockEntity>) SolarCookerBlockEntities.SOLAR_COOKER, SolarCookerBlockEntityRenderer::new);
    }

}
