package cech12.solarcooker.jei;

import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import cech12.solarcooker.init.ModBlocks;
import cech12.solarcooker.init.ModRecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.plugins.vanilla.cooking.AbstractCookingCategory;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;

public class SolarCookingCategory extends AbstractCookingCategory<SolarCookingRecipe> {

    public SolarCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, ModBlocks.SOLAR_COOKER.get(), "gui.jei.category.smelting", (int) (200 * ServerConfig.COOK_TIME_FACTOR.get()));
    }

    @Override
    @Nonnull
    public RecipeType<RecipeHolder<SolarCookingRecipe>> getRecipeType() {
        Class<? extends RecipeHolder<SolarCookingRecipe>> holderClass = (Class<? extends RecipeHolder<SolarCookingRecipe>>) (Object) RecipeHolder.class;
        return new RecipeType<>(ModRecipeTypes.SOLAR_COOKING.getId(), holderClass);
    }

}
