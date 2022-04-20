package cech12.solarcooker.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ServerConfig {
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final ForgeConfigSpec.BooleanValue VANILLA_RECIPES_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> VANILLA_RECIPE_TYPE;
    public static final ForgeConfigSpec.DoubleValue COOK_TIME_FACTOR;
    public static final ForgeConfigSpec.DoubleValue MAX_REFLECTOR_TIME_FACTOR;
    public static final ForgeConfigSpec.ConfigValue<String> RECIPE_BLACKLIST;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Options that affect the added Solar Cooker.").push("Solar Cooker Settings");
        VANILLA_RECIPES_ENABLED = builder
                .comment("If enabled, the vanilla blasting, smelting, or smoking recipes are used by the solar cooker.")
                .define("vanillaRecipesEnabled", true);
        VANILLA_RECIPE_TYPE = builder
                .comment("Defines which vanilla recipes the solar cooker can use. Possible values: \"smoking\" (default), \"smelting\", \"blasting\", \"campfire_cooking\"")
                .define("vanillaRecipeType", "smoking");
        COOK_TIME_FACTOR = builder
                .comment("Cook time factor of the solar cooker in relation to corresponding vanilla furnace. (i. e. 0.5 - half the time, 1.0 same time, 2.0 twice the time)")
                .defineInRange("cookTimeFactor", 4.0, 0.0, 100.0);
        MAX_REFLECTOR_TIME_FACTOR = builder
                .comment("Speed factor when all 4 reflectors are placed next to the solar cooker. (i. e. 0.5 - half the time, 1.0 same time)")
                .defineInRange("maxReflectorSpeedFactor", 0.25, 0.0, 1.0);
        RECIPE_BLACKLIST = builder
                .comment("A comma separated list of all vanilla recipes that should not be used by the solar cooker. Example: \"baked_potato,baked_potato_from_smoking,othermod:other_baked_food\"")
                .define("recipeBlacklist", "");
        builder.pop();

        SERVER_CONFIG = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    public static RecipeType<? extends AbstractCookingRecipe> getRecipeType() {
        return switch (VANILLA_RECIPE_TYPE.get()) {
            case "smelting" -> RecipeType.SMELTING;
            case "campfire_cooking" -> RecipeType.CAMPFIRE_COOKING;
            case "blasting" -> RecipeType.BLASTING;
            default -> RecipeType.SMOKING; // default to smoking
        };
    }

    public static boolean isRecipeNotBlacklisted(final ResourceLocation id) {
        String configValue = RECIPE_BLACKLIST.get().trim();
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
