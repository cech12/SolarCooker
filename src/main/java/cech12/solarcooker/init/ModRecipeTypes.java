package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.crafting.SolarCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {

    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, SolarCookerMod.MOD_ID);

    public static RegistryObject<RecipeType<SolarCookingRecipe>> SOLAR_COOKING = RECIPE_TYPES.register("solarcooking", () -> new RecipeType<>() {});

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SolarCookerMod.MOD_ID);

    public static RegistryObject<RecipeSerializer<?>> SOLAR_COOKING_SERIALIZER = RECIPE_SERIALIZERS.register("solarcooking", () -> SolarCookingRecipe.SERIALIZER);
}
