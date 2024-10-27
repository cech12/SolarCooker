package de.cech12.solarcooker.jei;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.plugins.vanilla.cooking.AbstractCookingCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

public class SolarCookingCategory extends AbstractCookingCategory<SolarCookingRecipe> {

    public SolarCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, new RecipeType<>(Constants.id(Constants.SOLAR_COOKING_NAME), (Class<? extends RecipeHolder<SolarCookingRecipe>>) (Object) RecipeHolder.class),
                Constants.SOLAR_COOKER_BLOCK.get(), "gui.jei.category.smelting", (int) (200 * Services.CONFIG.getCookTimeFactor()));
    }

    @Override
    public boolean isHandled(@NotNull RecipeHolder<SolarCookingRecipe> recipeHolder) {
        return true;
    }

}
