package de.cech12.solarcooker.jei;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.FabricSolarCookerMod;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@JeiPlugin
public class SolarCookerJEIPlugin implements IModPlugin {

    private static SolarCookingCategory solarCookingCategory;

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return Constants.id("plugin_" + Constants.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        solarCookingCategory = new SolarCookingCategory(guiHelper);
        registration.addRecipeCategories(solarCookingCategory);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        MinecraftServer server = FabricSolarCookerMod.getServer();
        if (server != null) {
            RecipeMap recipeMap = server.getRecipeManager().recipes;
            registration.addRecipes(solarCookingCategory.getRecipeType(), recipeMap.byType(Constants.SOLAR_COOKING_RECIPE_TYPE.get()).stream().toList());

            if (Services.CONFIG.areVanillaRecipesEnabled()) {
                registration.addRecipes(solarCookingCategory.getRecipeType(), recipeMap.byType(Services.CONFIG.getRecipeType()).stream()
                        .filter(recipe -> Services.CONFIG.isRecipeAllowed(recipe.id().location()))
                        .map(recipe -> new RecipeHolder<>(recipe.id(), SolarCookingRecipe.convert(recipe.value(), server.registryAccess())))
                        .collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(solarCookingCategory.getRecipeType(), new ItemStack(Constants.SOLAR_COOKER_BLOCK.get()));
    }

}
