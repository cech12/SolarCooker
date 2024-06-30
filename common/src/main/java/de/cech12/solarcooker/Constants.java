package de.cech12.solarcooker;

import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * Class that contains all common constants.
 */
public class Constants {

    /** mod id */
    public static final String MOD_ID = "solarcooker";
    /** mod name*/
    public static final String MOD_NAME = "Solar Cooker";
    /** Logger instance */
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String SOLAR_COOKER_NAME = "solar_cooker";
    public static final String REFLECTOR_NAME = "reflector";
    public static final String SHINING_DIAMOND_BLOCK_NAME = "shining_diamond_block";

    public static final String SOLAR_COOKING_NAME = "solarcooking";

    public static final String SOLAR_COOKER_MENU_NAME = "solarcooker";

    public static Supplier<Block> SOLAR_COOKER_BLOCK;
    public static Supplier<Block> REFLECTOR_BLOCK;
    public static Supplier<Block> SHINING_DIAMOND_BLOCK_BLOCK;

    public static Supplier<BlockEntityType<? extends SolarCookerBlockEntity>> SOLAR_COOKER_ENTITY_TYPE;

    public static Supplier<Item> SOLAR_COOKER_ITEM;
    public static Supplier<Item> REFLECTOR_ITEM;
    public static Supplier<Item> SHINING_DIAMOND_BLOCK_ITEM;

    public static Supplier<RecipeType<SolarCookingRecipe>> SOLAR_COOKING_RECIPE_TYPE;

    public static Supplier<MenuType<SolarCookerContainer>> SOLAR_COOKER_MENU_TYPE;

    private Constants() {}

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

}