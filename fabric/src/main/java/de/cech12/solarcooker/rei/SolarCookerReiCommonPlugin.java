package de.cech12.solarcooker.rei;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

@SuppressWarnings("unused")
public class SolarCookerReiCommonPlugin implements REICommonPlugin {

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(SolarCookingRecipe.class).filterType(Constants.SOLAR_COOKING_RECIPE_TYPE.get()).fill(SolarCookingReiDisplay::new);
        if (Services.CONFIG.areVanillaRecipesEnabled()) {
            registry.beginRecipeFiller(AbstractCookingRecipe.class)
                    .filterType((RecipeType<? super AbstractCookingRecipe>) Services.CONFIG.getRecipeType())
                    .filter(recipeHolder -> Services.CONFIG.isRecipeAllowed(recipeHolder.id().location()))
                    .fill(SolarCookingReiDisplay::new);
        }
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(Constants.id(Constants.SOLAR_COOKING_NAME), SolarCookingReiDisplay.SERIALIZER);
    }

}
