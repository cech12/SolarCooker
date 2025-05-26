package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.item.ReflectorItem;
import de.cech12.solarcooker.item.ShiningDiamondItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {

    private static final Item SOLAR_COOKER_ITEM = register(Constants.SOLAR_COOKER_NAME, new BlockItem(Constants.SOLAR_COOKER_BLOCK.get(), new Item.Properties().setId(id(Constants.SOLAR_COOKER_NAME))));
    private static final Item REFLECTOR_ITEM = register(Constants.REFLECTOR_NAME, new ReflectorItem(new Item.Properties().setId(id(Constants.REFLECTOR_NAME))));
    private static final Item SHINING_DIAMOND_BLOCK_ITEM = register(Constants.SHINING_DIAMOND_BLOCK_NAME, new ShiningDiamondItem(Constants.SHINING_DIAMOND_BLOCK_BLOCK.get(), new Item.Properties().setId(id(Constants.SHINING_DIAMOND_BLOCK_NAME))));

    static {
        Constants.SOLAR_COOKER_ITEM = () -> SOLAR_COOKER_ITEM;
        Constants.REFLECTOR_ITEM = () -> REFLECTOR_ITEM;
        Constants.SHINING_DIAMOND_BLOCK_ITEM = () -> SHINING_DIAMOND_BLOCK_ITEM;
    }

    public static void init() {}

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(BuiltInRegistries.ITEM.key(), Constants.id(name));
    }

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Constants.id(name), item);
    }

}
