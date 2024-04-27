package de.cech12.solarcooker.init;

import de.cech12.solarcooker.CommonLoader;
import de.cech12.solarcooker.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModItems {

    private static final Item SOLAR_COOKER_ITEM = register(Constants.SOLAR_COOKER_NAME, Constants.SOLAR_COOKER_BLOCK);
    private static final Item REFLECTOR_ITEM = register(Constants.REFLECTOR_NAME, Constants.REFLECTOR_BLOCK);
    private static final Item SHINING_DIAMOND_BLOCK_ITEM = register(Constants.SHINING_DIAMOND_BLOCK_NAME, Constants.SHINING_DIAMOND_BLOCK_BLOCK);

    static {
        Constants.SOLAR_COOKER_ITEM = () -> SOLAR_COOKER_ITEM;
        Constants.REFLECTOR_ITEM = () -> REFLECTOR_ITEM;
        Constants.SHINING_DIAMOND_BLOCK_ITEM = () -> SHINING_DIAMOND_BLOCK_ITEM;
    }

    public static void init() {}

    private static Item register(String name, Supplier<Block> block) {
        return Registry.register(BuiltInRegistries.ITEM, CommonLoader.id(name), new BlockItem(block.get(), new Item.Properties()));
    }

}
