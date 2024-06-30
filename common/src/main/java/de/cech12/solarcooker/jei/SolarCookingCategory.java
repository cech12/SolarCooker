package de.cech12.solarcooker.jei;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.plugins.vanilla.cooking.AbstractCookingCategory;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;

public class SolarCookingCategory extends AbstractCookingCategory<SolarCookingRecipe> {

    public SolarCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, Constants.SOLAR_COOKER_BLOCK.get(), "gui.jei.category.smelting", (int) (200 * Services.CONFIG.getCookTimeFactor()));
    }

    @Override
    @Nonnull
    public RecipeType<RecipeHolder<SolarCookingRecipe>> getRecipeType() {
        Class<? extends RecipeHolder<SolarCookingRecipe>> holderClass = (Class<? extends RecipeHolder<SolarCookingRecipe>>) (Object) RecipeHolder.class;
        return new RecipeType<>(Constants.id(Constants.SOLAR_COOKING_NAME), holderClass);
    }

}
