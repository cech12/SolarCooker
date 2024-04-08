package de.cech12.solarcooker.crafting;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class SolarCookingRecipe extends AbstractCookingRecipe {

    public static final SimpleCookingSerializer<SolarCookingRecipe> SERIALIZER = new SimpleCookingSerializer<>(SolarCookingRecipe::new, 200);

    public SolarCookingRecipe(String p_i50031_2_, CookingBookCategory category, Ingredient p_i50031_3_, ItemStack p_i50031_4_, float p_i50031_5_, int p_i50031_6_) {
        super(Constants.SOLAR_COOKING_RECIPE_TYPE.get(), p_i50031_2_, category, p_i50031_3_, p_i50031_4_, p_i50031_5_, p_i50031_6_);
    }

    public static SolarCookingRecipe convert(@Nonnull AbstractCookingRecipe recipe, RegistryAccess registryAccess) {
        return new SolarCookingRecipe(recipe.getGroup(), recipe.category(), recipe.getIngredients().get(0), recipe.getResultItem(registryAccess), recipe.getExperience(), (int) (recipe.getCookingTime() * Services.CONFIG.getCookTimeFactor()));
    }

    @Override
    @Nonnull
    public ItemStack getToastSymbol() {
        return new ItemStack(Constants.SOLAR_COOKER_BLOCK.get());
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}
