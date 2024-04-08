package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {

    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Constants.MOD_ID);

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SOLAR_COOKING_SERIALIZER = RECIPE_SERIALIZERS.register(Constants.SOLAR_COOKING_NAME, () -> SolarCookingRecipe.SERIALIZER);

    static {
        Constants.SOLAR_COOKING_RECIPE_TYPE = RECIPE_TYPES.register(Constants.SOLAR_COOKING_NAME, () -> new RecipeType<>() {});
    }
}
