package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {

    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Constants.MOD_ID);

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static RegistryObject<RecipeSerializer<?>> SOLAR_COOKING_SERIALIZER = RECIPE_SERIALIZERS.register(Constants.SOLAR_COOKING_NAME, () -> SolarCookingRecipe.SERIALIZER);

    static {
        Constants.SOLAR_COOKING_RECIPE_TYPE = RECIPE_TYPES.register(Constants.SOLAR_COOKING_NAME, () -> new RecipeType<>() {});
    }
}
