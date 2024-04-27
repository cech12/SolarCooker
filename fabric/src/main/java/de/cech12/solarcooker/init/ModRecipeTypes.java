package de.cech12.solarcooker.init;

import de.cech12.solarcooker.CommonLoader;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {

    private static final RecipeType<SolarCookingRecipe> SOLAR_COOKING_RECIPE_TYPE = registerRecipe(Constants.SOLAR_COOKING_NAME);

    private static final RecipeSerializer<?> SOLAR_COOKING_SERIALIZER = registerSerializer(Constants.SOLAR_COOKING_NAME, SolarCookingRecipe.SERIALIZER);

    static {
        Constants.SOLAR_COOKING_RECIPE_TYPE = () -> SOLAR_COOKING_RECIPE_TYPE;
    }

    public static void init() {}

    private static <T extends AbstractCookingRecipe> RecipeType<T> registerRecipe(String name) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, CommonLoader.id(name), new RecipeType<>() {});
    }

    private static <T extends AbstractCookingRecipe> RecipeSerializer<T> registerSerializer(String name, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, CommonLoader.id(name), serializer);
    }
}
