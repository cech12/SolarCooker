package cech12.solarcooker.crafting;

import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.init.ModBlocks;
import cech12.solarcooker.init.ModRecipeTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SolarCookingRecipe extends AbstractCookingRecipe {

    public static final SimpleCookingSerializer<SolarCookingRecipe> SERIALIZER = new SimpleCookingSerializer<>(SolarCookingRecipe::new, 200);

    public SolarCookingRecipe(ResourceLocation p_i50031_1_, String p_i50031_2_, CookingBookCategory category, Ingredient p_i50031_3_, ItemStack p_i50031_4_, float p_i50031_5_, int p_i50031_6_) {
        super(ModRecipeTypes.SOLAR_COOKING.get(), p_i50031_1_, p_i50031_2_, category, p_i50031_3_, p_i50031_4_, p_i50031_5_, p_i50031_6_);
    }

    public static SolarCookingRecipe convert(@Nonnull AbstractCookingRecipe recipe, RegistryAccess registryAccess) {
        return new SolarCookingRecipe(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getIngredients().get(0), recipe.getResultItem(registryAccess), recipe.getExperience(), (int) (recipe.getCookingTime() * ServerConfig.COOK_TIME_FACTOR.get()));
    }

    @Override
    @Nonnull
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.SOLAR_COOKER.get());
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}
