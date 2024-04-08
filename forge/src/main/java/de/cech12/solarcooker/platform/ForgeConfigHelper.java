package de.cech12.solarcooker.platform;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.platform.services.IConfigHelper;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;

/**
 * The config service implementation for Forge.
 */
public class ForgeConfigHelper implements IConfigHelper {

    private static final ForgeConfigSpec SERVER_CONFIG;

    public static final ForgeConfigSpec.BooleanValue VANILLA_RECIPES_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> VANILLA_RECIPE_TYPE;
    public static final ForgeConfigSpec.DoubleValue COOK_TIME_FACTOR;
    public static final ForgeConfigSpec.DoubleValue MAX_REFLECTOR_TIME_FACTOR;
    public static final ForgeConfigSpec.ConfigValue<String> RECIPE_BLOCKED_LIST;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Options that affect the added Solar Cooker.").push("Solar Cooker Settings");

        VANILLA_RECIPES_ENABLED = builder
                .comment(VANILLA_RECIPES_ENABLED_DESCRIPTION)
                .define("vanillaRecipesEnabled", VANILLA_RECIPES_ENABLED_DEFAULT);
        VANILLA_RECIPE_TYPE = builder
                .comment(VANILLA_RECIPE_TYPE_DESCRIPTION)
                .define("vanillaRecipeType", ForgeRegistries.RECIPE_TYPES.getKey(VANILLA_RECIPE_TYPE_DEFAULT).getPath());
        COOK_TIME_FACTOR = builder
                .comment(COOK_TIME_FACTOR_DESCRIPTION)
                .defineInRange("cookTimeFactor", COOK_TIME_FACTOR_DEFAULT, COOK_TIME_FACTOR_MIN, COOK_TIME_FACTOR_MAX);
        MAX_REFLECTOR_TIME_FACTOR = builder
                .comment(MAX_REFLECTOR_TIME_FACTOR_DESCRIPTION)
                .defineInRange("maxReflectorSpeedFactor", MAX_REFLECTOR_TIME_FACTOR_DEFAULT, MAX_REFLECTOR_TIME_FACTOR_MIN, MAX_REFLECTOR_TIME_FACTOR_MAX);
        RECIPE_BLOCKED_LIST = builder
                .comment(RECIPE_BLOCKED_LIST_DESCRIPTION)
                .define("recipeBlockedList", RECIPE_BLOCKED_LIST_DEFAULT);

        builder.pop();

        SERVER_CONFIG = builder.build();
    }

    @Override
    public void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
        Path path = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(Constants.MOD_ID + "-server.toml");
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        SERVER_CONFIG.setConfig(configData);
    }

    @Override
    public boolean areVanillaRecipesEnabled() {
        try {
            return VANILLA_RECIPES_ENABLED.get();
        } catch (IllegalStateException ex) {
            return VANILLA_RECIPES_ENABLED_DEFAULT;
        }
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getRecipeType() {
        return switch (VANILLA_RECIPE_TYPE.get()) {
            case "smoking" -> RecipeType.SMOKING;
            case "smelting" -> RecipeType.SMELTING;
            case "campfire_cooking" -> RecipeType.CAMPFIRE_COOKING;
            case "blasting" -> RecipeType.BLASTING;
            default -> VANILLA_RECIPE_TYPE_DEFAULT;
        };
    }

    @Override
    public double getCookTimeFactor() {
        try {
            return COOK_TIME_FACTOR.get();
        } catch (IllegalStateException ex) {
            return COOK_TIME_FACTOR_DEFAULT;
        }
    }

    @Override
    public double getMaxReflectorTimeFactor() {
        try {
            return MAX_REFLECTOR_TIME_FACTOR.get();
        } catch (IllegalStateException ex) {
            return MAX_REFLECTOR_TIME_FACTOR_DEFAULT;
        }
    }

    @Override
    public String getRecipeBlockedList() {
        try {
            return RECIPE_BLOCKED_LIST.get();
        } catch (IllegalStateException ex) {
            return RECIPE_BLOCKED_LIST_DEFAULT;
        }
    }

}
