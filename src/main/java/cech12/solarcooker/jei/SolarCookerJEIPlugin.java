package cech12.solarcooker.jei;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import cech12.solarcooker.init.ModBlocks;
import cech12.solarcooker.init.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@JeiPlugin
public class SolarCookerJEIPlugin implements IModPlugin {

    private static SolarCookingCategory solarCookingCategory;

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SolarCookerMod.MOD_ID, "plugin_" + SolarCookerMod.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        solarCookingCategory = new SolarCookingCategory(guiHelper);
        registration.addRecipeCategories(solarCookingCategory);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            RecipeManager manager = player.connection.getRecipeManager();
            registration.addRecipes(solarCookingCategory.getRecipeType(), manager.getAllRecipesFor(ModRecipeTypes.SOLAR_COOKING.get()));

            if (ServerConfig.VANILLA_RECIPES_ENABLED.get()) {
                registration.addRecipes(solarCookingCategory.getRecipeType(), manager.getAllRecipesFor(ServerConfig.getRecipeType()).stream()
                        .filter(recipe -> ServerConfig.isRecipeNotBlacklisted(recipe.getId()))
                        .map(SolarCookingRecipe::convert)
                        .collect(Collectors.toList()));
            }
        }
    }


    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SOLAR_COOKER.get()), solarCookingCategory.getRecipeType());
    }

}
