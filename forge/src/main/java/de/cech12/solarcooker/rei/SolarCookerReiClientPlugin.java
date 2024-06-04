package de.cech12.solarcooker.rei;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;

import java.util.Objects;

@SuppressWarnings("unused")
@REIPluginClient
public class SolarCookerReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SolarCookingReiDisplayCategory());
        registry.addWorkstations(SolarCookingReiDisplayCategory.ID, EntryStacks.of(Constants.SOLAR_COOKER_BLOCK.get()));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(SolarCookingReiDisplayCategory.ID, SolarCookingReiDisplay.getSerializer());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(SolarCookingRecipe.class, Constants.SOLAR_COOKING_RECIPE_TYPE.get(), SolarCookingReiDisplay::new);
        if (Services.CONFIG.areVanillaRecipesEnabled()) {
            registry.registerRecipeFiller(AbstractCookingRecipe.class, type -> Objects.equals(Services.CONFIG.getRecipeType(), type),
                    recipeHolder -> Services.CONFIG.isRecipeAllowed(recipeHolder.id()), SolarCookingReiDisplay::new);
        }
    }

}
