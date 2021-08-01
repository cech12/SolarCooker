package cech12.solarcooker.crafting;

import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.config.ServerConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SolarCookingRecipe extends AbstractCookingRecipe {

    public static final SimpleCookingSerializer<SolarCookingRecipe> SERIALIZER = new SimpleCookingSerializer<>(SolarCookingRecipe::new, 200);
    static {
        SERIALIZER.setRegistryName(RecipeTypes.SOLAR_COOKING_ID);
    }

    public SolarCookingRecipe(ResourceLocation p_i50031_1_, String p_i50031_2_, Ingredient p_i50031_3_, ItemStack p_i50031_4_, float p_i50031_5_, int p_i50031_6_) {
        super(RecipeTypes.SOLAR_COOKING, p_i50031_1_, p_i50031_2_, p_i50031_3_, p_i50031_4_, p_i50031_5_, p_i50031_6_);
    }

    public static SolarCookingRecipe convert(@Nonnull AbstractCookingRecipe recipe) {
        return new SolarCookingRecipe(recipe.getId(), recipe.getGroup(), recipe.getIngredients().get(0), recipe.getResultItem(), recipe.getExperience(), (int) (recipe.getCookingTime() * ServerConfig.COOK_TIME_FACTOR.get()));
    }

    @Override
    @Nonnull
    public ItemStack getToastSymbol() {
        return new ItemStack(SolarCookerBlocks.SOLAR_COOKER);
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}
