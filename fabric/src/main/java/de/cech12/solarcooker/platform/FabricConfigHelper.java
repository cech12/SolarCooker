package de.cech12.solarcooker.platform;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.platform.services.IConfigHelper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * The config service implementation for Fabric.
 */
@Config(name = Constants.MOD_ID)
public class FabricConfigHelper implements ConfigData, IConfigHelper {

    @ConfigEntry.Gui.Tooltip(count = 4)
    public boolean VANILLA_RECIPES_ENABLED = VANILLA_RECIPES_ENABLED_DEFAULT;

    @ConfigEntry.Gui.Tooltip(count = 4)
    public String VANILLA_RECIPE_TYPE = BuiltInRegistries.RECIPE_TYPE.getKey(VANILLA_RECIPE_TYPE_DEFAULT).getPath();

    @ConfigEntry.Gui.Tooltip(count = 5)
    @ConfigEntry.BoundedDiscrete(min = (long) (COOK_TIME_FACTOR_MIN * 100), max = (long) (COOK_TIME_FACTOR_MAX * 100))
    public long COOK_TIME_FACTOR = (long) (COOK_TIME_FACTOR_DEFAULT * 100);

    @ConfigEntry.Gui.Tooltip(count = 5)
    @ConfigEntry.BoundedDiscrete(min = (long) (MAX_REFLECTOR_TIME_FACTOR_MIN * 100), max = (long) (MAX_REFLECTOR_TIME_FACTOR_MAX * 100))
    public long MAX_REFLECTOR_TIME_FACTOR = (long) (MAX_REFLECTOR_TIME_FACTOR_DEFAULT * 100);

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String RECIPE_BLOCKED_LIST = RECIPE_BLOCKED_LIST_DEFAULT;

    @Override
    public void init() {
        AutoConfig.register(FabricConfigHelper.class, Toml4jConfigSerializer::new);
    }

    private FabricConfigHelper getConfig() {
        return AutoConfig.getConfigHolder(FabricConfigHelper.class).getConfig();
    }

    @Override
    public boolean areVanillaRecipesEnabled() {
        return getConfig().VANILLA_RECIPES_ENABLED;
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getRecipeType() {
        return switch (getConfig().VANILLA_RECIPE_TYPE) {
            case "smoking" -> RecipeType.SMOKING;
            case "smelting" -> RecipeType.SMELTING;
            case "campfire_cooking" -> RecipeType.CAMPFIRE_COOKING;
            case "blasting" -> RecipeType.BLASTING;
            default -> VANILLA_RECIPE_TYPE_DEFAULT;
        };
    }

    @Override
    public double getCookTimeFactor() {
        return getConfig().COOK_TIME_FACTOR / 100D;
    }

    @Override
    public double getMaxReflectorTimeFactor() {
        return getConfig().MAX_REFLECTOR_TIME_FACTOR / 100D;
    }

    @Override
    public String getRecipeBlockedList() {
        return getConfig().RECIPE_BLOCKED_LIST;
    }

}
