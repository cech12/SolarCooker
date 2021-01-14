package cech12.solarcooker.api.crafting;

import cech12.solarcooker.SolarCookerMod;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public class RecipeTypes {

    public final static ResourceLocation SOLAR_COOKING_ID = new ResourceLocation(SolarCookerMod.MOD_ID, "solarcooking");

    public static IRecipeType<? extends AbstractCookingRecipe> SOLAR_COOKING;

}
