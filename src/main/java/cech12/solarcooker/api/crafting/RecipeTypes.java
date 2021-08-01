package cech12.solarcooker.api.crafting;

import cech12.solarcooker.SolarCookerMod;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class RecipeTypes {

    public final static ResourceLocation SOLAR_COOKING_ID = new ResourceLocation(SolarCookerMod.MOD_ID, "solarcooking");

    public static RecipeType<? extends AbstractCookingRecipe> SOLAR_COOKING;

}
