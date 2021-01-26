package cech12.solarcooker.jei;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@JeiPlugin
public class SolarCookerJEIPlugin implements IModPlugin {

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SolarCookerMod.MOD_ID, "plugin_" + SolarCookerMod.MOD_ID);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            RecipeManager manager = player.connection.getRecipeManager();
            registration.addRecipes(manager.getRecipes(RecipeTypes.SOLAR_COOKING).values(), RecipeTypes.SOLAR_COOKING_ID); //1.15

            if (ServerConfig.VANILLA_RECIPES_ENABLED.get()) {
                registration.addRecipes(manager.getRecipes(ServerConfig.getRecipeType()).values().stream() //1.15
                        .filter(recipe -> ServerConfig.isRecipeNotBlacklisted(recipe.getId()))
                        .map(recipe -> SolarCookingRecipe.convert((SolarCookingRecipe) recipe)) //1.15
                        .collect(Collectors.toList()), RecipeTypes.SOLAR_COOKING_ID);
            }
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new SolarCookingCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(SolarCookerBlocks.SOLAR_COOKER), RecipeTypes.SOLAR_COOKING_ID);
    }

}
