package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.item.ReflectorItem;
import de.cech12.solarcooker.item.ShiningDiamondItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_ITEM = solarCookerItem();
        Constants.REFLECTOR_ITEM = item(Constants.REFLECTOR_NAME, () -> new ReflectorItem(new Item.Properties().setId(id(Constants.REFLECTOR_NAME))));
        Constants.SHINING_DIAMOND_BLOCK_ITEM = shiningDiamondItem(Constants.SHINING_DIAMOND_BLOCK_NAME, Constants.SHINING_DIAMOND_BLOCK_BLOCK);
    }

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(BuiltInRegistries.ITEM.key(), Constants.id(name));
    }

    private static DeferredHolder<Item, Item> item(String name, Supplier<Item> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static DeferredHolder<Item, Item> shiningDiamondItem(String name, Supplier<Block> block) {
        return item(name, () -> new ShiningDiamondItem(block.get(), new Item.Properties().setId(id(name))));
    }

    private static DeferredHolder<Item, Item> solarCookerItem() {
        return item(Constants.SOLAR_COOKER_NAME, () -> new BlockItem(Constants.SOLAR_COOKER_BLOCK.get(), new Item.Properties().setId(id(Constants.SOLAR_COOKER_NAME))));
    }

}
