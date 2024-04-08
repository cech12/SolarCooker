package de.cech12.solarcooker.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Common configuration helper service interface.
 */
public interface IConfigHelper {

    /** Default value of the vanilla recipe enabled option */
    boolean VANILLA_RECIPES_ENABLED_DEFAULT = true;
    /** Config description of the vanilla recipe enabled option */
    String VANILLA_RECIPES_ENABLED_DESCRIPTION = "If enabled, the vanilla blasting, smelting, or smoking recipes are used by the solar cooker.";

    /** Default value of the vanilla recipe type option */
    RecipeType<? extends AbstractCookingRecipe> VANILLA_RECIPE_TYPE_DEFAULT = RecipeType.SMOKING;
    /** Config description of the vanilla recipe type option */
    String VANILLA_RECIPE_TYPE_DESCRIPTION = "Defines which vanilla recipes the solar cooker can use. Possible values: \"smoking\" (default), \"smelting\", \"blasting\", \"campfire_cooking\"";

    /** Default value of the cook time factor option */
    double COOK_TIME_FACTOR_DEFAULT = 4D;
    /** Config description of the cook time factor option */
    String COOK_TIME_FACTOR_DESCRIPTION = "Cook time factor of the solar cooker in relation to corresponding vanilla furnace. (i. e. 0.5 - half the time, 1.0 same time, 2.0 twice the time)";
    /** Minimal value of the cook time factor option */
    double COOK_TIME_FACTOR_MIN = 0D;
    /** Maximal value of the cook time factor option */
    double COOK_TIME_FACTOR_MAX = 100D;

    /** Default value of the max reflector time factor option */
    double MAX_REFLECTOR_TIME_FACTOR_DEFAULT = 0.25D;
    /** Config description of the max reflector time factor option */
    String MAX_REFLECTOR_TIME_FACTOR_DESCRIPTION = "Speed factor when all 4 reflectors are placed next to the solar cooker. (i. e. 0.5 - half the time, 1.0 same time)";
    /** Minimal value of the max reflector time factor option */
    double MAX_REFLECTOR_TIME_FACTOR_MIN = 0D;
    /** Maximal value of the max reflector time factor option */
    double MAX_REFLECTOR_TIME_FACTOR_MAX = 1D;

    /** Default value of the recipe blocked list option */
    String RECIPE_BLOCKED_LIST_DEFAULT = "";
    /** Config description of the recipe blocked list option */
    String RECIPE_BLOCKED_LIST_DESCRIPTION = "A comma separated list of all vanilla recipes that should not be used by the brick furnaces. Example: \"baked_potato,baked_potato_from_smoking,othermod:other_baked_food\"";

    /**
     * Initialization method for the Service implementations.
     */
    void init();

    /**
     * Gets the configured value of the vanilla recipe enabled option.
     *
     * @return configured value of the vanilla recipe enabled option
     */
    boolean areVanillaRecipesEnabled();

    /**
     * Gets the configured value of the vanilla recipe type option.
     *
     * @return configured value of the vanilla recipe type option
     */
    RecipeType<? extends AbstractCookingRecipe> getRecipeType();

    /**
     * Gets the configured value of the cook time factor option.
     *
     * @return configured value of the cook time factor option
     */
    double getCookTimeFactor();

    /**
     * Gets the configured value of the max reflector time factor option.
     *
     * @return configured value of the max reflector time factor option
     */
    double getMaxReflectorTimeFactor();

    /**
     * Gets the configured value of the recipe blocked list option.
     *
     * @return configured value of the recipe blocked list option
     */
    String getRecipeBlockedList();

    /**
     * Checks if the given ResourceLocation is allowed taking into account the recipe blocked list.
     * @param id ResourceLocation of recipe
     * @return true, if recipe is allowed, else false
     */
    default boolean isRecipeAllowed(final ResourceLocation id) {
        String configValue = getRecipeBlockedList().trim();
        if (!configValue.isEmpty()) {
            String[] ids = configValue.split(",");
            if (ids.length < 1) {
                return !(new ResourceLocation(configValue).equals(id));
            } else {
                for (String recipeId : ids) {
                    if (new ResourceLocation(recipeId.trim()).equals(id)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}