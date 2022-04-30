package cech12.solarcooker.jei;

import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.plugins.vanilla.cooking.AbstractCookingCategory;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SolarCookingCategory extends AbstractCookingCategory<SolarCookingRecipe> {

    public SolarCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, SolarCookerBlocks.SOLAR_COOKER, "gui.jei.category.smelting", (int) (200 * ServerConfig.COOK_TIME_FACTOR.get()));
    }

    @Override
    @Nonnull
    public RecipeType<SolarCookingRecipe> getRecipeType() {
        return new RecipeType<>(RecipeTypes.SOLAR_COOKING_ID, SolarCookingRecipe.class);
    }

    @Override
    @Nonnull
    @Deprecated //is deprecated since 9.5.0, getRecipeType() should be used instead
    public ResourceLocation getUid() {
        return RecipeTypes.SOLAR_COOKING_ID;
    }

    @Override
    @Nonnull
    @Deprecated //is deprecated since 9.5.0, getRecipeType() should be used instead
    public Class<? extends SolarCookingRecipe> getRecipeClass() {
        return SolarCookingRecipe.class;
    }
}
