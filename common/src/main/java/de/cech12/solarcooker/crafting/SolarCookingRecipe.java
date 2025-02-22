package de.cech12.solarcooker.crafting;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;

import javax.annotation.Nonnull;

public class SolarCookingRecipe extends AbstractCookingRecipe {

    public static final RecipeSerializer<SolarCookingRecipe> SERIALIZER = new AbstractCookingRecipe.Serializer<>(SolarCookingRecipe::new, 200);

    public SolarCookingRecipe(String p_i50031_2_, CookingBookCategory category, Ingredient p_i50031_3_, ItemStack p_i50031_4_, float p_i50031_5_, int p_i50031_6_) {
        super(p_i50031_2_, category, p_i50031_3_, p_i50031_4_, p_i50031_5_, p_i50031_6_);
    }

    public static SolarCookingRecipe convert(@Nonnull AbstractCookingRecipe recipe, RegistryAccess registryAccess) {
        return new SolarCookingRecipe(recipe.group(), recipe.category(), recipe.input(), recipe.assemble(new SingleRecipeInput(new ItemStack(recipe.input().items().getFirst())), registryAccess), recipe.experience(), (int) (recipe.cookingTime() * Services.CONFIG.getCookTimeFactor()));
    }

    @Override
    @Nonnull
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return Constants.SOLAR_COOKING_RECIPE_TYPE.get();
    }

    @Override
    @Nonnull
    public Item furnaceIcon() {
        return Constants.SOLAR_COOKER_BLOCK.get().asItem();
    }

    @Override
    @Nonnull
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    @Nonnull
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }
}
