package de.cech12.solarcooker.jei;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

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
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            RecipeManager manager = player.connection.getRecipeManager();
            registration.addRecipes(solarCookingCategory.getRecipeType(), manager.getAllRecipesFor(Constants.SOLAR_COOKING_RECIPE_TYPE.get()));

            if (Services.CONFIG.areVanillaRecipesEnabled()) {
                registration.addRecipes(solarCookingCategory.getRecipeType(), manager.getAllRecipesFor(Services.CONFIG.getRecipeType()).stream()
                        .filter(recipe -> Services.CONFIG.isRecipeAllowed(recipe.id()))
                        .map(recipe -> new RecipeHolder<>(recipe.id(), SolarCookingRecipe.convert(recipe.value(), player.level().registryAccess())))
                        .collect(Collectors.toList()));
            }
        }
    }


    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Constants.SOLAR_COOKER_BLOCK.get()), solarCookingCategory.getRecipeType());
    }

}
